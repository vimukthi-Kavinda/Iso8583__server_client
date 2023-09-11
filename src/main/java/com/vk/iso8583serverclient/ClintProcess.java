package com.vk.iso8583serverclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Hello world!
 *
 */
public class ClintProcess implements Runnable {

	Socket processSocket;
	BufferedReader inp;
	PrintWriter outp;

	ClintProcess(Socket s) throws IOException {
		this.processSocket = s;
		this.inp = new BufferedReader(new InputStreamReader(processSocket.getInputStream()));
		this.outp = new PrintWriter(processSocket.getOutputStream(), true);
	}

	void closeAll(BufferedReader b, PrintWriter p, Socket s) {

		try {
			if (b != null) {
				b.close();
			}
			if (p != null) {
				p.close();
			}
			if (s != null) {
				s.close();
			}
		} catch (IOException e) {
			e.getMessage();
		}
	}

	@Override
	public void run() {
		try {

			while (processSocket.isConnected()) {
				String s = inp.readLine();

				System.out.println("recieved txt : " + s);

				if (s.equals("exit")) {

					// closeAll(inp, outp, processSocket);
					break;
				}
				if (!s.equals("exit")) {
					System.out.println(s + "  sent a name");
					outp.println("hello " + s);
				}
			}

		} catch (NullPointerException e) {
			e.getMessage();
			e.getCause();
		}

		catch (IOException e) {
			// TODO Auto-generated catch block

			e.getMessage();
		} finally {
			closeAll(this.inp, this.outp, this.processSocket);
		}

	}
}
