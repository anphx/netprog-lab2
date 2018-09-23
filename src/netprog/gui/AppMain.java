package netprog.gui;

import netprog.functions.CmdExecutor;
import netprog.functions.Fibonacci;

import javax.swing.*;

public class AppMain {
    private JPanel mainPanel;
    private JButton btnExec;
    private JTextArea txtOut;
    private JTextField txtIn;
    private JButton btnFibonacci;

    private int seed = 0;

    public AppMain(String[] args) {
        this.seed = Integer.parseInt(args[0]);
        AppMain self = this;

        btnExec.addActionListener(e -> {
            // Read command entered from the input, execute and show results in the output
            String input = txtIn.getText();

            // This is to print the input command to output field
            txtOut.append(">>" + input + "...\n");

            String out = "";
            if (input.length() >= 1) {
//                out = CmdExecutor.executeCmd(input);
                CmdExecutor executor = new CmdExecutor(input);
                new Thread(executor).start();
                out = executor.executeCmd();
            }
            txtOut.append(out);
        });

        btnFibonacci.addActionListener(e -> new Thread(new Fibonacci(seed, self)).start());
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
}
