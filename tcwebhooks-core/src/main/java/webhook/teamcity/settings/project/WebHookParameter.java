package webhook.teamcity.settings.project;

import java.util.Map;

public interface WebHookParameter {
	
	public String getProjectInternalId();
	public void setProjectInternalId(String string);

	public String getName();
	public void setName(String name);
	
	public String getValue();
	public void setValue(String value);
	
	public Boolean getSecure();
	public void setSecure(Boolean isSecure);
	
	public Boolean getIncludedInLegacyPayloads();
	public void setIncludedInLegacyPayloads(Boolean isIncluded);
	
	public Map<String,String> getProperties();
	public void setProperties(Map<String,String> properties);
}
