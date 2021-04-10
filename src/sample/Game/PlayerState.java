package sample.Game;

import java.util.HashMap;

public class PlayerState {
    public static String ip,localIp,mac,username = "";
    public static boolean useVPN,cheatsEnabled,usernameSpecified;
    private static float money;
    private static int level = 1, currentXp, xpToNextLevel = 1000, energy, research, internetSpeed = 0;

    public static final int internetSpeedModifier = 2;
    public static final boolean POPUPS_DISABLED = true;


    public static void setCheatsEnabled(boolean cheatsEnabled) {
        PlayerState.cheatsEnabled = cheatsEnabled;
    }

    public static void setUsernameSpecified(boolean usernameSpecified) {
        PlayerState.usernameSpecified = usernameSpecified;
    }

    public static void setMoney(float money) {
        PlayerState.money = money;
    }

    public static void setLevel(int level) {
        PlayerState.level = level;
    }

    public static void setCurrentXp(int currentXp) {
        PlayerState.currentXp = currentXp;
    }

    public static void setXpToNextLevel(int xpToNextLevel) {
        PlayerState.xpToNextLevel = xpToNextLevel;
    }

    public static void setEnergy(int energy) {
        PlayerState.energy = energy;
    }

    public static void setResearch(int research) {
        PlayerState.research = research;
    }

    public static void setInternetSpeed(int internetSpeed) {
        PlayerState.internetSpeed = internetSpeed;
    }

    public static String getIp() {
        return ip;
    }

    public static String getLocalIp() {
        return localIp;
    }

    public static String getMac() {
        return mac;
    }

    public static boolean isUseVPN() {
        return useVPN;
    }

    public static boolean isCheatsEnabled() {
        return cheatsEnabled;
    }

    public static int getEnergy() {
        return energy;
    }

    public static int getResearch() {
        return research;
    }

    public static int getInternetSpeed() {
        return internetSpeed;
    }

    public static void setIp(String ip) {
        PlayerState.ip = ip;
    }
    public static void setMac(String mac) {
        PlayerState.mac = mac;
    }
    public static void setUseVPN(boolean useVPN) {
        PlayerState.useVPN = useVPN;
    }
    public static void setLocalIp(String localIp) {
        PlayerState.localIp = localIp;
    }
    public static void addMoney(float money1) {
        money += money1;
    }
    public static float getMoney() {
        return money;
    }
    public static void enableCheats() {
        cheatsEnabled = true;
    }
    public static void setUsername(String username) {
        PlayerState.username = username;
        usernameSpecified = true;
    }
    public static String getUsername() {
        return username;
    }
    public static boolean isUsernameSpecified() {
        return usernameSpecified;
    }

    public static int getLevel() {
        return level;
    }

    public static int getCurrentXp() {
        return currentXp;
    }

    public static int getXpToNextLevel() {
        return xpToNextLevel;
    }

    public static void addLevel() {
        PlayerState.level += 1;
    }

    public static void addXp(int currentXp) {
        PlayerState.currentXp += currentXp;
        while (PlayerState.currentXp >= xpToNextLevel) {
            addLevel();
            xpToNextLevel += 1000;
        }
        PlayerInfo.updateXpBar();
    }
}
