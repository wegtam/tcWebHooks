package webhook.teamcity.server.rest.model.parameter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import jetbrains.buildServer.server.rest.model.Fields;
import jetbrains.buildServer.server.rest.util.ValueWithDefault;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import webhook.teamcity.server.rest.util.BeanContext;
import webhook.teamcity.settings.project.WebHookParameter;

@Getter @Setter @NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="projectParameter")
@XmlType( propOrder = { "id", "name", "value", "secure", "includedInLegacyPayloads", "href" })
public class ProjectWebhookParameter implements WebHookParameter {
	
	@XmlAttribute
	private String id;

	@XmlElement
	private String name;
	
	@XmlElement
	private String value;
	
	@XmlAttribute
	private Boolean secure;
	
	@XmlAttribute
	private Boolean includedInLegacyPayloads;
	
	@XmlAttribute
	private String href;
	
	public ProjectWebhookParameter(WebHookParameter parameter, String projectExternalId, Fields fields, BeanContext beanContext) {
		this.id = ValueWithDefault.decideDefault(fields.isIncluded("id", true, true), parameter.getId());
		this.name = ValueWithDefault.decideDefault(fields.isIncluded("name", false, true), parameter.getName());
		this.value = ValueWithDefault.decideDefault(fields.isIncluded("value", false, true), parameter.getValue());
		this.secure = ValueWithDefault.decideDefault(fields.isIncluded("secure", false, true), parameter.getSecure());
		this.includedInLegacyPayloads = ValueWithDefault.decideDefault(fields.isIncluded("includedInLegacyPayloads", false, true), parameter.getIncludedInLegacyPayloads());
		href = ValueWithDefault.decideDefault(fields.isIncluded("href"), beanContext.getApiUrlBuilder().getHref(projectExternalId, parameter));

	}

}
