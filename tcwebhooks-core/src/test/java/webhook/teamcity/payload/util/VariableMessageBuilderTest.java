package webhook.teamcity.payload.util;

import static org.junit.Assert.*;

import org.junit.Test;

import webhook.teamcity.BuildStateEnum;
import webhook.teamcity.payload.WebHookPayloadDefaultTemplates;
import webhook.teamcity.payload.content.WebHookPayloadContent;
import webhook.teamcity.payload.content.WebHookPayloadContent.SimpleSerialiser;
import webhook.teamcity.payload.variableresolver.VariableMessageBuilder;
import webhook.teamcity.payload.variableresolver.standard.WebHooksBeanUtilsVariableResolver;
import webhook.teamcity.settings.secure.WebHookSecretResolver;
import webhook.teamcity.settings.secure.WebHookSecretResolverImpl;
import webhook.teamcity.settings.secure.WebHookSecretResolverNoOpImpl;

public class VariableMessageBuilderTest extends VariableMessageBuilderTestBase {
	
	WebHookSecretResolver webHookSecretResolver = new WebHookSecretResolverNoOpImpl();
	
	@Test
	public void testBuild() {
		WebHookPayloadContent content = new WebHookPayloadContent(variableResolverFactory, sBuildServer, sRunningBuild, previousSuccessfulBuild, BuildStateEnum.BEFORE_BUILD_FINISHED, extraParameters, WebHookPayloadDefaultTemplates.getDefaultEnabledPayloadTemplates());
		VariableMessageBuilder builder = variableResolverFactory.createVariableMessageBuilder("This is a test ${buildFullName}", new WebHooksBeanUtilsVariableResolver(content.getProject(), new SimpleSerialiser(), content, allProperties, webHookSecretResolver));
		assertEquals("This is a test Test Project :: Test Build", builder.build());
		System.out.println(content.getBuildFullName());
		
	}
	
	@Test
	public void testBuildWithDoubleResultion() {
		WebHookPayloadContent content = new WebHookPayloadContent(variableResolverFactory, sBuildServer, sRunningBuild, previousSuccessfulBuild, BuildStateEnum.BEFORE_BUILD_FINISHED, extraParameters, WebHookPayloadDefaultTemplates.getDefaultEnabledPayloadTemplates());
		VariableMessageBuilder builder = variableResolverFactory.createVariableMessageBuilder("This is a test ${buildFullName}", new WebHooksBeanUtilsVariableResolver(content.getProject(), new SimpleSerialiser(), content, allProperties, webHookSecretResolver));
		assertEquals("This is a test Test Project :: Test Build", builder.build());
		System.out.println(content.getBuildFullName());
		
	}
	
	@Test
	public void testTeamCityProperties() {
		WebHookPayloadContent content = new WebHookPayloadContent(variableResolverFactory, sBuildServer, sRunningBuild, previousSuccessfulBuild, BuildStateEnum.BEFORE_BUILD_FINISHED, extraParameters, WebHookPayloadDefaultTemplates.getDefaultEnabledPayloadTemplates());
		VariableMessageBuilder builder = variableResolverFactory.createVariableMessageBuilder("This is a test ${env.isInATest}", new WebHooksBeanUtilsVariableResolver(content.getProject(), new SimpleSerialiser(), content, allProperties, webHookSecretResolver));
		System.out.println(builder.build());
		System.out.println(content.getBuildFullName());
		assertEquals("This is a test Yes, we are in a test", builder.build());
	}
	
	@Test
	public void testDateTemplateProperty() {
		WebHookPayloadContent content = new WebHookPayloadContent(variableResolverFactory, sBuildServer, sRunningBuild, previousSuccessfulBuild, BuildStateEnum.BEFORE_BUILD_FINISHED, extraParameters, WebHookPayloadDefaultTemplates.getDefaultEnabledPayloadTemplates());
		VariableMessageBuilder builder = variableResolverFactory.createVariableMessageBuilder("The date now is ${now(\"yyyy-MM-dd'T'HH:mm:ss.SSSXXX\")}", new WebHooksBeanUtilsVariableResolver(content.getProject(), new SimpleSerialiser(), content, allProperties, webHookSecretResolver));
		System.out.println(builder.build());
		builder = variableResolverFactory.createVariableMessageBuilder("The month now is ${now(\"yyyy-MM\")}", new WebHooksBeanUtilsVariableResolver(content.getProject(), new SimpleSerialiser(), content, allProperties, webHookSecretResolver));
		System.out.println(builder.build());
		System.out.println(content.getBuildFullName());
	}
	
	@Test
	public void testSanitiseTemplateProperty() {
		WebHookPayloadContent content = new WebHookPayloadContent(variableResolverFactory, sBuildServer, sRunningBuild, previousSuccessfulBuild, BuildStateEnum.BEFORE_BUILD_FINISHED, extraParameters, WebHookPayloadDefaultTemplates.getDefaultEnabledPayloadTemplates());
		VariableMessageBuilder builder = variableResolverFactory.createVariableMessageBuilder("Sanitising ${sanitise(someTagThing)}", new WebHooksBeanUtilsVariableResolver(content.getProject(), new SimpleSerialiser(), content, allProperties, webHookSecretResolver));
		System.out.println(builder.build());
		builder = variableResolverFactory.createVariableMessageBuilder("Sanitizing ${sanitize(someTagThing)}", new WebHooksBeanUtilsVariableResolver(content.getProject(), new SimpleSerialiser(), content, allProperties, webHookSecretResolver));
		System.out.println(builder.build());
		System.out.println(content.getBuildFullName());
	}
	
	@Test
	public void TestResolvingParamtersFromTeamCityAndExtras(){
		WebHookPayloadContent content = new WebHookPayloadContent(variableResolverFactory, sBuildServer, sRunningBuild, previousSuccessfulBuild, BuildStateEnum.BEFORE_BUILD_FINISHED, extraParameters, WebHookPayloadDefaultTemplates.getDefaultEnabledPayloadTemplates());
		VariableMessageBuilder builder = variableResolverFactory.createVariableMessageBuilder("{ \"notifyType\": \"${notifyType}\",  "
				+ "\"buildId\": \"${buildId}\", "
				+ "\"buildTypeId\": \"${buildTypeId}\", "
				+ "\"buildStatus\": \"${buildStatus}\", "
				+ "\"config\": \"${config}\","
				+ "\"appVersion\": \"${builder.appVersion}\", "
				+ "\"sha\": \"${build.vcs.number}\" }", 
            new WebHooksBeanUtilsVariableResolver(content.getProject(), new SimpleSerialiser(), content, content.getAllParameters(), webHookSecretResolver));
		System.out.println(builder.build());
		assertEquals("{ \"notifyType\": \"beforeBuildFinish\",  \"buildId\": \"123456\", \"buildTypeId\": \"TestBuild\", \"buildStatus\": \"Running\", \"config\": \"This is some config thing\",\"appVersion\": \"This is the appVersion\", \"sha\": \"3b0a11eda029aaeb349993cb070a1c2e5987906c\" }", builder.build());
	}
	
	@Test
	public void TestResolvingParamtersFromTeamCityAndExtrasAndEscapeJson(){
		extraParameters.put("jsonString", " \" Some string that shouldn't be used\"");
		WebHookPayloadContent content = new WebHookPayloadContent(variableResolverFactory, sBuildServer, sRunningBuild, previousSuccessfulBuild, BuildStateEnum.BEFORE_BUILD_FINISHED, extraParameters, WebHookPayloadDefaultTemplates.getDefaultEnabledPayloadTemplates());
		VariableMessageBuilder builder = variableResolverFactory.createVariableMessageBuilder("{ "
				+ "\"myJson\": \"${escapejson(jsonString)}\" "
				+ "}", 
            new WebHooksBeanUtilsVariableResolver(content.getProject(), new SimpleSerialiser(), content, content.getAllParameters(), webHookSecretResolver));
		System.out.println(builder.build());
		assertEquals("{ \"myJson\": \" \\\" Some string that shouldn't be used\\\"\" }", builder.build());
	}
	
	@Test
	public void testSubString(){
		WebHookPayloadContent content = new WebHookPayloadContent(variableResolverFactory, sBuildServer, sRunningBuild, previousSuccessfulBuild, BuildStateEnum.BEFORE_BUILD_FINISHED, extraParameters, WebHookPayloadDefaultTemplates.getDefaultEnabledPayloadTemplates());
		VariableMessageBuilder builder = variableResolverFactory.createVariableMessageBuilder("build.vcs.number ${substr(build.vcs.number,0,7,32)}", new WebHooksBeanUtilsVariableResolver(content.getProject(), new SimpleSerialiser(), content, allProperties, webHookSecretResolver));
		assertEquals("build.vcs.number 3b0a11e", builder.build());
	}
	
	@Test
	public void testSubCapitilise(){
		WebHookPayloadContent content = new WebHookPayloadContent(variableResolverFactory, sBuildServer, sRunningBuild, previousSuccessfulBuild, BuildStateEnum.BEFORE_BUILD_FINISHED, extraParameters, WebHookPayloadDefaultTemplates.getDefaultEnabledPayloadTemplates());
		VariableMessageBuilder builder = variableResolverFactory.createVariableMessageBuilder("blah ${capitalise(lowercaseString)}", new WebHooksBeanUtilsVariableResolver(content.getProject(), new SimpleSerialiser(), content, allProperties, webHookSecretResolver));
		assertEquals("blah Yes, We Are All Lowercase", builder.build());
	}
	
	@Test
	public void testSubCapitilize(){
		WebHookPayloadContent content = new WebHookPayloadContent(variableResolverFactory, sBuildServer, sRunningBuild, previousSuccessfulBuild, BuildStateEnum.BEFORE_BUILD_FINISHED, extraParameters, WebHookPayloadDefaultTemplates.getDefaultEnabledPayloadTemplates());
		VariableMessageBuilder builder = variableResolverFactory.createVariableMessageBuilder("blah ${capitalize(lowercaseString)}", new WebHooksBeanUtilsVariableResolver(content.getProject(), new SimpleSerialiser(), content, allProperties, webHookSecretResolver));
		assertEquals("blah Yes, We Are All Lowercase", builder.build());
	}

}
