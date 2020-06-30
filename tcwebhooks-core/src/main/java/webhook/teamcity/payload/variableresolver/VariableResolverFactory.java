package webhook.teamcity.payload.variableresolver;

import webhook.teamcity.payload.PayloadTemplateEngineType;
import webhook.teamcity.payload.WebHookContentObjectSerialiser;
import webhook.teamcity.payload.content.ExtraParameters;

public interface VariableResolverFactory {
	
	public abstract void register();
	public abstract void setWebHookVariableResolverManager(WebHookVariableResolverManager variableResolverManager);
	public abstract PayloadTemplateEngineType getPayloadTemplateType();
	public abstract String getVariableResolverFactoryName();
	public abstract VariableMessageBuilder createVariableMessageBuilder(final String template, VariableResolver resolver);
	public abstract VariableResolver buildVariableResolver(WebHookContentObjectSerialiser webhookPayload, Object javaBean, ExtraParameters extraAndTeamCityProperties);
	

}
