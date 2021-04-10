package sample.Game;

import javafx.application.Platform;

public class CmdIO {
    private CmdIO() {

    }
    public static String handle(String input) {
        String[] pcs = input.split(" ");
        String command = pcs[0];
        String[] args = input.replaceAll(command, "").split(" ");

        String output = "";

        // Command handling
        if     (command.equalsIgnoreCase("shutdown")) {
            output = "Shutting down all services...";
            Platform.exit();
        }
        else if(command.equalsIgnoreCase("money")) {
            // TODO money output
        }
        else if(command.equalsIgnoreCase("help")) {
            // TODO help output
        }
        else if(command.equalsIgnoreCase("service")) {

        }
        else if(command.equalsIgnoreCase("log")) {

        }
        else if(command.equalsIgnoreCase("ls")) {

        }
        else if(command.equalsIgnoreCase("cd")) {

        }
        else if(command.equalsIgnoreCase("mkdir")) {

        }
        else if(command.equalsIgnoreCase("cat")) {
            // Output of file's content
        }
        else if(command.equalsIgnoreCase("pwd")) {

        }
        else if(command.equalsIgnoreCase("time")) {

        }
        else if(command.equalsIgnoreCase("date")) {

        }
        else if(command.equalsIgnoreCase("sleep")) {

        }
        else if(command.equalsIgnoreCase("ip")) {

        }
        else if(command.equalsIgnoreCase("ping")) {

        }
        else if(command.equalsIgnoreCase("mac")) {

        }
        else if(command.equalsIgnoreCase("target")) {

        }
        else if(command.equalsIgnoreCase("clear")) {
            // Clear cmd
        }
        else if(command.equalsIgnoreCase("apt-get")) {

        }
        else if(command.equalsIgnoreCase("")) {

        }
        else if(command.equalsIgnoreCase("")) {

        }
        else if(command.equalsIgnoreCase("")) {

        }
        else if(command.equalsIgnoreCase("")) {

        }
        else if(command.equalsIgnoreCase("")) {

        }


    }
}
