package webhook.teamcity;

import com.google.common.collect.Iterables;
import jetbrains.buildServer.log.LogInitializer;
import jetbrains.buildServer.serverSide.BuildPromotion;
import jetbrains.buildServer.serverSide.TagData;
import jetbrains.buildServer.users.User;
import jetbrains.buildServer.util.executors.ExecutorsFactory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;


@RequiredArgsConstructor
public class WebHookTagsEventHandlerImpl implements DeferrableService, WebHookTagsEventHandler {

    private final AtomicBoolean myStopped = new AtomicBoolean(true);
    private Map<Long, List<TagWrapper>> buildTagEvents = new HashMap<>();
    private long intervalForCheckingMsecs = 100;    // check every 100 ms.
    private long noNewEventsPeriodMsecs = 500;      // If no new events received in 500 ms, build and fire event.

    private final DeferrableServiceManager deferrableServiceManager;
    @Setter
    private WebHookListener webHookListener;
    private ScheduledExecutorService ourScheduler;


    @Override
    public void requestDeferredRegistration() {
        Loggers.SERVER.info("WebHookTagsEventHandler :: Registering as a deferrable service");
        deferrableServiceManager.registerService(this);
    }

    @Override
    public void register() {
        Loggers.SERVER.info("WebHookTagsEventHandler :: Starting scheduler");
        if (ourScheduler == null || (ourScheduler.isShutdown() && LogInitializer.isUnitTest())) {
            ourScheduler = ExecutorsFactory.newFixedScheduledDaemonExecutor("WebHookTagsEventHandler", 1);
        }
        start();
    }
    
    @Override
    public void addTagsEvent(TagWrapper tagWrapper) {
        synchronized (buildTagEvents) {
            Loggers.SERVER.debug("WebHookTagsEventHandler :: Adding tags " + tagWrapper);
            List<WebHookTagsEventHandlerImpl.TagWrapper> existingEventsForBuild = this.buildTagEvents.getOrDefault(tagWrapper.buildPromotion.getId(), new ArrayList<>());
            existingEventsForBuild.add(tagWrapper);
            buildTagEvents.put(tagWrapper.buildPromotion.getId(), existingEventsForBuild);
        }
    }

    private void checkForRecentEvents() {
        synchronized (buildTagEvents) {
            buildTagEvents.forEach((buildId, events) -> {
                if (!events.isEmpty() && Iterables.getLast(events).getEventTime().plusMillis(noNewEventsPeriodMsecs).isBefore(Instant.now())) {
                    TagWrapper first = Iterables.getFirst(events, null);
                    TagWrapper last = Iterables.getLast(events);
                    Loggers.SERVER.debug("WebHookTagsEventHandler :: Sending tagsEvent for build: " + buildId);
                    webHookListener.processBuildTagsChanged(first.getBuildPromotion(), first.getUser(), first.getOldTags(),last.getNewTags());
                    this.buildTagEvents.put(buildId, new ArrayList<>());
                    Loggers.SERVER.debug("WebHookTagsEventHandler :: Done sending tagsEvent for build: " + buildId);
                }
            });
        }
    }
    
    /**
     * Starts observer
     */
    public void start() {
        myStopped.set(false);
        scheduleMe();
    }

    /**
     * Stops observer
     */
    public void stop() {
        myStopped.set(true);
    }

    private void scheduleMe() {
        final ScheduledExecutorService scheduler = ourScheduler; // it's volatile
        assert scheduler != null;
        try {
            scheduler.schedule(new Runnable() {
                public void run() {
                    try {
                        if (myStopped.get()) return;
                        checkForRecentEvents();
                        scheduleMe();
                    } catch (Throwable t) {
                        Loggers.SERVER.warn("WebHookTagsEventHandler task failed: " + t.getMessage(), t);
                    }
                }
            }, intervalForCheckingMsecs, TimeUnit.MILLISECONDS);
        } catch (RejectedExecutionException e) {
            Loggers.SERVER.info(e.toString());
            Loggers.SERVER.debug(e.toString(), e);
        }
    }

    @AllArgsConstructor
    @Getter @ToString
    public static class TagWrapper {
        @NotNull Instant eventTime;
        @NotNull BuildPromotion buildPromotion;
        @Nullable String user;
        @NotNull Collection<TagData> oldTags;
        @NotNull Collection<TagData> newTags;    }
}
