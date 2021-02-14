package webhook.teamcity;

import jetbrains.buildServer.serverSide.BuildPromotion;
import jetbrains.buildServer.serverSide.TagData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class WebHookTagsEventHandlerImplTest {

    
    @Mock
    DeferrableServiceManager deferrableServiceManager;
    
    @Mock
    WebHookListener webHookListener;
    
    @InjectMocks
    WebHookTagsEventHandlerImpl webHookTagsEventHandler;
    
    @Test
    public void addTagsEvent() throws InterruptedException {
        
        webHookTagsEventHandler.setWebHookListener(webHookListener);
        List<TagData> firstTags = Arrays.asList(
                TagData.createPublicTag("prod"),
                TagData.createPublicTag("release")
        );

        List<TagData> secondTags = Arrays.asList(
                TagData.createPublicTag("prod")
        );
        BuildPromotion buildPromotion = mock(BuildPromotion.class);
        WebHookTagsEventHandlerImpl.TagWrapper wrapper01 = new WebHookTagsEventHandlerImpl.TagWrapper(
                Instant.now(),
                buildPromotion,
                null,
                firstTags,
                Collections.emptyList()
        );
        
        WebHookTagsEventHandlerImpl.TagWrapper wrapper02 = new WebHookTagsEventHandlerImpl.TagWrapper(
                Instant.now(),
                buildPromotion,
                null,
                Collections.emptyList(),
                secondTags
        );
        
        webHookTagsEventHandler.register();
        Thread.sleep(100);
        webHookTagsEventHandler.addTagsEvent(wrapper01);
        Thread.sleep(100);
        webHookTagsEventHandler.addTagsEvent(wrapper02);
        Thread.sleep(100);

        Mockito.verify(webHookListener, Mockito.timeout(10000).times(1))
                .processBuildTagsChanged(
                        ArgumentMatchers.eq(buildPromotion),
                        ArgumentMatchers.eq(null),
                        ArgumentMatchers.eq(firstTags),
                        ArgumentMatchers.eq(secondTags)
                        );
    }
}