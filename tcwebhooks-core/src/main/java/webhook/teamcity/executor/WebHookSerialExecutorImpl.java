package webhook.teamcity.executor;

import jetbrains.buildServer.serverSide.BuildPromotion;
import jetbrains.buildServer.serverSide.SBuild;
import jetbrains.buildServer.serverSide.SProject;
import jetbrains.buildServer.serverSide.SQueuedBuild;
import jetbrains.buildServer.serverSide.TagData;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import webhook.WebHook;
import webhook.teamcity.BuildStateEnum;
import webhook.teamcity.Loggers;
import webhook.teamcity.settings.WebHookConfig;
import webhook.teamcity.statistics.StatisticsReport;

import java.util.Collection;

@AllArgsConstructor
public class WebHookSerialExecutorImpl implements WebHookSerialExecutor, WebHookStatisticsExecutor {
	
    private final WebHookRunnerFactory webHookRunnerFactory;

	@Override
	public void execute(WebHook webhook, WebHookConfig whc, SQueuedBuild sQueuedBuild, 
						BuildStateEnum state, String user, String comment, boolean isTest) 
	{
		Loggers.SERVER.debug("WebHookSerialExecutorImpl :: About to start runner for webhook :: " + 
				webhook.getExecutionStats().getTrackingIdAsString() + " : " + whc.getUniqueKey());

		webHookRunnerFactory.getRunner(webhook, whc, sQueuedBuild, state, user, comment, isTest).run();
		
		Loggers.SERVER.debug("WebHookSerialExecutorImpl :: Finished runner for webhook :: " + 
				webhook.getExecutionStats().getTrackingIdAsString() + " : " + whc.getUniqueKey());
	}

	@Override
	public void execute(WebHook webhook, WebHookConfig whc, BuildStateEnum state,
			WebHookResponsibilityHolder responsibilityHolder, boolean isTest) {
		
		Loggers.SERVER.debug("WebHookSerialExecutorImpl :: About to start runner for webhook :: " + 
				webhook.getExecutionStats().getTrackingIdAsString() + " : " + whc.getUniqueKey());

		webHookRunnerFactory.getRunner(webhook, whc, state, responsibilityHolder, isTest).run();
		
		Loggers.SERVER.debug("WebHookSerialExecutorImpl :: Finished runner for webhook :: " + 
				webhook.getExecutionStats().getTrackingIdAsString() + " : " + whc.getUniqueKey());
	}

	@Override
	public void execute(WebHook webhook, WebHookConfig whc, SBuild sBuild, BuildStateEnum state, String username,
			String comment, boolean isTest) 
	{
		Loggers.SERVER.debug("WebHookSerialExecutorImpl :: About to start runner for webhook :: " + 
				webhook.getExecutionStats().getTrackingIdAsString() + " : " + whc.getUniqueKey());

		webHookRunnerFactory.getRunner(webhook, whc, sBuild, state, username, comment, isTest).run();
		
		Loggers.SERVER.debug("WebHookSerialExecutorImpl :: Finished runner for webhook :: " + 
				webhook.getExecutionStats().getTrackingIdAsString() + " : " + whc.getUniqueKey());
	}

	@Override
	public void execute(@NotNull WebHook webhook, @NotNull WebHookConfig whc, 
						@NotNull BuildPromotion buildPromotion, @NotNull BuildStateEnum state, @Nullable String user, 
						@NotNull Collection<TagData> oldTags, @NotNull Collection<TagData> newTags, boolean isTest) {

		Loggers.SERVER.debug("WebHookSerialExecutorImpl :: About to schedule runner for webhook :: " +
				webhook.getExecutionStats().getTrackingIdAsString() + " : " + whc.getUniqueKey());

		webHookRunnerFactory.getRunner(webhook, whc, buildPromotion, state, user, oldTags, newTags, isTest).run();

		Loggers.SERVER.debug("WebHookSerialExecutorImpl :: Finished scheduling runner for webhook :: " +
				webhook.getExecutionStats().getTrackingIdAsString() + " : " + whc.getUniqueKey());

	}
	
	@Override
	public void execute(WebHook webhook, WebHookConfig whc, BuildStateEnum state, 
			StatisticsReport report, SProject rootProject, boolean isTest) {
		Loggers.SERVER.debug("WebHookSerialExecutorImpl :: About to start runner for webhook :: " + 
				webhook.getExecutionStats().getTrackingIdAsString() + " : " + whc.getUniqueKey());

		webHookRunnerFactory.getRunner(webhook, whc, state, report, rootProject, isTest).run();
		
		Loggers.SERVER.debug("WebHookSerialExecutorImpl :: Finished runner for webhook :: " + 
				webhook.getExecutionStats().getTrackingIdAsString() + " : " + whc.getUniqueKey());
	}
	
}
