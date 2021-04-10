package sample.Game;

import java.util.Random;

public class IpGenerator {
    public static String generateRandomIp() {
        Random random = new Random();
        return (""+(random.nextInt(256))+"."+
                (random.nextInt(256))+"."+(random.nextInt(256))+"."+(random.nextInt(256)));
    }
    public static String generateRandomLocalIp() {
        Random random = new Random();
        return ("192.168."+(random.nextInt(256))+"."+(random.nextInt(256)));
    }
}
