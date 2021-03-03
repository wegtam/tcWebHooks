package webhook.teamcity;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import jetbrains.buildServer.serverSide.BuildPromotion;
import jetbrains.buildServer.serverSide.TagData;
import webhook.teamcity.payload.content.WebHooksTags;

@RunWith(MockitoJUnitRunner.class)
public class WebHookTagsEventHandlerImplTest {

    
    @Mock
    DeferrableServiceManager deferrableServiceManager;
    
    @Mock
    WebHookListener webHookListener;
    
    @InjectMocks
    WebHookTagsEventHandlerImpl webHookTagsEventHandler;

    
    @Test
    public void addTagsEvent() throws InterruptedException {
        
        webHookTagsEventHandler.setWebHookListener(webHookListener);
        List<TagData> firstTags = Arrays.asList(
                TagData.createPublicTag("prod")
        );

        List<TagData> secondTags = Arrays.asList(
                TagData.createPublicTag("prod"),
                TagData.createPublicTag("release")
        );
        BuildPromotion buildPromotion = mock(BuildPromotion.class);
        WebHookTagsEventHandlerImpl.TagWrapper wrapper01 = new WebHookTagsEventHandlerImpl.TagWrapper(
                Instant.now(),
                buildPromotion,
                null,
                firstTags,
                Collections.emptyList()
        );
        
        WebHookTagsEventHandlerImpl.TagWrapper wrapper02 = new WebHookTagsEventHandlerImpl.TagWrapper(
                Instant.now(),
                buildPromotion,
                null,
                Collections.emptyList(),
                secondTags
        );
        
        webHookTagsEventHandler.register();
        Thread.sleep(100);
        webHookTagsEventHandler.addTagsEvent(wrapper01);
        Thread.sleep(100);
        webHookTagsEventHandler.addTagsEvent(wrapper02);

        Mockito.verify(webHookListener, Mockito.timeout(10000).times(1))
                .processBuildTagsChanged(
                        ArgumentMatchers.eq(buildPromotion),
                        ArgumentMatchers.eq(null),
                        ArgumentMatchers.eq(firstTags),
                        ArgumentMatchers.eq(secondTags)
                        );
    }
    
    @Test
    public void addRemoveTagsEvent() throws InterruptedException {
    	
    	webHookTagsEventHandler.setWebHookListener(webHookListener);
    	List<TagData> firstTags = Arrays.asList(
    			TagData.createPublicTag("prod"),
    			TagData.createPublicTag("release")
    			);
    	
    	List<TagData> secondTags = Arrays.asList(
    			TagData.createPublicTag("prod")
    			);
    	BuildPromotion buildPromotion = mock(BuildPromotion.class);
    	WebHookTagsEventHandlerImpl.TagWrapper wrapper01 = new WebHookTagsEventHandlerImpl.TagWrapper(
    			Instant.now(),
    			buildPromotion,
    			null,
    			firstTags,
    			Collections.emptyList()
    			);
    	
    	WebHookTagsEventHandlerImpl.TagWrapper wrapper02 = new WebHookTagsEventHandlerImpl.TagWrapper(
    			Instant.now(),
    			buildPromotion,
    			null,
    			Collections.emptyList(),
    			secondTags
    			);
    	
    	webHookTagsEventHandler.register();
    	Thread.sleep(100);
    	webHookTagsEventHandler.addTagsEvent(wrapper01);
    	Thread.sleep(100);
    	webHookTagsEventHandler.addTagsEvent(wrapper02);
    	
    	Mockito.verify(webHookListener, Mockito.timeout(10000).times(1))
    	.processBuildTagsChanged(
    			ArgumentMatchers.eq(buildPromotion),
    			ArgumentMatchers.eq(null),
    			ArgumentMatchers.eq(firstTags),
    			ArgumentMatchers.eq(secondTags)
    			);
    }
    
    @Test
    public void testAddingBuildTags() {
    	List<TagData> firstTags = Arrays.asList(
    			TagData.createPublicTag("prod"),
    			TagData.createPublicTag("release")
    			);
    	
    	List<TagData> secondTags = Arrays.asList(
    			TagData.createPublicTag("prod")
    			);
    	WebHooksTags tags = WebHookListener.buildTags(firstTags, secondTags);
    	assertEquals(buildSet("prod", "release"), tags.getOldTags());
    	assertEquals(buildSet("prod"), tags.getNewTags());
    	assertEquals(buildSet("release"), tags.getRemovedTags());
    	assertEquals(buildSet(), tags.getAddedTags());
    }
    
    @Test
    public void testRemovingBuildTags() {
    	List<TagData> firstTags = Arrays.asList(
    			TagData.createPublicTag("prod"),
    			TagData.createPublicTag("release")
    			);
    	
    	List<TagData> secondTags = Arrays.asList(
    			TagData.createPublicTag("prod"),
    			TagData.createPublicTag("dev")
    			);
    	WebHooksTags tags = WebHookListener.buildTags(firstTags, secondTags);
    	assertEquals(buildSet("prod", "release"), tags.getOldTags());
    	assertEquals(buildSet("prod", "dev"), tags.getNewTags());
    	assertEquals(buildSet("release"), tags.getRemovedTags());
    	assertEquals(buildSet("dev"), tags.getAddedTags());
    }

	private Object buildSet(String... strings) {
		Set<String> set = new HashSet<>();
		Arrays.asList(strings).stream().forEach(s -> set.add(s));
		return set;
	}
}