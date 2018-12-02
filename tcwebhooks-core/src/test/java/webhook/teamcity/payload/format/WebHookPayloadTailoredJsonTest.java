package webhook.teamcity.payload.format;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.TreeMap;

import jetbrains.buildServer.serverSide.SBuildServer;
import jetbrains.buildServer.serverSide.SFinishedBuild;
import jetbrains.buildServer.serverSide.SRunningBuild;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import webhook.teamcity.BuildStateEnum;
import webhook.teamcity.payload.PayloadTemplateEngineType;
import webhook.teamcity.payload.WebHookPayloadDefaultTemplates;
import webhook.teamcity.payload.WebHookPayloadManager;
import webhook.teamcity.payload.content.ExtraParametersMap;
import webhook.teamcity.payload.content.WebHookPayloadContent;
import webhook.teamcity.payload.content.WebHookPayloadContentAssemblyException;
import webhook.teamcity.payload.variableresolver.VariableResolverFactory;
import webhook.teamcity.payload.variableresolver.WebHookVariableResolverManager;
import webhook.teamcity.payload.variableresolver.WebHookVariableResolverManagerImpl;
import webhook.teamcity.payload.variableresolver.WebHooksBeanUtilsVariableResolverFactory;
import webhook.testframework.WebHookMockingFramework;
import webhook.testframework.WebHookMockingFrameworkImpl;

public class WebHookPayloadTailoredJsonTest {
	
	@Mock SBuildServer server;
	@Mock SRunningBuild buildType;
	@Mock SFinishedBuild sFinishedBuild;
	
	WebHookVariableResolverManager variableResolverManager = new WebHookVariableResolverManagerImpl();
	
	@Before
	public void setup() {
		variableResolverManager.registerVariableResolverFactory(new WebHooksBeanUtilsVariableResolverFactory());
	}

	@Test
	public void testRegister() {
		WebHookPayloadManager wpm = new WebHookPayloadManager(server);
		WebHookPayloadTailoredJson whp = new WebHookPayloadTailoredJson(wpm, variableResolverManager);
		whp.register();
		assertEquals(whp, wpm.getFormat(whp.getFormatShortName()));
	}

	@Test
	public void testGetContentType() {
		WebHookPayloadTailoredJson whp = new WebHookPayloadTailoredJson(null, variableResolverManager);
		assertEquals(whp.getContentType().toString(), "application/json");

	}

	@Test
	public void testGetRank() {
		WebHookPayloadTailoredJson whp = new WebHookPayloadTailoredJson(null, variableResolverManager);
		assertEquals(101, whp.getRank().intValue());
	}

	@Test
	public void testSetRank() {
		WebHookPayloadTailoredJson whp = new WebHookPayloadTailoredJson(null, variableResolverManager);
		whp.setRank(10);
		assertTrue(whp.getRank() == 10);
	}

	@Test
	public void testGetCharset() {
		WebHookPayloadTailoredJson whp = new WebHookPayloadTailoredJson(null, variableResolverManager);
		assertTrue(whp.getCharset().equals("UTF-8".toString()));
	}

	@Test
	public void testGetFormatDescription() {
		WebHookPayloadTailoredJson whp = new WebHookPayloadTailoredJson(null, variableResolverManager);
		assertEquals("Tailored JSON in body", whp.getFormatDescription());
	}

	@Test
	public void testGetFormatShortName() {
		WebHookPayloadTailoredJson whp = new WebHookPayloadTailoredJson(null, variableResolverManager);
		assertEquals("tailoredjson", whp.getFormatShortName());
	}

	@Test
	public void testGetFormatToolTipText() {
		WebHookPayloadTailoredJson whp = new WebHookPayloadTailoredJson(null, variableResolverManager);
		assertEquals("Send a JSON payload with content specified by parameter named 'body'", whp.getFormatToolTipText());
	}
	
	@Test (expected=WebHookPayloadContentAssemblyException.class)
	public void testForNullPointerWithoutBody() throws WebHookPayloadContentAssemblyException{
		
		WebHookPayloadTailoredJson whp = new WebHookPayloadTailoredJson(null, variableResolverManager);
		
		ExtraParametersMap extraParameters = new ExtraParametersMap(new TreeMap<String,String>());
		ExtraParametersMap teamcityProperties = new ExtraParametersMap(new TreeMap<String,String>());
		ExtraParametersMap templates = new ExtraParametersMap(new TreeMap<String,String>());
		templates.put(WebHookPayloadDefaultTemplates.HTML_BUILDSTATUS_TEMPLATE, "test template");
		
		WebHookMockingFramework framework = WebHookMockingFrameworkImpl.create(BuildStateEnum.BUILD_FINISHED, extraParameters, teamcityProperties);
		VariableResolverFactory variableResolverFactory = variableResolverManager.getVariableResolverFactory(PayloadTemplateEngineType.STANDARD);

		WebHookPayloadContent content = new WebHookPayloadContent(variableResolverFactory, framework.getServer(), framework.getRunningBuild(), sFinishedBuild, BuildStateEnum.BUILD_FINISHED, extraParameters, teamcityProperties, templates);
		whp.getStatusAsString(content, null);
		
	}
	
	@Test
	public void testForNotNullPointerWithBody() throws WebHookPayloadContentAssemblyException{
		
		WebHookPayloadTailoredJson whp = new WebHookPayloadTailoredJson(null, variableResolverManager);
		
		ExtraParametersMap extraParameters = new ExtraParametersMap(new TreeMap<String,String>());
		extraParameters.put("body", "{ \"someBody\" : \"This is a body for project ${projectName} \"}");
		
		ExtraParametersMap teamcityProperties = new ExtraParametersMap(new TreeMap<String,String>());
		ExtraParametersMap templates = new ExtraParametersMap(new TreeMap<String,String>());
		templates.put(WebHookPayloadDefaultTemplates.HTML_BUILDSTATUS_TEMPLATE, "test template");
		
		WebHookMockingFramework framework = WebHookMockingFrameworkImpl.create(BuildStateEnum.BUILD_FINISHED, extraParameters, teamcityProperties);
		VariableResolverFactory variableResolverFactory = variableResolverManager.getVariableResolverFactory(PayloadTemplateEngineType.STANDARD);

		
		WebHookPayloadContent content = new WebHookPayloadContent(variableResolverFactory, framework.getServer(), framework.getRunningBuild(), sFinishedBuild, BuildStateEnum.BUILD_FINISHED, extraParameters, teamcityProperties, templates);
		whp.getStatusAsString(content, null);
		assertEquals("{ \"someBody\" : \"This is a body for project Test Project \"}", whp.getStatusAsString(content, null));
		
	}
}
