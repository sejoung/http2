package kr.co.kilers.jetty.http2;

import java.net.InetSocketAddress;

import org.eclipse.jetty.http.DateGenerator;
import org.eclipse.jetty.http.HostPortHttpField;
import org.eclipse.jetty.http.HttpField;
import org.eclipse.jetty.http.HttpFields;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.http.HttpScheme;
import org.eclipse.jetty.http.HttpVersion;
import org.eclipse.jetty.http.MetaData;
import org.eclipse.jetty.http.PreEncodedHttpField;
import org.eclipse.jetty.http2.api.Session;
import org.eclipse.jetty.http2.api.Stream;
import org.eclipse.jetty.http2.client.HTTP2Client;
import org.eclipse.jetty.http2.frames.HeadersFrame;
import org.eclipse.jetty.util.FuturePromise;
import org.eclipse.jetty.util.Promise;

public class MultiplexingAndDynamicTableTest {
	final static String host = "localhost";
	final static int port = 8080;
	final static String url = "/test";

	final static HttpField ServerJetty = new PreEncodedHttpField(HttpHeader.SERVER, "jetty");
	final static HttpField XPowerJetty = new PreEncodedHttpField(HttpHeader.X_POWERED_BY, "jetty");
	final static HttpField Date = new PreEncodedHttpField(HttpHeader.DATE, DateGenerator.formatDate(System.currentTimeMillis()));

	public static void main(String[] args) throws Exception {
			
		send();

	}
	
	public static void send() throws Exception {
		HTTP2Client lowLevelClient = new HTTP2Client();
		lowLevelClient.start();
		PrintingFramesHandler framesHandler = new PrintingFramesHandler();

		FuturePromise<Session> sessionFuture = new FuturePromise<>();
		lowLevelClient.connect(new InetSocketAddress(host, port), new Session.Listener.Adapter(), sessionFuture);
		Session session = sessionFuture.get();
		HttpFields fields0 = new HttpFields();
		fields0.add(HttpHeader.CONTENT_TYPE, "text/html");
		fields0.add(HttpHeader.CONTENT_LENGTH, "1024");
		fields0.add(ServerJetty);
		fields0.add(XPowerJetty);
		fields0.add(Date);
		fields0.add(HttpHeader.SET_COOKIE, "abcdefghijklmnopqrstuvwxyz");
		fields0.add("custom-key", "custom-vaasdasdasdasdaslue");
		fields0.add(HttpHeader.USER_AGENT, "Mozilla/5.0");

		HttpFields fields1 = new HttpFields();
		fields1.add(HttpHeader.CONTENT_TYPE, "text/htmlasdasd");
		fields1.add(Date);
		fields1.add(HttpHeader.SET_COOKIE, "abcdefghiasdasdasdjklmnopqrstuvwxyz");
		fields1.add("custom-key2", "custom-vaasdasdasasdasdasddasdaslue");
		fields1.add("custom-key2", "custom-vaaasdasdasdassdasdasdasdaslue");
		fields1.add(HttpHeader.USER_AGENT, "Mozilla/5.0");


		MetaData.Request metaData = new MetaData.Request("GET", HttpScheme.HTTP, new HostPortHttpField(host + ":" + port), url, HttpVersion.HTTP_2, fields0);
		HeadersFrame frame = new HeadersFrame(1, metaData, null, true);
		session.newStream(frame, new Promise.Adapter<Stream>(), framesHandler);
		session.newStream(frame, new Promise.Adapter<Stream>(), framesHandler);
		
		HeadersFrame frame1 = new HeadersFrame(1, metaData, null, true);
		
		session.newStream(frame1, new Promise.Adapter<Stream>(), framesHandler);
		session.newStream(frame1, new Promise.Adapter<Stream>(), framesHandler);

		framesHandler.getCompletedFuture().get();

		lowLevelClient.stop();
	}

}
