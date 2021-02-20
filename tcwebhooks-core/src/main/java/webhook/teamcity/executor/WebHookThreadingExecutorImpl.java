package webhook.teamcity.executor;

import jetbrains.buildServer.serverSide.BuildPromotion;
import jetbrains.buildServer.serverSide.SBuild;
import jetbrains.buildServer.serverSide.SQueuedBuild;
import jetbrains.buildServer.serverSide.TagData;
import jetbrains.buildServer.serverSide.executors.ExecutorServices;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import webhook.WebHook;
import webhook.teamcity.BuildStateEnum;
import webhook.teamcity.Loggers;
import webhook.teamcity.payload.content.WebHooksTags;
import webhook.teamcity.settings.WebHookConfig;

import java.util.Collection;

@AllArgsConstructor
public class WebHookThreadingExecutorImpl implements WebHookThreadingExecutor {
	
    private final WebHookRunnerFactory webHookRunnerFactory;
    private final ExecutorServices executorServices;

	@Override
	public void execute(WebHook webhook, WebHookConfig whc, SQueuedBuild sQueuedBuild, 
						BuildStateEnum state, String user, String comment, boolean isTest) 
	{
		Loggers.SERVER.debug("WebHookThreadingExecutorImpl :: About to schedule runner for webhook :: " + 
				webhook.getExecutionStats().getTrackingIdAsString() + " : " + whc.getUniqueKey());
		
		WebHookRunner runner = webHookRunnerFactory.getRunner(webhook, whc, sQueuedBuild, state, user, comment, isTest);
		executorServices.getNormalExecutorService().execute(runner);
		
		Loggers.SERVER.debug("WebHookThreadingExecutorImpl :: Finished scheduling runner for webhook :: " + 
				webhook.getExecutionStats().getTrackingIdAsString() + " : " + whc.getUniqueKey());
	}

	@Override
	public void execute(WebHook webhook, WebHookConfig whc, BuildStateEnum state, WebHookResponsibilityHolder responsibilityHolder,
			boolean isTest) {
		Loggers.SERVER.debug("WebHookThreadingExecutorImpl :: About to schedule runner for webhook :: " + 
				webhook.getExecutionStats().getTrackingIdAsString() + " : " + whc.getUniqueKey());
		
		WebHookRunner runner = webHookRunnerFactory.getRunner(webhook, whc, state, responsibilityHolder, isTest);
		executorServices.getNormalExecutorService().execute(runner);
		
		Loggers.SERVER.debug("WebHookThreadingExecutorImpl :: Finished scheduling runner for webhook :: " + 
				webhook.getExecutionStats().getTrackingIdAsString() + " : " + whc.getUniqueKey());
	}

	@Override
	public void execute(@NotNull WebHook webhook, @NotNull WebHookConfig whc, 
						@NotNull BuildPromotion buildPromotion, @NotNull BuildStateEnum state, 
						@Nullable String user, @NotNull WebHooksTags tags, 
						boolean isTest) {
		Loggers.SERVER.debug("WebHookThreadingExecutorImpl :: About to schedule runner for webhook :: " +
				webhook.getExecutionStats().getTrackingIdAsString() + " : " + whc.getUniqueKey());

		WebHookRunner runner = webHookRunnerFactory.getRunner(webhook, whc, buildPromotion, state, user, tags, isTest);
		executorServices.getNormalExecutorService().execute(runner);

		Loggers.SERVER.debug("WebHookThreadingExecutorImpl :: Finished scheduling runner for webhook :: " +
				webhook.getExecutionStats().getTrackingIdAsString() + " : " + whc.getUniqueKey());
	}

	@Override
	public void execute(WebHook webhook, WebHookConfig whc, SBuild sBuild, BuildStateEnum state, String user, String comment,
			boolean isTest) {
		Loggers.SERVER.debug("WebHookThreadingExecutorImpl :: About to schedule runner for webhook :: " + 
				webhook.getExecutionStats().getTrackingIdAsString() + " : " + whc.getUniqueKey());
		
		WebHookRunner runner = webHookRunnerFactory.getRunner(webhook, whc, sBuild, state, user, comment, isTest);
		executorServices.getNormalExecutorService().execute(runner);
		
		Loggers.SERVER.debug("WebHookThreadingExecutorImpl :: Finished scheduling runner for webhook :: " + 
				webhook.getExecutionStats().getTrackingIdAsString() + " : " + whc.getUniqueKey());
		
	}
	
}
