package com.vk.iso8583serverclient;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;





public class Server {
	
	private ServerSocket svrskt;

	public Server(ServerSocket s) {
		this.svrskt = s;
	}
	
	
	
	
	public void startServer() {
		try {

			while (!svrskt.isClosed()) {
				
				Socket s = svrskt.accept();
				System.out.println("client connected");
				ClintProcess cp = new ClintProcess(s);

				Thread process = new Thread(cp);
				process.start();
			}
		} catch (IOException e) {
			this.closerSrvrSocket();
		}

	}
	
	public void closerSrvrSocket() {
		try {
			this.svrskt.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public static void main(String[] args) throws IOException {
		System.out.println("server started");
		ServerSocket s=new ServerSocket(5000);
		Server sr=new Server(s);
		sr.startServer();
	}
}
