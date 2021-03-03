package webhook.teamcity;

import java.io.IOException;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

@SuppressWarnings("deprecation")
public class TestingWebHookHttpClientFactoryImpl implements WebHookHttpClientFactory {
	
	
	TestableHttpClient httpClient;
	
	public TestingWebHookHttpClientFactoryImpl() {
		this.httpClient = new TestableHttpClient();
	}
	public TestingWebHookHttpClientFactoryImpl(TestableHttpClient client) {
		this.httpClient = client;
	}
	
	@Override
	public HttpClient getHttpClient(){
		return httpClient;
	}
	
	public interface InvocationCountable {
		public abstract int getInvocationCount();
	}


	public static class TestableHttpClient implements InvocationCountable, HttpClient {
		
		HttpClient httpClient = HttpClients.createDefault();
		
		public int invocationCount = 0;
		

		@Override
		public int getInvocationCount() {
			return invocationCount;
		}


		@Override
		public HttpParams getParams() {
			return httpClient.getParams();
		}


		@Override
		public ClientConnectionManager getConnectionManager() {
			return httpClient.getConnectionManager();
		}


		@Override
		public HttpResponse execute(HttpUriRequest request) throws IOException, ClientProtocolException {
			this.invocationCount++;
			return httpClient.execute(request);
		}


		@Override
		public HttpResponse execute(HttpUriRequest request, HttpContext context) throws IOException, ClientProtocolException {
			this.invocationCount++;
			return httpClient.execute(request, context);
		}


		@Override
		public HttpResponse execute(HttpHost target, HttpRequest request) throws IOException, ClientProtocolException {
			this.invocationCount++;
			return httpClient.execute(target, request);
		}


		@Override
		public HttpResponse execute(HttpHost target, HttpRequest request, HttpContext context)
				throws IOException, ClientProtocolException {
			this.invocationCount++;
			return httpClient.execute(target, request, context);
		}


		@Override
		public <T> T execute(HttpUriRequest request, ResponseHandler<? extends T> responseHandler)
				throws IOException, ClientProtocolException {
			this.invocationCount++;
			return httpClient.execute(request, responseHandler);
		}


		@Override
		public <T> T execute(HttpUriRequest request, ResponseHandler<? extends T> responseHandler, HttpContext context)
				throws IOException, ClientProtocolException {
			this.invocationCount++;
			return httpClient.execute(request, responseHandler, context);
		}


		@Override
		public <T> T execute(HttpHost target, HttpRequest request, ResponseHandler<? extends T> responseHandler)
				throws IOException, ClientProtocolException {
			this.invocationCount++;
			return httpClient.execute(target, request, responseHandler);
		}


		@Override
		public <T> T execute(HttpHost target, HttpRequest request, ResponseHandler<? extends T> responseHandler, HttpContext context) throws IOException, ClientProtocolException {
			this.invocationCount++;
			return httpClient.execute(target, request, responseHandler, context);
		}


	}
}
