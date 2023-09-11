package com.vk.iso8583serverclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.jpos.iso.ISOBasePackager;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;
import org.jpos.iso.packager.ISO87APackager;

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
		ISOBasePackager packager =new ISO87APackager();
		
		try {

			while (processSocket.isConnected()) {
				String recvBytStr = inp.readLine();
				
				
				System.out.println("Recieved a message");
				
				
				//System.out.println("recieved txt : " + s);

				if (recvBytStr.equals("N")) {

					// closeAll(inp, outp, processSocket);
					break;
				}
				if (!recvBytStr.equals("N")) {
					
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
					
					//sending reply
					
					System.out.println("Sending response 810");
					ISOMsg m=new ISOMsg();
					m.setPackager(packager);
					
					m.set(0,"0810");
					m.set(3,"000000");
					m.set(41,"00000001");
					m.set(70,"301");
					
					byte[] pkdMsg=m.pack();
					
					System.out.println("Packed message is : "+ISOUtil.hexdump(pkdMsg));
				
					String nm=new String (pkdMsg);
					outp.println(nm);
					/*System.out.println(s + "  sent a name");
					outp.println("hello " + s);*/
					
					
				}
			}

		} catch (NullPointerException e) {
			e.getMessage();
			e.getCause();
		}

		catch (IOException e) {
			// TODO Auto-generated catch block

			e.getMessage();
		} catch (ISOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			closeAll(this.inp, this.outp, this.processSocket);
		}

	}
}
