package webhook.teamcity.executor;

import jetbrains.buildServer.serverSide.BuildPromotion;
import jetbrains.buildServer.serverSide.SBuild;
import jetbrains.buildServer.serverSide.SProject;
import jetbrains.buildServer.serverSide.SQueuedBuild;
import jetbrains.buildServer.serverSide.TagData;
import lombok.AllArgsConstructor;
import webhook.WebHook;
import webhook.teamcity.BuildStateEnum;
import webhook.teamcity.WebHookContentBuilder;
import webhook.teamcity.history.WebHookHistoryItemFactory;
import webhook.teamcity.history.WebHookHistoryRepository;
import webhook.teamcity.payload.content.WebHooksTags;
import webhook.teamcity.settings.WebHookConfig;
import webhook.teamcity.statistics.StatisticsReport;

import java.util.Collection;

import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
public class WebHookRunnerFactory {
	
	private WebHookContentBuilder webHookContentBuilder;
	private WebHookHistoryRepository webHookHistoryRepository;
	private WebHookHistoryItemFactory webHookHistoryItemFactory;

	public WebHookRunner getRunner(WebHook webhook, WebHookConfig whc, SBuild sBuild, BuildStateEnum state, String username,
			String comment, boolean isTest) {
		return new BuildEventWebHookRunner(
				webHookContentBuilder, 
				webHookHistoryRepository, 
				webHookHistoryItemFactory, 
				whc, 
				state,
				username,
				comment,
				isTest,	// Test enables override too.
				webhook,
				sBuild,
				isTest
			);
	}

	public WebHookRunner getRunner(
			WebHook webhook, WebHookConfig whc, BuildStateEnum state,
			WebHookResponsibilityHolder responsibilityHolder,
			boolean isTest) {
		return new ResponsibilityChangedWebHookRunner(
				webHookContentBuilder, 
				webHookHistoryRepository, 
				webHookHistoryItemFactory, 
				whc,
				state,
				isTest,	// Test enables override too.
				webhook,
				responsibilityHolder,
				isTest
			);
	}

	public WebHookRunner getRunner(WebHook webhook, WebHookConfig whc, SQueuedBuild sQueuedBuild, BuildStateEnum state,
			String user, String comment, boolean isTest) {
		return new QueuedBuildWebHookRunner(
				webHookContentBuilder, 
				webHookHistoryRepository, 
				webHookHistoryItemFactory, 
				whc, 
				state, 
				isTest,	// Test enables override too.
				webhook, 
				sQueuedBuild, 
				user, 
				comment,
				isTest
			);
	}

	public WebHookRunner getRunner(WebHook webhook, WebHookConfig whc, BuildPromotion buildPromotion, BuildStateEnum state, 
			   String user, WebHooksTags tags, boolean isTest) {
		return new TaggedBuildWebHookRunner(
				webHookContentBuilder,
				webHookHistoryRepository,
				webHookHistoryItemFactory,
				whc,
				state,
				isTest,	// Test enables override too.
				webhook,
				buildPromotion,
				user,
				tags,
				isTest
			);
		}
	
	public WebHookRunner getRunner(WebHook webhook, WebHookConfig whc, BuildStateEnum state, StatisticsReport report, SProject rootProject, boolean isTest) {
		return new StatisticsReporterWebHookRunner(
				webHookContentBuilder, 
				webHookHistoryRepository, 
				webHookHistoryItemFactory, 
				whc, 
				state, 
				isTest, 
				webhook, 
				isTest, 
				rootProject, 
				report
			);
	}

}
