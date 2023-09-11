package com.vk.iso8583serverclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import org.jpos.iso.ISOBasePackager;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;
import org.jpos.iso.packager.ISO87APackager;


public class Client {
	
	Socket processSocket;
	BufferedReader inp;
	PrintWriter outp;

	Client(Socket s) throws IOException {
		this.processSocket = s;
		this.inp = new BufferedReader(new InputStreamReader(processSocket.getInputStream()));
		this.outp = new PrintWriter(processSocket.getOutputStream(), true);
	}
	
	
	
	
void sendReq() throws IOException, ISOException {
	ISOBasePackager packager =new ISO87APackager();
	
		Scanner in=new Scanner(System.in);
		String nm,cardnumber,amount,traceno,expdate,tid,mid;
		while(this.processSocket.isConnected()) {
			System.out.println("Sending Iso8583 Echo message");
			
			System.out.println("Want to proceed - type \'y\' to proceed \'N\' to quit : ");
			nm=in.nextLine();
			if(nm.equals("N")) {
				this.closeAll(this.inp, this.outp, this.processSocket);
				break;
			}
			
			ISOMsg m=new ISOMsg();
			m.setPackager(packager);
			
			m.set(0,"0800");
			m.set(3,"000000");
			m.set(41,"00000001");
			m.set(70,"301");
			
			byte[] pkdMsg=m.pack();
			
			System.out.println("Packed message is : "+ISOUtil.hexdump(pkdMsg));
		
			nm=new String (pkdMsg);
			this.outp.println(nm);
			
			
			String recvBytStr=this.inp.readLine();
			System.out.println("Recieved a message");
			
			byte[]recvBytArr=recvBytStr.getBytes();
			System.out.println("Recieved message is : "+ISOUtil.hexdump(recvBytArr));
			
			ISOMsg recvm=new ISOMsg();
			recvm.setPackager(packager);
			recvm.unpack(recvBytArr);
			//print each field of recieved
			for (int i = 1; i <= recvm.getMaxField(); i++) {
                if (recvm.hasField(i)) {
                    System.out.println("Field " + i + ": " + recvm.getString(i));
                }
            }
			
			
			System.out.println();
		
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
		} catch (ISOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
}
