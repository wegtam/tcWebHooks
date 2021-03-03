package webhook.teamcity.executor;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.awaitility.Awaitility;
import org.jdom.JDOMException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import jetbrains.buildServer.serverSide.SBuild;
import jetbrains.buildServer.serverSide.SQueuedBuild;
import jetbrains.buildServer.serverSide.executors.ExecutorServices;
import webhook.WebHook;
import webhook.WebHookImpl;
import webhook.WebHookTestServer;
import webhook.WebHookTestServerTestBase;
import webhook.teamcity.BuildStateEnum;
import webhook.teamcity.WebHookContentBuilder;
import webhook.teamcity.WebHookFactory;
import webhook.teamcity.history.WebHookHistoryItemFactory;
import webhook.teamcity.history.WebHookHistoryRepository;
import webhook.teamcity.settings.WebHookConfig;
import webhook.testframework.util.ConfigLoaderUtil;

public class WebHookThreadingExecutorImplTest extends WebHookTestServerTestBase {
	
	WebHookExecutor executor;
	private WebHookTestServer s;
	
	@Mock
	WebHookFactory webhookFactory;
	
	@Mock
	WebHookContentBuilder webHookContentBuilder;
	
	@Mock
	WebHookHistoryRepository webHookHistoryRepository;
	
	@Mock
	WebHookHistoryItemFactory webHookHistoryItemFactory;
	
	@Mock
	SBuild sBuild;
	
	@Mock
	SQueuedBuild sQueueBuild;
	
	@Mock
	ExecutorServices executorServices;

	ScheduledExecutorService executorService = Executors.newScheduledThreadPool(100);
	
	@Before
	public void setup(){
		MockitoAnnotations.initMocks(this);
		s = startWebServer();
		Mockito.when(executorServices.getNormalExecutorService()).thenReturn(executorService);
		WebHookRunnerFactory webHookRunnerFactory = new WebHookRunnerFactory(webHookContentBuilder, webHookHistoryRepository, webHookHistoryItemFactory);
		executor = new WebHookThreadingExecutorImpl(webHookRunnerFactory, executorServices);
	}
	
	@After
	public void tearDown() throws InterruptedException {
		stopWebServer(s);
	}

	@Test
	public void testExecuteWebHookSQueuedBuild() throws JDOMException, IOException {
		
		PoolingHttpClientConnectionManager connPool = new PoolingHttpClientConnectionManager();
		
		WebHookConfig whc = ConfigLoaderUtil.getFirstWebHookInConfig(new File("src/test/resources/project-settings-test-all-states-enabled-localhost50039.xml"));
		HttpClient httpClient = HttpClients.custom()
	            .setConnectionManager(connPool) // shared connection manager
	            .setConnectionManagerShared(true).build();
		WebHook wh = new WebHookImpl(whc.getUrl(), null, httpClient);
		wh.setEnabled(true);
		
		Mockito.when(webhookFactory.getWebHook(whc, null)).thenReturn(wh);
		Mockito.when(webHookContentBuilder.buildWebHookContent(wh, whc, sQueueBuild, BuildStateEnum.BUILD_ADDED_TO_QUEUE, null, null, false)).thenReturn(wh);
		for (int i = 0; i < 100; i++) {
			executor.execute(wh, whc, sQueueBuild, BuildStateEnum.BUILD_ADDED_TO_QUEUE, null, null, false);
		}
		Awaitility.await().atMost(10, TimeUnit.SECONDS).untilAtomic(s.count, equalTo(100));
		assertEquals(100,s.getRequestCount());
	}

	@Override
	public String getHost() {
		return "localhost";
	}

	@Override
	public Integer getPort() {
		return 50039;
	}

}
