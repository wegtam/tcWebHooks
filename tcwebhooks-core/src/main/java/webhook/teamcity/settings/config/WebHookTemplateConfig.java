package webhook.teamcity.settings.config;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class WebHookTemplateConfig {

	String name;
	boolean enabled;
	int rank;
	WebHookTemplateText defaultTemplate;
	WebHookTemplateBranchText defaultBranchTemplate;
	String templateDescription;
	String templateToolTip;
	String preferredDateTimeFormat;
	List<WebHookTemplateFormat> formats = new ArrayList<>();
	WebHookTemplateItems templates = new WebHookTemplateItems();
	

	public WebHookTemplateConfig(String templateName, boolean templateEnabled) {
		this.name = templateName;
		this.enabled = templateEnabled;
	}

	@Data @AllArgsConstructor @NoArgsConstructor
	public static class WebHookTemplateText {
		boolean useTemplateTextForBranch;
		String templateContent;
		
		public WebHookTemplateText(String templateText) {
			this(false, templateText);
		}
	}
	
	@Data @AllArgsConstructor @NoArgsConstructor
	public static class WebHookTemplateBranchText {
		String templateContent;
	}

	@Data @AllArgsConstructor @NoArgsConstructor
	public static class WebHookTemplateItems {
		Integer maxId;
		List<WebHookTemplateItem> templates = new ArrayList<>();
	}
	
	@Data @AllArgsConstructor @NoArgsConstructor
	public static class WebHookTemplateFormat {
		String name;
		boolean enabled;
	}
	
	@Data @AllArgsConstructor @NoArgsConstructor @XmlRootElement
	public static class WebHookTemplateItem {
		WebHookTemplateText templateText;
		WebHookTemplateBranchText branchTemplateText;
		boolean enabled;
		Integer id;
		List<WebHookTemplateState> states = new ArrayList<>();
	}
	
	@Data @AllArgsConstructor @NoArgsConstructor
	public static class WebHookTemplateState {
		String type;
		boolean enabled;
	}
}