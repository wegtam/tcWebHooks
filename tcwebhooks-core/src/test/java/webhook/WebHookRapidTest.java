package webhook;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class WebHookRapidTest extends WebHookTestBase {

	@Test
	public void testRapid() throws InterruptedException {
		WebHookTestServer s = startWebServer();
		List<WebHook> webhooks = new ArrayList<>();
		int webhookAttempts = 100;
		for (int i = 0; i < webhookAttempts; i++) {
			webhooks.add(factory.getWebHook(url + "/200?count=" + i));
		}
		webhooks.parallelStream().forEach(w -> {
			try {
				w.addParam("buildID", "foobar");
				w.addParam("notifiedFor", "someUser");
				w.addParam("buildResult", "failed");
				w.addParam("triggeredBy", "Subversion");
				w.setEnabled(true);
				w.post();
			} catch (IOException e) {
				fail(e.getMessage());
			}
		});
		assertEquals(webhookAttempts, s.getRequestCount());
		stopWebServer(s);
	}

}
