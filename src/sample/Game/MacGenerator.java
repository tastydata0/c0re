package sample.Game;

import java.util.Random;

public class MacGenerator {
    public static String getRandomMac(){
        String mac;
        Random random = new Random();
        mac = "0"+(random.nextInt(9)+1)+"-"+getRandomChar(random)+getRandomChar(random)+"-"+
                (random.nextInt(9)+1)+getRandomChar(random)+"-"+random.nextInt(10)+""+
                random.nextInt(10)+"-"+
                (random.nextInt(9)+1)+getRandomChar(random)+"-"+
                (random.nextInt(9)+1)+getRandomChar(random);
        return mac;
    }
    private static String getRandomChar(Random random){
        String chars = "AABCCDEEFFGHIJKLMNOOPQRSTUVWXYZ";
        return chars.charAt(random.nextInt(chars.length()))+"";
    }
}
