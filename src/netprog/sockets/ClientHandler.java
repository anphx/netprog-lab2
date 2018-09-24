package netprog.sockets;

import netprog.functions.CmdExecutor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.logging.Logger;

public class ClientHandler implements Runnable {
    private Socket socket;
    private Logger LOGGER;
    private static int BUFSIZE = 2048;

    public ClientHandler(Socket s, Logger logger) {
        socket = s;
        LOGGER = logger;
    }

    @Override
    public void run() {
        try {
            LOGGER.info("Client thread started!");
            handleClient(socket);
        } catch (IOException e) {
            LOGGER.throwing("ClientHandler", "handleClient(socket)", e);
            e.printStackTrace();
        }
    }

    private void handleClient(Socket s) throws IOException {
        // AnP: handle request from client
        byte[] buff = new byte[BUFSIZE];

        // AnP: print out client's address
        String clientAddr = s.getInetAddress().getHostAddress();
        LOGGER.info("Connection from " + clientAddr + " on port: " + s.getPort());

        // AnP: Set up streams
        InputStream in = s.getInputStream();
        OutputStream out = s.getOutputStream();

        // AnP: read cmd input from client
        while (in.read(buff) != -1) {
            // AnP: execute command using CmdExecutor
            String cmd = new String(buff, Charset.forName("UTF-8")).trim();
            LOGGER.info("Command request from " + clientAddr + ": " + cmd);

            CmdExecutor executor = new CmdExecutor(cmd);
//            byte[] cmdOutput = executor.executeCmd().getBytes();

            // AnP: send back result to outputStream
            out.write(executor.executeCmd().getBytes());

            // AnP: Clean up buffer reader to avoid memorized string from last msg
            // this happens when the next string is shorter than the previous one
            // e.g: 1. 123456 => 123456
            // 		2. 123	  => 123456, because bytes are allocated and occupied by [1][2][3][4][5][6]
            Arrays.fill(buff, (byte)0);
            out.flush();
        }

        // AnP: close connection when client disconnected
//        System.out.println("Client has left\n");
        LOGGER.info("Client connected on port: " + s.getPort() + " has left\n");
        LOGGER.info("Connection closed!\n");
        s.close();
    }
}
