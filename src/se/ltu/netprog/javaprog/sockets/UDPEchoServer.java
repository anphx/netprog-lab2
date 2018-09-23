package se.ltu.netprog.javaprog.sockets;


import netprog.helpers.StrHelper;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.charset.Charset;

public class UDPEchoServer {
    static final int BUFSIZE = 2048;

    static public void main(String args[]) throws SocketException {

        if (args.length != 1) {
            throw new IllegalArgumentException("Must specify a port!");
        }

        int port = Integer.parseInt(args[0]);
        DatagramSocket s = new DatagramSocket(port);
        DatagramPacket dp = new DatagramPacket(new byte[BUFSIZE], BUFSIZE);

        try {
            while (true) {
                s.receive(dp);
                // print out client's address
                System.out.println("Message from " + dp.getAddress().getHostAddress());

                // AnP: Append my name and send response back
                // Send it right back
                byte[] dpData = dp.getData();
                String newData = new String(dpData, Charset.forName("UTF-8")).trim().replaceAll("[\\n\\r]", "");
                byte newMsg[] = (newData + " AnPham!\n").getBytes();
                dp.setData(newMsg, 0, newMsg.length);
                s.send(dp);

                // AnP: reset buffer data and length datagrampacket to avoid overflow the buffer
                dp.setData(new byte[BUFSIZE]);
                dp.setLength(BUFSIZE);// avoid shrinking the packet buffer
            }
        } catch (IOException e) {
            System.out.println("Fatal I/O Error !");
            System.exit(0);
        }
    }
}