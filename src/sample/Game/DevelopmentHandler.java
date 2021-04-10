package sample.Game;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

public class DevelopmentHandler {
    private String[] easyWords;
    private String[] mediumWords;
    private String[] hardWords;
    private Random random = new Random();
    private static DevelopmentHandler instance = new DevelopmentHandler();

    public static final int DIFFICULTY_EASY   = 0;
    public static final int DIFFICULTY_MEDIUM = 1;
    public static final int DIFFICULTY_HARD   = 2;

    private int[] programsCount = new int[3];

    public static DevelopmentHandler getInstance() {
        return instance;
    }

    private DevelopmentHandler() {
        try {
            easyWords = readFile("assets/easyDict.txt").split("\n");
            mediumWords = readFile("assets/mediumDict.txt").split("\n");
            hardWords = readFile("assets/hardDict.txt").split("\n");
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public String getWord(int difficulty) {
        if(difficulty > 2) {
            throw new RuntimeException("Wrong difficulty");
        }
        switch(difficulty) {
            case 0:
                return easyWords[random.nextInt(easyWords.length)];
            case 1:
                return mediumWords[random.nextInt(mediumWords.length)];
            case 2:
                return hardWords[random.nextInt(hardWords.length)];

        }
        return null;
    }

    public void addProgram(int difficulty) {
        programsCount[difficulty]++;
    }

    public int[] getProgramsCount(){
        return programsCount;
    }

    private String readFile(String file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String         line = null;
        StringBuilder  stringBuilder = new StringBuilder();
        String         ls = System.getProperty("line.separator");

        try {
            while((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append(ls);
            }
            reader.close();
            return stringBuilder.toString();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}