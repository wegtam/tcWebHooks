package webhook.teamcity.server.rest.data;

import jetbrains.buildServer.server.rest.data.PermissionChecker;
import jetbrains.buildServer.serverSide.ProjectManager;
import jetbrains.buildServer.serverSide.SProject;
import jetbrains.buildServer.serverSide.auth.AccessDeniedException;
import jetbrains.buildServer.serverSide.auth.Permission;
import webhook.teamcity.server.rest.model.template.ErrorResult;
import webhook.teamcity.server.rest.model.webhook.ProjectWebHookFilter;
import webhook.teamcity.server.rest.model.webhook.ProjectWebhook;

import java.util.Objects;

public class WebHookConfigurationValidator {
	private static final String PROJECT_ID_KEY = "projectId";
	private final PermissionChecker myPermissionChecker;
	private final ProjectManager myProjectManager;
	
	public WebHookConfigurationValidator(PermissionChecker permissionChecker, ProjectManager projectManager) {
		this.myPermissionChecker = permissionChecker;
		this.myProjectManager = projectManager;
	}

	public ErrorResult validateNewWebHook(String projectId, ProjectWebhook newWebHook, ErrorResult result) {
		
		if (newWebHook.getId() != null && !newWebHook.getId().trim().isEmpty()) {
			result.addError("id-empty", "The webhook id must be empty. It will be generated by Teamcity");
		}
		
		validateStandardWebHookFields(projectId, newWebHook, result);
		
		return result;
		
	}

	public ErrorResult validateUpdatedWebHook(String externalId, ProjectWebhook updatedWebHook, ErrorResult result) {
		
		if (updatedWebHook.getId() == null || updatedWebHook.getId().trim().isEmpty()) {
			result.addError("id-empty", "The webhook id must not be empty.");
		}
		validateStandardWebHookFields(externalId, updatedWebHook, result);

		return result;
	}
	public ErrorResult validateStandardWebHookFields(String externalId, ProjectWebhook updatedWebHook, ErrorResult result) {
		
		if (updatedWebHook.getUrl() == null || updatedWebHook.getUrl().trim().isEmpty()) {
			result.addError("url", "The URL cannot be empty.");
		}
		
		if (updatedWebHook.getTemplate() == null || updatedWebHook.getTemplate().trim().isEmpty()) {
			result.addError("template", "The webhook template cannot be empty.");
		}
		
		if (updatedWebHook.getTemplate() == null || updatedWebHook.getTemplate().trim().isEmpty()) {
			result.addError("template", "The webhook template cannot be empty.");
		}
		
		if (updatedWebHook.getEnabled() == null) {
			result.addError("enabled", "The 'enabled' flag cannot be empty and must be 'true' or 'false'.");
		}
		
		validateProjectId(externalId, result);
		
		if (Objects.nonNull(updatedWebHook.getFilters())) {
			for (ProjectWebHookFilter f : updatedWebHook.getFilters().getFilters()) {
				WebHookFilterValidator.validateFilter(f, result);
			}
		}
		
		return result;
	}
	private ErrorResult validateProjectId(String projectId, ErrorResult result) {
		if (projectId != null && !projectId.isEmpty()) {
			SProject sProject = null;
			try {
				sProject = myProjectManager.findProjectByExternalId(projectId);
				
			} catch (AccessDeniedException ex) {
				result.addError(PROJECT_ID_KEY, "The TeamCity project is not visible to your user");
			}
			if (sProject == null) {
				result.addError(PROJECT_ID_KEY, "The projectId must refer to a valid TeamCity project");
			} else {
				if (! myPermissionChecker.isPermissionGranted(Permission.EDIT_PROJECT, sProject.getProjectId())) {
					result.addError(PROJECT_ID_KEY, "The TeamCity project is not writable by your user");
				}
			}
		} else {
			result.addError(PROJECT_ID_KEY, "The projectId cannot be empty");
		}
		return result;
	}
	
}
