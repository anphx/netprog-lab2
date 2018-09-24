package netprog.functions;

import netprog.gui.AppMain;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

public class CmdRemoteHandler implements Runnable {

    private static int BUFSIZE = 1024;
    private static String cmd;
    private static AppMain window;

    private static BufferedInputStream inStream;

    public CmdRemoteHandler(BufferedInputStream in, String cmd, AppMain window) {
        this.inStream = in;
        this.cmd = cmd;
        this.window = window;
    }

    public CmdRemoteHandler(AppMain window) {
        this.window = window;
    }

    void waitForIt() {
        try {
            byte[] buff = new byte[BUFSIZE];
            String result = "";

            while (inStream.read(buff) != -1) {
                // AnP: Show the response in GUI output area
                result = new String(buff, Charset.forName("UTF-8")).trim();
                window.printToOutput("\nResponse from server....... \n");
                window.printToOutput(">>>> " + cmd + "\n");
                window.printToOutput(result + "\n");

                // AnP: Clean up buffer reader
                Arrays.fill(buff, (byte)0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        waitForIt();
    }

    public void setInputStream(BufferedInputStream in, String cmdStr) {
        inStream = in;
        cmd = cmdStr;
    }
}
