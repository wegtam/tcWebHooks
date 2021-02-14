package webhook.teamcity;

public interface WebHookTagsEventHandler {
    public void addTagsEvent(WebHookTagsEventHandlerImpl.TagWrapper tagWrapper);
}
