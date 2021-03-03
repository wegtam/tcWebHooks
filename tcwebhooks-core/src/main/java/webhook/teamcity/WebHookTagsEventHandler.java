package webhook.teamcity;


/** 
 * When TeamCity changes tags it issues two events. The first with the original tags, and an empty "new" list.
 * Then the second event has an empty "old" list and the "new" tags in the new list.
 * 
 * Therefore we need a special handler that can wait for both events, and then produce the correct difference
 * between tags.
 */
public interface WebHookTagsEventHandler {
    public void addTagsEvent(WebHookTagsEventHandlerImpl.TagWrapper tagWrapper);
}
