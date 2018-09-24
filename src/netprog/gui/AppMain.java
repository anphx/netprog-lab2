package netprog.gui;

import netprog.functions.CmdExecutor;
import netprog.functions.CmdRemoteHandler;
import netprog.functions.Fibonacci;

import javax.swing.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppMain {
    private JPanel mainPanel;
    private JTextArea txtOut;
    private JTextField txtIn;
    private JButton btnFibonacci;
    private JTextField txtServerAddr;
    private JTextField txtServerPort;
    private JButton btnServerExec;
    private JButton btnClearConsole;
    private JButton btnDisconn;
    private JButton btnLocalExec;

    private Socket sock;
    private int seed = 0;

    public AppMain(String[] args) {
        this.seed = Integer.parseInt(args[0]);

        btnLocalExec.addActionListener(e -> {
            // Read command entered from the input, execute and show results in the output
            String input = txtIn.getText();

            // This is to print the input command to output field
            printToOutput("\n>>" + input + "...\n");

            String out = "";
            if (input.length() >= 1) {
//                out = CmdExecutor.executeCmd(input);

                //AnP: This is to execute command in a separate thread
                CmdExecutor executor = new CmdExecutor(input);
                new Thread(executor).start();
                out = executor.executeCmd();
            }
            printToOutput(out);
        });

        btnFibonacci.addActionListener(e -> new Thread(new Fibonacci(seed, this)).start());
        btnServerExec.addActionListener(e -> {
            String serverAddr = txtServerAddr.getText();
            String serverPort = txtServerPort.getText();
            String cmd = txtIn.getText();

            if (serverAddr.length() == 0 || serverPort.length() == 0 || cmd.length() == 0) {
                printToOutput("Parameters are missing!\n");
                return;
            }

            sendCmdToServer(serverAddr, serverPort, cmd);
        });
        btnClearConsole.addActionListener(e -> {
            txtOut.setText("");
        });

        btnDisconn.addActionListener(e -> {
            try {
                sock.close();
                sock = null;
                printToOutput("==================== Server disconnected!!!====================\n\n");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException("Must specify an iteration number!");
        }

        if (args[0] .length() <= 0) {
            throw new IllegalArgumentException("Iteration number should be an integer >=1 ");
        }

        bindFrame(args);
    }

    static void bindFrame(String[] args) {
        JFrame frame = new JFrame("a.p. Commander GUI");
        frame.setContentPane(new AppMain(args).mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public void printToOutput(String text) {
        txtOut.append(text);
    }

    void sendCmdToServer(String serverAddr, String serverPort, String cmd) {
        int port = Integer.parseInt(serverPort);

        // TODO: specify number of threads as argument (optional)
        ExecutorService threadPool = Executors.newFixedThreadPool(5);

        if (serverAddr.length() != 0 && port != 0 && cmd.length() != 0) {
            try {
                if (sock == null) {
                    sock = new Socket(serverAddr, port);
                }
                BufferedInputStream in = new BufferedInputStream(sock.getInputStream());
                OutputStream out = sock.getOutputStream();

                // AnP: Send command to server through output stream
                out.write(cmd.getBytes());

                // AnP: Listen on the input stream for server response
                // Show the response in GUI output area
                // These actions are done in a separate thread in order not to block the GUI while waiting
                threadPool.execute(new CmdRemoteHandler(in, cmd, this));
                out.flush();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
