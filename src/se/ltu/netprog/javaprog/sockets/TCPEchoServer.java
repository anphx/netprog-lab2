package se.ltu.netprog.javaprog.sockets;

//Requires a single command line arg - the port number
import netprog.helpers.StrHelper;

import java.net.*;	// need this for InetAddress, Socket, ServerSocket
import java.io.*;	// need this for I/O stuff
import java.util.Arrays;

public class TCPEchoServer {
	// define a constant used as size of buffer 
	static final int BUFSIZE=1024;
	// main starts things rolling
	static public void main(String args[]) { 
		
		if (args.length != 1){
			throw new IllegalArgumentException("Must specify a port!");
		}
		
		int port = Integer.parseInt(args[0]);
		try { 
			// Create Server Socket (passive socket) 
			ServerSocket ss = new ServerSocket(port);
			
			while (true) { 
				Socket s = ss.accept();
				handleClient(s);
			}
		} catch (IOException e) {
			System.out.println("Fatal I/O Error !"); 
			System.exit(0);
		}
		
	}
	
	// this method handles one client
	// declared as throwing IOException - this means it throws 
	// up to the calling method (who must handle it!)
	// try taking out the "throws IOException" and compiling,
	// the compiler will tell us we need to deal with this!
	
	static void handleClient(Socket s) throws IOException 
	{ 
		byte[] buff = new byte[BUFSIZE];
		int bytesread = 0;
		
		//print out client's address
		System.out.println("Connection from " + s.getInetAddress().getHostAddress());
		
		//Set up streams 
		InputStream in = s.getInputStream(); 
		OutputStream out = s.getOutputStream();
		
		//read/write loop 

//Modify your code here so that it sends back your name in addition to the echoed symbols

		while ((bytesread = in.read(buff)) != -1) {
			byte[] newBuff = new String(buff).replaceAll("[\\n\\r]", "").getBytes();
			byte[] myName = " AnPham\n".getBytes();


			/*
			AnP: concat buff and myName to a new byte array.
			write the final result to output stream
			to increase reusability, created concatBytes function in StrHelper class.
			*/
			out.write(StrHelper.concatBytes(newBuff, myName));

			// AnP: reset input buffer to avoid memorized string from last msg
			// this happens when the next string is shorter than the previous one
			// e.g: 1. 123456 => 123456
			// 		2. 123	  => 123456, because bytes are allocated and occupied by [1][2][3][4][5][6]
			Arrays.fill(buff, (byte)0);
		}
		
		System.out.println("Client has left\n"); 
		
		s.close();
		
	}
	
}