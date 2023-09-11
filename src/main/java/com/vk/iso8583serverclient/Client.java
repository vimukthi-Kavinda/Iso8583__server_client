package com.vk.iso8583serverclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;


public class Client {
	
	Socket processSocket;
	BufferedReader inp;
	PrintWriter outp;

	Client(Socket s) throws IOException {
		this.processSocket = s;
		this.inp = new BufferedReader(new InputStreamReader(processSocket.getInputStream()));
		this.outp = new PrintWriter(processSocket.getOutputStream(), true);
	}
	
	
	
	
void sendReq() throws IOException {
		
		Scanner in=new Scanner(System.in);
		String nm;
		while(this.processSocket.isConnected()) {
			System.out.println("input name - exit to quit : ");
			nm=in.nextLine();
			if(nm.equals("exit")) {
				this.closeAll(this.inp, this.outp, this.processSocket);
				break;
			}
			this.outp.println(nm);
			System.out.println(this.inp.readLine());
		
		}
	}
	
	void closeAll(BufferedReader b, PrintWriter p, Socket s) {

		try {
			p.write("client left..");
			
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
	
	

	public static void main(String[] args) {
		try {
			Socket s=new Socket("localhost",5000);
			Client c=new Client(s);
			c.sendReq();
			
			c.closeAll(c.inp, c.outp, c.processSocket);
		} catch (UnknownHostException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
}
