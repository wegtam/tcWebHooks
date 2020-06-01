package webhook.teamcity.settings.project;

import java.util.Map;

import jetbrains.buildServer.agent.Constants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class WebHookParameterModel implements WebHookParameter {
	
	public static final String BOOLEAN_PROPERTY_PREFIX = "boolean:";
	public static final String SECURE_PROPERTY_PREFIX = Constants.SECURE_PROPERTY_PREFIX;

	String configId;
	String name;
	/** Type, eg "slack" */
	String type;
	String projectInternalId;
	Boolean emailAutoMappingEnabled;
	Map<String,String> properties;
	
}
