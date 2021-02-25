package webhook.teamcity.executor;

import jetbrains.buildServer.serverSide.BuildPromotion;
import jetbrains.buildServer.serverSide.SBuild;
import jetbrains.buildServer.serverSide.SProject;
import jetbrains.buildServer.serverSide.SQueuedBuild;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import webhook.WebHook;
import webhook.teamcity.BuildStateEnum;
import webhook.teamcity.payload.content.WebHooksTags;
import webhook.teamcity.settings.WebHookConfig;
import webhook.teamcity.settings.WebHookMainSettings;
import webhook.teamcity.statistics.StatisticsReport;

public class WebHookExecutorManager implements WebHookExecutor, WebHookStatisticsExecutor {
	
	boolean useThreadedExecutor = true;
	private WebHookMainSettings myWebHookMainSettings;
	private WebHookSerialExecutor myWebHookSerialExecutor;
	private WebHookThreadingExecutor myWebHookThreadingExecutor;
	
	public WebHookExecutorManager(
			WebHookMainSettings webHookMainSettings,
			WebHookSerialExecutor webHookSerialExecutor,
			WebHookThreadingExecutor webHookThreadingExecutor) {
		myWebHookMainSettings = webHookMainSettings;
		myWebHookSerialExecutor = webHookSerialExecutor;
		myWebHookThreadingExecutor = webHookThreadingExecutor;
	}

	@Override
	public void execute(WebHook webHook, WebHookConfig whc, SQueuedBuild sBuild, BuildStateEnum state, String username,
			String comment, boolean isTest) {
		if (myWebHookMainSettings.useThreadedExecutor()) {
			myWebHookThreadingExecutor.execute(webHook, whc, sBuild, state, username, comment, isTest);
		} else {
			myWebHookSerialExecutor.execute(webHook, whc, sBuild, state, username, comment, isTest);
		}
	}
	
	@Override
	public void execute(WebHook webHook, WebHookConfig whc, BuildStateEnum state,
			WebHookResponsibilityHolder responsibilityHolder, boolean isTest) {
		if (myWebHookMainSettings.useThreadedExecutor()) {
			myWebHookThreadingExecutor.execute(webHook, whc, state, responsibilityHolder, isTest);
		} else {
			myWebHookSerialExecutor.execute(webHook, whc, state, responsibilityHolder, isTest);
		}
	}

	@Override
	public void execute(@NotNull WebHook webHook, @NotNull WebHookConfig webHookConfig, 
						@NotNull BuildPromotion buildPromotion, @NotNull BuildStateEnum state, 
						@Nullable String user, @NotNull WebHooksTags tags, 
						boolean isTest) {
		if (myWebHookMainSettings.useThreadedExecutor()) {
			myWebHookThreadingExecutor.execute(webHook, webHookConfig, buildPromotion, state, user, tags, isTest);
		} else {
			myWebHookSerialExecutor.execute(webHook, webHookConfig, buildPromotion, state, user, tags, isTest);
		}
	}


	@Override
	public void execute(WebHook webHook, WebHookConfig whc, SBuild sBuild, BuildStateEnum state, String username,
			String comment, boolean isTest) {
		if (myWebHookMainSettings.useThreadedExecutor()) {
			myWebHookThreadingExecutor.execute(webHook, whc, sBuild, state, username, comment, isTest);
		} else {
			myWebHookSerialExecutor.execute(webHook, whc, sBuild, state, username, comment, isTest);
		}
	}

	@Override
	public void execute(WebHook webHook, WebHookConfig whc, BuildStateEnum state, StatisticsReport report, SProject rootProject, boolean isTest) {
		myWebHookSerialExecutor.execute(webHook, whc, state, report, rootProject, isTest);

	}

}
