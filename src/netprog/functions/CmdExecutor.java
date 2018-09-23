package netprog.functions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CmdExecutor implements Runnable {
    private String cmd;

    public CmdExecutor(String cmd) {
        this.cmd = cmd;
    }

    public String executeCmd() {
        String output = "";

        try {
            String[] commands = {
                "/bin/sh",
                "-c",
                this.cmd
            };
            Process proc = Runtime.getRuntime().exec(commands);

            BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));

            BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));

            // read the output from the command
            String s;
            while ((s = stdInput.readLine()) != null) {
                output = output.concat(s + "\n");
            }
            // output error as well
            while ((s = stdError.readLine()) != null) {
                output = output.concat(s + "\n");
            }

            return output;

        } catch (IOException e) {
            return output;
        }
    }

    @Override
    public void run() {
        // just to run
    }
}
