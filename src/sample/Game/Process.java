package sample.Game;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import sun.management.counter.Counter;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;

public class Process extends Thread {
    private String input;
    private Cmd cmd;
    private int sleepIteration;
    private float uploadProgress;

    public Process(Cmd cmd, String input) {
        this.input = input;
        this.cmd = cmd;
        Platform.runLater(this);
    }

    /**
     * If this thread was constructed using a separate
     * <code>Runnable</code> run object, then that
     * <code>Runnable</code> object's <code>run</code> method is called;
     * otherwise, this method does nothing and returns.
     * <p>
     * Subclasses of <code>Thread</code> should override this method.
     *
     * @see #start()
     * @see #stop()
     */
    @Override
    public void run() {
        super.run();
        handle(input);
    }

    private void handle(String input) {
        String[] pcs = input.split(" ");
        if(pcs.length == 0) {
            pcs = new String[]{""};
        }
        String command = pcs[0];
        String args = input.replaceAll(command, "").trim();

        String output = "";
        // Command handling
        if     (command.equalsIgnoreCase("shutdown")) {
            Prefs.save();
            output("Shutting down all services...",Color.ORANGE);
            Platform.exit();
        }
        else if(command.equalsIgnoreCase("upload")) {
            String targetStructureName = args;
            Structure target = StructureHandler.getInstance().getByName(targetStructureName);
            if (target != null) {
                if(DevelopmentHandler.getInstance().getProgramsCount()[target.getType()] > 0) {
                    DevelopmentHandler.getInstance().getProgramsCount()[target.getType()]--;
                    final float CYCLE_COUNT = 1000 + target.getType()*1000;
                    output("Uploading...", Color.GOLD);
                    output("", Color.LIGHTGREEN);
                    uploadProgress = 0;
                    Timeline timeline = new Timeline(new KeyFrame(Duration.millis(6), event -> {
                        incrementUploadProgress();
                        cmd.setStatus(StatusBarGenerator.generate(uploadProgress / CYCLE_COUNT));
                    }));
                    timeline.setCycleCount((int) CYCLE_COUNT);
                    timeline.play();
                    timeline.setOnFinished(event -> {
                        target.addFirmware();
                        append("Done.");
                        cmd.getChildren().remove(cmd.status);

                        Timeline popupTimeline = new Timeline(new KeyFrame(Duration.seconds(4), event1 -> {
                            cmd.window.desktop.getChildren().add(new Popup("Поздравляем!\nВаша первая антенна начала давать людям интернет.\n" +
                                    "Попробуйте построить больше антенн.", cmd.window.desktop, 4));
                        }));
                        popupTimeline.setCycleCount(1);
                        popupTimeline.play();
                    });

                }
                else {
                    output("There is no firmware for "+targetStructureName+". Write it using IDE", Color.RED);
                }


            }
            else {
                output("Unable to find "+targetStructureName+".", Color.RED);
            }

        }
        else if(command.equalsIgnoreCase("money")) {
            output("Balance: "+PlayerState.getMoney()+" bitcoins.",Color.GOLD);
        }
        else if(command.equalsIgnoreCase("int+")) {
            PlayerState.setInternetSpeed(PlayerState.getInternetSpeed()+100);
            CountersHandler.speed.updateValue(PlayerState.getInternetSpeed());
        }
        else if(command.equalsIgnoreCase("firm+")) {
            DevelopmentHandler.getInstance().addProgram(0);
            DevelopmentHandler.getInstance().addProgram(1);
            DevelopmentHandler.getInstance().addProgram(2);
        }
        else if(command.equalsIgnoreCase("progress_reset")) {
            File file = new File("prefs.dat");
            if(file.exists()) {
                System.out.println("DELETING: "+file.delete());
            }
            Platform.exit();
        }
        else if(command.equalsIgnoreCase("help")) {
            output("+------------------------------ Help ------------------------------+\n" +
                   "| \'shutdown\'                                    save game and exit |\n" +
                   //"| \'money\'                                    print current balance |\n" +
                   "| \'ls\'            print all files and folders in current directory |\n" +
                   "| \'cd <Folder name>\'                               go to directory |\n" +
                   "| \'ip\'                                   print local and global ip |\n" +
                   "| \'mac\'                                       print computer's mac |\n" +
                   "| \'pwd\'      prints the absolute path to current working directory |\n" +
                   "| \'mac\'                                       print computer's mac |\n" +
                   "| \'mkdir <Name>\'                create folder in current directory |\n" +
                   "| \'cat <File path>\'                           print file's content |\n" +
                   "| \'date\'                                        print current date |\n" +
                   "| \'time\'                                        print current time |\n" +
                   "| \'sleep <Seconds>\'                        system will go to sleep |\n" +
                   "| \'clear\'                                           clear terminal |\n" +
                   "| \'upload <Название объекта>\'          загрузить прошивку в объект |\n" +
                   //"| \'apt-get <Program> install\'             install concrete program |\n" +
                   //"| \'language <En/Ru>\'                            set other language |\n"+
                   "+------------------------------------------------------------------+\n",Color.WHITE);

        }
        else if(command.equalsIgnoreCase("service")) {

        }
        else if(command.equalsIgnoreCase("log")) {
            cmd.window.desktop.getLog().add(args,Color.GRAY);
        }
        else if(command.equalsIgnoreCase("ls")) {
            File[] ls = new File("in-game"+cmd.currentDirectory).listFiles();
            for (int i = 0; i < ls.length; i++) {
                if(i!=ls.length-1)
                    if(ls[i].getName().contains(".")) {
                        output(ls[i].getName(),Color.WHITE);
                    }
                    else {
                        output(ls[i].getName(),Color.SKYBLUE);
                    }

                else {
                    if(ls[i].getName().contains(".")) {
                        output(ls[i].getName(),Color.WHITE);
                    }
                    else {
                        output(ls[i].getName(),Color.SKYBLUE);
                    }
                }
            }
        }
        else if(command.equalsIgnoreCase("cd")) {
            if(args.equals("")) {
                output("Error: directory is not specified",Color.RED);
            }
            else {
                File folder = new File("in-game" + cmd.currentDirectory + args);
                if (folder.exists()) {
                    if(args.equals("../")) {
                        String newDir = "";
                        String[] folders = cmd.currentDirectory.split("/");
                        for (int i = 0; i < folders.length-1; i++) {
                            newDir += folders[i]+"/";
                        }
                        cmd.currentDirectory = newDir;
                    }
                    else
                        cmd.currentDirectory += args + "/";
                } else
                    output("Cannot find directory '" + args + "'", Color.RED);
            }
        }
        else if(command.equalsIgnoreCase("mkdir")) {
            new File("in-game/"+cmd.currentDirectory+args).mkdirs();
        }
        else if(command.equalsIgnoreCase("cat")) {
            if(!args.equals("")) {
                Scanner scanner;
                try {
                    scanner = new Scanner(new File("in-game"+cmd.currentDirectory+args));
                    while (scanner.hasNextLine()) {
                        output += scanner.nextLine()+"\n";
                    }
                    output(output, Color.WHITE);
                } catch (FileNotFoundException e) {
                    output("Error: file not found", Color.RED);
                }
            }
            else {
                output("Error: file name is not specified",Color.RED);
            }


        }
        else if(command.equalsIgnoreCase("pwd")) {
            output(cmd.currentDirectory,Color.SKYBLUE);
        }
        else if(command.equalsIgnoreCase("time")) {
            output(new SimpleDateFormat("HH:mm:ss").format(new Date()),Color.WHITE);
        }
        else if(command.equalsIgnoreCase("date")) {
            output(new SimpleDateFormat("d.M.y").format(new Date()),Color.WHITE);
        }
        else if(command.equalsIgnoreCase("sleep")) {
            int sleepMilis = Integer.parseInt(args)*1000;
            sleepIteration = 0;
            Timeline sleep = new Timeline(new KeyFrame(Duration.millis(50),event -> {
                cmd.setStatus(StatusBarGenerator.generate((double)sleepIteration/(sleepMilis/50)));
                increment();
            }));
            sleep.setCycleCount(sleepMilis/50);
            sleep.play();
            sleep.setOnFinished(event -> {
                cmd.processFinish(this);
                cmd.getChildren().remove(cmd.status);
            });
            return;
        }
        else if(command.equalsIgnoreCase("ip")) {
            output("Global ip: "+PlayerState.ip+"\nLocal ip: "+PlayerState.localIp+"\nUse VPN: "+PlayerState.useVPN,Color.WHITE);
        }
        else if(command.equalsIgnoreCase("ping")) {

        }
        else if(command.equalsIgnoreCase("mac")) {
            output(PlayerState.mac,Color.WHITE);
        }
        else if(command.equalsIgnoreCase("target")) {

        }
        else if(command.equalsIgnoreCase("clear")) {
            cmd.lines.getChildren().clear();
            cmd.linesContent.clear();
        }
        else if(command.equalsIgnoreCase("apt-get")) {

        }
        else if(command.equalsIgnoreCase("banner")) {
            int bannerId;
            if(!args.equals("")) {
                bannerId = Integer.parseInt(args);
            }
            else {
                bannerId = new Random().nextInt(8)+2;
            }
            switch (bannerId){
                case 0:
                    output("               _/                       \n" +
                            "    _/_/_/  _/  _/  _/  _/_/    _/_/    \n" +
                            " _/        _/  _/  _/_/      _/_/_/_/   \n" +
                            "_/        _/  _/  _/        _/          \n" +
                            " _/_/_/    _/    _/          _/_/_/     \n" +
                            "                                        ",Color.SKYBLUE);
                    break;
                case 1:
                    output("       ___           \n" +
                            "  ___ / _ \\ _ __ ___ \n" +
                            " / __| | | | '__/ _ \\\n" +
                            "| (__| |_| | | |  __/\n" +
                            " \\___|\\___/|_|  \\___|\n" +
                            "                     ",Color.SKYBLUE);
                    break;
                case 2:
                    output("            ______                      \n" +
                            "           /      \\                     \n" +
                            "  _______ |  $$$$$$\\  ______    ______  \n" +
                            " /       \\| $$  | $$ /      \\  /      \\ \n" +
                            "|  $$$$$$$| $$  | $$|  $$$$$$\\|  $$$$$$\\\n" +
                            "| $$      | $$  | $$| $$   \\$$| $$    $$\n" +
                            "| $$_____ | $$__/ $$| $$      | $$$$$$$$\n" +
                            " \\$$     \\ \\$$    $$| $$       \\$$     \\\n" +
                            "  \\$$$$$$$  \\$$$$$$  \\$$        \\$$$$$$$\n" +
                            "                                        \n" +
                            "                                        \n" +
                            "                                        ",Color.RED);
                    break;
                case 3:
                    output(" ______     ______     ______     ______    \n" +
                            "/\\  ___\\   /\\  __ \\   /\\  == \\   /\\  ___\\   \n" +
                            "\\ \\ \\____  \\ \\ \\/\\ \\  \\ \\  __<   \\ \\  __\\   \n" +
                            " \\ \\_____\\  \\ \\_____\\  \\ \\_\\ \\_\\  \\ \\_____\\ \n" +
                            "  \\/_____/   \\/_____/   \\/_/ /_/   \\/_____/ \n" +
                            "                                            ",Color.ORANGE);
                    break;
                case 4:
                    output("       _______                 \n" +
                            "  ____ \\   _  \\_______   ____  \n" +
                            "_/ ___\\/  /_\\  \\_  __ \\_/ __ \\ \n" +
                            "\\  \\___\\  \\_/   \\  | \\/\\  ___/ \n" +
                            " \\___  >\\_____  /__|    \\___  >\n" +
                            "     \\/       \\/            \\/ ",Color.YELLOW);
                    break;
                case 5:
                    output("   _,.----.     _,.---._                    ,----.  \n" +
                            " .' .' -   \\  ,-.' , -  `.   .-.,.---.   ,-.--` , \\ \n" +
                            "/==/  ,  ,-' /==/_,  ,  - \\ /==/  `   \\ |==|-  _.-` \n" +
                            "|==|-   |  .|==|   .=.     |==|-, .=., ||==|   `.-. \n" +
                            "|==|_   `-' \\==|_ : ;=:  - |==|   '='  /==/_ ,    / \n" +
                            "|==|   _  , |==| , '='     |==|- ,   .'|==|    .-'  \n" +
                            "\\==\\.       /\\==\\ -    ,_ /|==|_  . ,'.|==|_  ,`-._ \n" +
                            " `-.`.___.-'  '.='. -   .' /==/  /\\ ,  )==/ ,     / \n" +
                            "                `--`--''   `--`-`--`--'`--`-----``  ",Color.LIGHTGREEN);
                    break;
                case 6:
                    output(" ::::::::   ::::::::  :::::::::  :::::::::: \n" +
                            ":+:    :+: :+:    :+: :+:    :+: :+:        \n" +
                            "+:+        +:+    +:+ +:+    +:+ +:+        \n" +
                            "+#+        +#+    +:+ +#++:++#:  +#++:++#   \n" +
                            "+#+        +#+    +#+ +#+    +#+ +#+        \n" +
                            "#+#    #+# #+#    #+# #+#    #+# #+#        \n" +
                            " ########   ########  ###    ### ########## ",Color.SKYBLUE);
                    break;
                case 7:
                    output("      ___           ___           ___           ___     \n" +
                            "     /  /\\         /  /\\         /  /\\         /  /\\    \n" +
                            "    /  /:/        /  /::\\       /  /::\\       /  /:/_   \n" +
                            "   /  /:/        /  /:/\\:\\     /  /:/\\:\\     /  /:/ /\\  \n" +
                            "  /  /:/  ___   /  /:/  \\:\\   /  /:/~/:/    /  /:/ /:/_ \n" +
                            " /__/:/  /  /\\ /__/:/ \\__\\:\\ /__/:/ /:/___ /__/:/ /:/ /\\\n" +
                            " \\  \\:\\ /  /:/ \\  \\:\\ /  /:/ \\  \\:\\/:::::/ \\  \\:\\/:/ /:/\n" +
                            "  \\  \\:\\  /:/   \\  \\:\\  /:/   \\  \\::/~~~~   \\  \\::/ /:/ \n" +
                            "   \\  \\:\\/:/     \\  \\:\\/:/     \\  \\:\\        \\  \\:\\/:/  \n" +
                            "    \\  \\::/       \\  \\::/       \\  \\:\\        \\  \\::/   \n" +
                            "     \\__\\/         \\__\\/         \\__\\/         \\__\\/    ",Color.VIOLET);
                    break;
                case 8:
                    output( "  ██████╗ ██████╗ ██████╗ ███████╗\n" +
                            " ██╔════╝██╔═══██╗██╔══██╗██╔════╝\n" +
                            " ██║       ██║   ██║██████╔╝█████╗  \n" +
                            " ██║       ██║   ██║██╔══██╗██╔══╝  \n" +
                            " ╚██████╗╚██████╔╝██║  ██║███████╗\n" +
                            "  ╚═════╝ ╚═════╝ ╚═╝  ╚═╝╚══════╝\n" +
                            "                                 ",Color.PINK);
                    break;
                case 9:
                    output(" ▄████▄   ▒█████     ██▀███  ▓█████ \n" +
                            "▒██▀ ▀█  ▒██▒  ██▒ ▓██ ▒ ██▒▓█   ▀ \n" +
                            "▒▓█    ▄ ▒██░  ██▒ ▓██ ░▄█ ▒▒███   \n" +
                            "▒▓▓▄ ▄██▒▒██   ██░▒██▀▀█▄  ▒▓█  ▄ \n" +
                            "▒ ▓███▀ ░░ ████▓▒░░██▓ ▒██▒░▒████▒\n" +
                            "░ ░▒ ▒  ░░ ▒░▒░▒░ ░▒▓ ░▒▓░░░ ▒░ ░\n" +
                            "  ░  ▒     ░ ▒ ▒░   ░▒ ░ ▒░ ░ ░  ░\n" +
                            "░        ░ ░ ░ ▒    ░░   ░    ░   \n" +
                            "░ ░          ░ ░     ░        ░  ░\n" +
                            "░                                 ",Color.LIGHTGREEN);
                    break;
            }

        }
        else if(command.equalsIgnoreCase("scan")) {
            cmd.window.desktop.getMap().addPoint();
        }
        else if(command.equalsIgnoreCase("echo")) {
            output(args,Color.WHITE);
        }
        else if(command.equalsIgnoreCase("pixiedust") ) {
            if(args.equalsIgnoreCase("-money") && PlayerState.cheatsEnabled) {
                PlayerState.addMoney(1);
            }
            else if(args.equalsIgnoreCase("-enable")) {
                if(!PlayerState.cheatsEnabled) {
                    output("Cheats enabled!", Color.LIGHTGREEN);
                }
                PlayerState.enableCheats();
            }
            else if(args.equalsIgnoreCase("-register")) {

            }
            else if(args.equalsIgnoreCase("-xp")) {
                PlayerState.addXp(300);
            }
        }
        else if(command.equalsIgnoreCase("")) {

        }
        else {
            output("Unknown command. Type <help> to print all commands.", Color.RED);
        }
        cmd.processFinish(this);

    }

    private void incrementUploadProgress() {
        uploadProgress++;
    }

    private void append(String text) {
        cmd.append(text);
    }

    private void output(String message, Color color) {
        cmd.output(message,color);
    }

    private void increment() {
        sleepIteration++;
    }
}
