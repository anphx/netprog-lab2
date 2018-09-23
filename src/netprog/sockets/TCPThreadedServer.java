package netprog.sockets;

import netprog.logger.FileLogger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class TCPThreadedServer {
    private static int POOLSIZE = 5;

    public static void main(String[] args) {
        if (args.length < 1) {
            throw new IllegalArgumentException("Must specify a port!");
        }

        if (args.length != 2) {
            throw new IllegalArgumentException("Must specify a log level!");
        }

        FileLogger fileLog = new FileLogger("TCPThreadedServer", args[1]);
        try {
            fileLog.setup();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Logger logger = fileLog.logger();


        // TODO: specify number of threads as argument (optional)
        ExecutorService threadPool = Executors.newFixedThreadPool(POOLSIZE);

        try {
            int port = Integer.parseInt(args[0]);
            ServerSocket listener = new ServerSocket(port);

            for (; ; ) {
                logger.info("Server is running, waiting for client to connect");
                Socket s = listener.accept();
                ClientHandler obj_worker = new ClientHandler(s, args[1]);
                threadPool.execute(obj_worker);
            }

        } catch (IOException e) {
            logger.throwing("TCPThreadedServer", "main()", e);
            System.out.println("Fatal I/O Error !");
            System.exit(0);
        }
    }
}
