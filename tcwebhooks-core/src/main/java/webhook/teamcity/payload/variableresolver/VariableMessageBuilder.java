package webhook.teamcity.payload.variableresolver;

public interface VariableMessageBuilder {

	@Deprecated
	public  abstract String build();
	public abstract String build(String template);
	
}
