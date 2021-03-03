package webhook;

import org.springframework.util.SocketUtils;


public abstract class WebHookTestBase{
	public String proxy = "127.0.0.1";
	public Integer proxyPort = SocketUtils.findAvailableTcpPort();
	String proxyPortString = String.valueOf(proxyPort);
	public Integer webserverPort = SocketUtils.findAvailableTcpPort();
	public Integer proxyserverPort = proxyPort;
	public String webserverHost = "127.0.0.1";
	String url = "http://"  + webserverHost + ":" + webserverPort;
	
	public String proxyUsername = "foo";
	public String proxyPassword = "bar";
	
	protected TestingWebHookFactory factory = new TestingWebHookFactory();
	
	
	public WebHookTestServer startWebServer(){
		try {
			WebHookTestServer s = new WebHookTestServer(webserverHost, webserverPort);
			s.server.start();
			return s;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void stopWebServer(WebHookTestServer s) throws InterruptedException {
		try {
			s.server.stop();
			s.count.set(0);
			// Sleep to let the server shutdown cleanly.
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Thread.sleep(100);
		}
	}

	public WebHookTestProxyServer startProxyServer(){
		try {
			WebHookTestProxyServer p = new WebHookTestProxyServer(webserverHost, proxyserverPort);
			p.server.start();
			return p;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public WebHookTestProxyServer startProxyServerAuth(String username, String password){
		try {
			WebHookTestProxyServer p = new WebHookTestProxyServer(webserverHost, proxyserverPort, 
					username, password);
			p.server.start();
			return p;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void stopProxyServer(WebHookTestProxyServer p) {
		try {
			p.server.stop();
			// Sleep to let the server shutdown cleanly.
			Thread.sleep(100);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
}
