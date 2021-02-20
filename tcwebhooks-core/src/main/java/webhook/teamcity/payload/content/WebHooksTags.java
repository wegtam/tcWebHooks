package webhook.teamcity.payload.content;

import java.util.HashSet;
import java.util.Set;

import lombok.Getter;

@Getter
public class WebHooksTags {
	
	private Set<String> oldTags;
	private Set<String> newTags;
	private Set<String> removedTags;
	private Set<String> addedTags;
	
	public static WebHooksTags build(Set<String> oldTagset, Set<String> newTagset) {
		Set<String> oldCopy = new HashSet<>(oldTagset); 
		Set<String> newCopy = new HashSet<>(newTagset); 
		
		WebHooksTags tags = new WebHooksTags();
		tags.oldTags = oldTagset;
		tags.newTags = newTagset;
		
		oldCopy.removeAll(newTagset); // Should now contain only removed tags
		newCopy.removeAll(oldTagset); // Should now contain only added tags
		
		tags.removedTags = oldCopy;
		tags.addedTags = newCopy;
		return tags;
	}
	

}
