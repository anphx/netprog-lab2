package netprog.sockets;

import netprog.functions.CmdExecutor;
import netprog.logger.FileLogger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.logging.Logger;

public class ClientHandler implements Runnable {
    private Socket socket;
    private Logger LOGGER;
    private static int BUFSIZE = 2048;

    public ClientHandler(Socket s, String logLevel) {
        socket = s;
        LOGGER = new FileLogger("TCPThreadedServer", logLevel).logger();
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
        this.LOGGER.info("Connection from " + clientAddr);
        System.out.println("Connection from " + clientAddr);

        // AnP: Set up streams
        InputStream in = s.getInputStream();
        OutputStream out = s.getOutputStream();
        // AnP: read cmd input from client
        while (in.read(buff) != -1) {
            // AnP: execute command using CmdExecutor
            CmdExecutor executor = new CmdExecutor(new String(buff, Charset.forName("UTF-8")).trim());
            byte[] cmdOutput = executor.executeCmd().getBytes();

            // AnP: send back result to outputStream
            out.write(cmdOutput,0, cmdOutput.length);
        }

        // AnP: close connection when client disconnected
        System.out.println("Client has left\n");
        this.LOGGER.severe("Client has left\n");
        this.LOGGER.severe("Connection closed!\n");

        s.close();
    }
}
