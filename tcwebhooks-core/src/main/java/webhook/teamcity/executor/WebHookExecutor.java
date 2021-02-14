package webhook.teamcity.executor;

import jetbrains.buildServer.serverSide.BuildPromotion;
import jetbrains.buildServer.serverSide.TagData;
import org.jetbrains.annotations.NotNull;

import jetbrains.buildServer.serverSide.SBuild;
import jetbrains.buildServer.serverSide.SQueuedBuild;
import org.jetbrains.annotations.Nullable;
import webhook.WebHook;
import webhook.teamcity.BuildStateEnum;
import webhook.teamcity.settings.WebHookConfig;

import java.util.Collection;

public interface WebHookExecutor {
	
	/**
	 * Executor for Add/Remove from Queue events.
	 * @param webHook
	 * @param sBuild
	 * @param state
	 * @param user
	 * @param comment
	 */
	public void execute(
			@NotNull WebHook webHook,
			@NotNull WebHookConfig whc,
			@NotNull SQueuedBuild sBuild, 
			@NotNull BuildStateEnum state,
			String user, 
			String comment,
			boolean isTest
		);
	
	/** 
	 * Executor for other build events.
	 * @param sBuild
	 * @param state
	 */
	public void execute(
			@NotNull WebHook webHook,
			@NotNull WebHookConfig whc,
			@NotNull SBuild sBuild, 
			@NotNull BuildStateEnum state,
			String user, 
			String comment,
			boolean isTest
		);

	/**
	 * Executor for responsibility events.
	 * @param webHook
	 * @param webHookConfig
	 * @param state
	 * @param responsibilityHolder
	 * @param isTest
	 */
	public void execute(
			@NotNull WebHook webHook, 
			@NotNull WebHookConfig webHookConfig, 
			@NotNull BuildStateEnum state,
			@NotNull WebHookResponsibilityHolder responsibilityHolder, 
			boolean isTest);

	/**
	 * Executor for build tagged events.
	 * @param webHook
	 * @param webHookConfig
	 * @param buildPromotion
	 * @param state
	 * @param user
	 * @param oldTags
	 * @param newTags
	 * @param isTest
	 */
	public void execute(
			@NotNull WebHook webHook,
			@NotNull WebHookConfig webHookConfig,
			@NotNull BuildPromotion buildPromotion,
			@NotNull BuildStateEnum state, 
			@Nullable String user,
			@NotNull Collection<TagData> oldTags,
			@NotNull Collection<TagData> newTags, 
			boolean isTest);
}
