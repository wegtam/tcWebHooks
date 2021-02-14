package webhook.teamcity.executor;

import jetbrains.buildServer.serverSide.BuildPromotion;
import jetbrains.buildServer.serverSide.TagData;
import webhook.WebHook;
import webhook.teamcity.BuildStateEnum;
import webhook.teamcity.WebHookContentBuilder;
import webhook.teamcity.history.WebHookHistoryItem;
import webhook.teamcity.history.WebHookHistoryItem.WebHookErrorStatus;
import webhook.teamcity.history.WebHookHistoryItemFactory;
import webhook.teamcity.history.WebHookHistoryRepository;
import webhook.teamcity.settings.WebHookConfig;

import java.util.Collection;

public class TaggedBuildWebHookRunner extends AbstractWebHookExecutor implements WebHookRunner {

    private final BuildPromotion buildPromotion;
    private final String user;
	private final Collection<TagData> oldTags;
	private final Collection<TagData> newTags;

	public TaggedBuildWebHookRunner(
			WebHookContentBuilder webHookContentBuilder,
			WebHookHistoryRepository webHookHistoryRepository,
			WebHookHistoryItemFactory webHookHistoryItemFactory,
			WebHookConfig whc,
			BuildStateEnum state,
			boolean isOverrideEnabled,
			WebHook webhook,
			BuildPromotion buildPromotion,
			String user,
			Collection<TagData> oldTags,
			Collection<TagData> newTags,
			boolean isTest)
	{
		super (
			 webHookContentBuilder,
			 webHookHistoryRepository,
			 webHookHistoryItemFactory,
			 whc,
			 state,
			 isOverrideEnabled,
			 webhook,
			 isTest);
		this.buildPromotion = buildPromotion;
		this.user = user;
		this.oldTags = oldTags;
		this.newTags = newTags;
	}


	@Override
	protected WebHook getWebHookContent() {
		return webHookContentBuilder.buildWebHookContent(webhook, whc, buildPromotion.getAssociatedBuild(), state, user, null, overrideIsEnabled);
	}

	@Override
	protected WebHookHistoryItem buildWebHookHistoryItem(WebHookErrorStatus errorStatus) {
		if (this.isTest) {
			return webHookHistoryItemFactory.getWebHookHistoryTestItem(
					whc,
					webhook.getExecutionStats(),
					buildPromotion.getBuildType(),
					errorStatus
			);

		} else {
			return webHookHistoryItemFactory.getWebHookHistoryItem(
					whc,
					webhook.getExecutionStats(),
					buildPromotion.getBuildType(),
					errorStatus
			);
		}
	}

}
