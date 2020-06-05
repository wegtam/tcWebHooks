package webhook.teamcity.settings.project;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jetbrains.buildServer.agent.Constants;
import jetbrains.buildServer.serverSide.SProjectFeatureDescriptor;

public class WebHookParameterFactory {
	
	public static final String STRING_PROPERTY_PREFIX = "string.";
	public static final String BOOLEAN_PROPERTY_PREFIX = "boolean.";
	public static final String SECURE_PROPERTY_PREFIX = Constants.SECURE_PROPERTY_PREFIX;
	public static final String CONFIG_ID_KEY    = "configId";
	public static final String NAME_KEY   		= "name";
	private static final String VALUE_KEY 		= "value";
	private static final String PROJECT_ID_KEY  = "projectExternalId";
	private static final String SECURE_KEY  = "boolean.secure";
	private static final String LEGACY_PAYLOADS_KEY  = "boolean.includedInLegacyPayloads";
	
	private static final List<String> KEYS = Arrays.asList(NAME_KEY, VALUE_KEY, PROJECT_ID_KEY, SECURE_KEY, LEGACY_PAYLOADS_KEY);

	private WebHookParameterFactory(){}
	
	public static WebHookParameter readFrom(Map<String, String> parameters) {
		
		WebHookParameter model = new WebHookParameterModel();
		model.setProjectInternalId(parameters.get(PROJECT_ID_KEY));
		model.setName(parameters.get(NAME_KEY));
		model.setValue(parameters.get(VALUE_KEY));
		model.setProperties(new HashMap<String, String>());
		
//		parameters.forEach((k,v) -> {
//			if ( ! KEYS.contains(k)) {
//				model.getProperties().put(k, v);
//			}
//		});
		return model;
	}

	public static Map<String, String> asMap(WebHookParameter model) {
		Map<String,String> properties = new HashMap<>();
		properties.putAll(model.getProperties());
		
		properties.put(PROJECT_ID_KEY, model.getProjectInternalId());
		properties.put(NAME_KEY, model.getName());
		properties.put(VALUE_KEY, model.getValue());
		properties.put(SECURE_KEY, model.getSecure().toString());
		properties.put(LEGACY_PAYLOADS_KEY, model.getIncludedInLegacyPayloads().toString());
		
		return properties;
	}

	public static WebHookParameter fromDescriptor(SProjectFeatureDescriptor myDescriptor) {
		return readFrom(myDescriptor.getParameters());
	}
	
}
