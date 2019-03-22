package kr.co.kilers.jetty.http2;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.http.DateGenerator;
import org.eclipse.jetty.http.HttpField;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.http.HttpVersion;
import org.eclipse.jetty.http.PreEncodedHttpField;
import org.eclipse.jetty.http2.client.HTTP2Client;
import org.eclipse.jetty.http2.client.http.HttpClientTransportOverHTTP2;

public class TestClient {
	final static String host = "localhost";
	final static int port = 8080;
	final static String url = "/test";

	final static HttpField ServerJetty = new PreEncodedHttpField(HttpHeader.SERVER, "jetty");
	final static HttpField XPowerJetty = new PreEncodedHttpField(HttpHeader.X_POWERED_BY, "jetty");
	final static HttpField Date = new PreEncodedHttpField(HttpHeader.DATE, DateGenerator.formatDate(System.currentTimeMillis()));

	public static void main(String[] args) throws Exception {
		HTTP2Client lowLevelClient = new HTTP2Client();
		lowLevelClient.start();

		HttpClient client = new HttpClient(new HttpClientTransportOverHTTP2(lowLevelClient), null);

		client.start();

		Request request = client.POST("http://localhost:8080");
		request.header(HttpHeader.CONTENT_TYPE, "text/html");
		request.header(HttpHeader.SERVER, "jetty");
		request.header(HttpHeader.X_POWERED_BY, "jetty");
		request.header(HttpHeader.DATE, DateGenerator.formatDate(System.currentTimeMillis()));
		request.header(HttpHeader.SET_COOKIE, "abcdefghijklmnopqrstuvwxyz");
		request.header("custom-key", "custom-vaasdasdasdasdaslue");
		request.version(HttpVersion.HTTP_2);
		request.param("AUTH_ID", "test");
		request.param("AUTH_KEY", "test01");
		request.param("USERID", "test02");
		request.param("PASSWD", "test03");

		ContentResponse response = request.send();

		System.out.println("Version " + response.getVersion());
		System.out.println("Status " + response.getStatus());
		System.out.println("Headers \n" + response.getHeaders());
		System.out.println("MediaType " + response.getMediaType());
		System.out.println("Content " + response.getContentAsString());

		client.stop();

		lowLevelClient.stop();
		
		
		
		

	}

}
