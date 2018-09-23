package netprog.functions;

import netprog.gui.AppMain;

import static java.lang.Thread.*;

public class Fibonacci implements Runnable {
    private int seed;
    private int n1 = 0, n2 = 1, n3 = 0;
    private AppMain window;

    public Fibonacci(int seed, AppMain window) {
        this.seed = seed;
        this.window = window;
    }

    public void loop () {
        // this is to debug which thread is running
        //  System.out.println ("Thread " + Thread.currentThread().getId() + " is running");

        int i = 0;
        String outStr = "0 1";
        window.printToOutput("Fibonaci printing starts....... \n");

        // loop n=seed times
        // for each iteration i, print "seed" next number in Fibonaci sequence.
        while (i < seed) {
            // print to output field of the GUI

            outStr = outStr + printFibonacci(seed);
            window.printToOutput("Iteration " + i + ": " + outStr  + "\n");
            i++;
            outStr = "";
        }
    }

    private String printFibonacci(int count) {
        // printing Fibonacci sequence using recursive
        String out = "";

        if(count>0) {
            n3 = n1 + n2;
            n1 = n2;
            n2 = n3;
            System.out.print(" " + n3);
            out = out + " " + String.valueOf(n3) + printFibonacci(count-1);
        }
        return out;

    }

    @Override
    public void run() {
        // start the loop
        loop();
    }
}
