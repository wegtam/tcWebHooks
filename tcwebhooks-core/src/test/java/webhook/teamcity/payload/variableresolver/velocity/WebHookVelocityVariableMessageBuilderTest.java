package webhook.teamcity.payload.variableresolver.velocity;

import static org.junit.Assert.*;

import org.apache.velocity.context.Context;
import org.junit.Test;

import lombok.AllArgsConstructor;
import lombok.Data;
import webhook.teamcity.payload.content.ExtraParameters;
import webhook.teamcity.payload.content.WebHookPayloadContent;

public class WebHookVelocityVariableMessageBuilderTest {

	@Test
	public void testBuild() {
		ExtraParameters extraParameters = new ExtraParameters();
		extraParameters.put("myString", "${buildId} is in project ${projectId}");
		Context resolver = new WebHooksBeanUtilsVelocityVariableResolver(
				null, 
				new WebHookPayloadContent.SimpleSerialiser(),
				new JavaBean("bt01", "project01"),
				extraParameters,
				null
			);
		WebHookVelocityVariableMessageBuilder builder = WebHookVelocityVariableMessageBuilder.create(resolver, null);
		
		assertEquals("bt01", builder.build("${buildId}"));
		assertEquals("project01", builder.build("${projectId}"));
		assertEquals("bt01 is in project project01", builder.build("${myString}"));
		
	}

	@Data @AllArgsConstructor
	public class JavaBean {
		
		private String buildId;
		private String projectId;
	}
}
