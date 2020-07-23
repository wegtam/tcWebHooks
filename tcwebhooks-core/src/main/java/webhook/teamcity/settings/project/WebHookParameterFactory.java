package webhook.teamcity.settings.project;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import jetbrains.buildServer.agent.Constants;
import jetbrains.buildServer.serverSide.SProjectFeatureDescriptor;

public class WebHookParameterFactory {
	
	public static final String STRING_PROPERTY_PREFIX = "string.";
	public static final String BOOLEAN_PROPERTY_PREFIX = "boolean.";
	public static final String SECURE_PROPERTY_PREFIX = Constants.SECURE_PROPERTY_PREFIX;
	public static final String CONFIG_ID_KEY    = "configId";
	public static final String NAME_KEY   		= "name";
	private static final String VALUE_KEY 		= "value";
	private static final String SECURE_KEY  = "boolean.secure";
	private static final String LEGACY_PAYLOADS_KEY  = "boolean.includedInLegacyPayloads";
	
	private WebHookParameterFactory(){}
	
	public static WebHookParameter readFrom(String id, Map<String, String> parameters) {
		
		WebHookParameter model = new WebHookParameterModel();
		model.setId(id);
		model.setName(parameters.get(NAME_KEY));
		model.setValue(parameters.get(VALUE_KEY));
		return model;
	}

	public static Map<String, String> asMap(WebHookParameter model) {
		Map<String,String> properties = new HashMap<>();
		
		properties.put(NAME_KEY, model.getName());
		properties.put(VALUE_KEY, model.getValue());
		properties.put(SECURE_KEY, model.getSecure() != null ? model.getSecure().toString() : "false");
		properties.put(LEGACY_PAYLOADS_KEY, model.getIncludedInLegacyPayloads() != null ? model.getIncludedInLegacyPayloads().toString() : "false");
		
		return properties;
	}

	public static WebHookParameter fromDescriptor(SProjectFeatureDescriptor myDescriptor) {
		return readFrom(myDescriptor.getId(), myDescriptor.getParameters());
	}
	
}
