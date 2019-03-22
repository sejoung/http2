package kr.co.kilers.jetty.http2;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import org.eclipse.jetty.util.Utf8StringBuilder;

public class Http1_1_Upgrade {
	final static String host = "localhost";
	final static int port = 8080;

	public static void main(String[] args) throws Exception {
		try (Socket client = new Socket(host, port)) {
			OutputStream output = client.getOutputStream();
			output.write(("" + "GET /one HTTP/1.1\r\n" + "Host: localhost\r\n" + "Connection: Upgrade, HTTP2-Settings\r\n" + "Upgrade: h2c\r\n" + "HTTP2-Settings: \r\n" + "\r\n").getBytes(StandardCharsets.ISO_8859_1));
			output.flush();

			InputStream input = client.getInputStream();
			Utf8StringBuilder upgrade = new Utf8StringBuilder();
			int crlfs = 0;
			while (true) {
				int read = input.read();
				if (read == '\r' || read == '\n')
					++crlfs;
				else
					crlfs = 0;
				upgrade.append((byte) read);
				if (crlfs == 4)
					break;
			}

			System.out.println(upgrade);

			client.close();
		}

	}

}
