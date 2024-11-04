package main;

public class Main {
    public static boolean debug = false;
    public static boolean logfps = false;
    
    public static void main(String[] args) {
        for (String arg : args) {
            if (arg.equals("--debug")) {
                debug = true;
                System.out.println("Debugging enabled");
            } else if (arg.equals("--logfps")) {
                logfps = true;
            }
        }
        
        if (debug) {
            System.out.println("Starting program");
            System.out.println("Setting up");
            System.out.println("Loading songs...");
            Songs.load();
        }
    }
}
