package webhook.teamcity.settings.project;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import chatbot.teamcity.model.ChatClientConfig;
import jetbrains.buildServer.serverSide.SProjectFeatureDescriptor;

public class WebHookParameterFactory {
	
	public  static final String CONFIG_ID_KEY   = "configId";
	public  static final String NAME_KEY   		= "name";
	private static final String TYPE_KEY 		= "type";
	private static final String VALUE_KEY 		= "value";
	private static final String PROJECT_ID_KEY  = "projectInternalId";
	private static final String EMAIL_AUTO_KEY  = "boolean.emailAutoMappingEnabled";
	
	private static final List<String> KEYS = Arrays.asList(CONFIG_ID_KEY, NAME_KEY, CLIENT_TYPE_KEY, PROJECT_ID_KEY, EMAIL_AUTO_KEY);

	private ChatClientConfigFactory(){}
	
	public static WebHookParameter readFrom(Map<String, String> parameters) {
		
		WebHookParameter model = new WebHookParameterModel();
		model.setName(parameters.get(NAME_KEY));
		model.setProjectInternalId(parameters.get(PROJECT_ID_KEY));
		model.setClientType(parameters.get(CLIENT_TYPE_KEY));
		model.setEmailAutoMappingEnabled(Boolean.parseBoolean(parameters.get(EMAIL_AUTO_KEY)));
		model.setProperties(new HashMap<String, String>());
		
		parameters.forEach((k,v) -> {
			if ( ! KEYS.contains(k)) {
				model.getProperties().put(k, v);
			}
		});
		return model;
	}

	public static Map<String, String> asMap(WebHookParameter model) {
		Map<String,String> properties = new HashMap<>();
		properties.putAll(model.getProperties());
		
		properties.put(CONFIG_ID_KEY, model.getConfigId());
		properties.put(NAME_KEY, model.getName());
		properties.put(PROJECT_ID_KEY, model.getProjectInternalId());
		properties.put(TYPE_KEY, model.getType());
		properties.put(EMAIL_AUTO_KEY, model.getEmailAutoMappingEnabled().toString());
		
		return properties;
	}

	public static WebHookParameter fromDescriptor(SProjectFeatureDescriptor myDescriptor) {
		return readFrom(myDescriptor.getParameters());
	}
	
}
