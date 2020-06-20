package webhook.teamcity.server.rest.data;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import jetbrains.buildServer.server.rest.data.Locator;
import jetbrains.buildServer.server.rest.model.Fields;
import jetbrains.buildServer.server.rest.model.PagerData;
import jetbrains.buildServer.serverSide.ProjectManager;
import jetbrains.buildServer.serverSide.SProject;
import webhook.teamcity.server.rest.model.parameter.ProjectWebhookParameter;
import webhook.teamcity.server.rest.model.parameter.ProjectWebhookParameters;
import webhook.teamcity.server.rest.util.BeanContext;
import webhook.teamcity.settings.project.WebHookParameter;
import webhook.teamcity.settings.project.WebHookParameterStore;

public class WebHookParameterFinder {
	
	@NotNull private final ProjectManager projectManager;
	@NotNull private final WebHookParameterStore myWebHookParameterStore;
	
	public WebHookParameterFinder(
			@NotNull final ProjectManager projectManager,
			@NotNull final WebHookParameterStore webHookParameterStore)
	{
		this.projectManager = projectManager;
		this.myWebHookParameterStore = webHookParameterStore;
	}

	public static String getLocator(final WebHookParameter webhookParameter) {
		return Locator.createEmptyLocator().setDimension("id", webhookParameter.getId()).getStringRepresentation();
	}

	public ProjectWebhookParameters getAllWebHookParameters(SProject sProject, PagerData pagerData, Fields fields, BeanContext myBeanContext) {
		return new ProjectWebhookParameters(
				myWebHookParameterStore.getAllWebHookParameters(sProject),
				sProject.getExternalId(), 
				pagerData, 
				fields, 
				myBeanContext);
	}
	
	public ProjectWebhookParameter findWebhookParameter(SProject sProject, Fields fields, BeanContext myBeanContext) {
		return null;
		
	}
	
	public WebHookParameter findWebhookParameter(SProject sProject, String name) {
		return myWebHookParameterStore.findWebHookParameter(sProject, name);
	}

	public WebHookParameter findWebhookParameterById(SProject sProject, String parameterId) {
		return myWebHookParameterStore.getWebHookParameterById(sProject, parameterId);
	}
}
