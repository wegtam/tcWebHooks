package webhook.teamcity;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

public class WebHookHttpClientFactoryImpl implements WebHookHttpClientFactory {
	
	private static final PoolingHttpClientConnectionManager connPool = new PoolingHttpClientConnectionManager();
	
	@Override
	public HttpClient getHttpClient(){
		return HttpClients.custom()
	            .setConnectionManager(connPool) // shared connection manager
	            .setConnectionManagerShared(true).build();
	}

}
