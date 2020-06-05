package webhook.teamcity.settings.project;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class WebHookParameterModel implements WebHookParameter {
	
	private String projectInternalId;
	private String name;
	private String value;
	private Boolean secure;
	private Boolean includedInLegacyPayloads;
	private Map<String,String> properties;
	
}
