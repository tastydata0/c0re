package sample.Game;

import javafx.scene.layout.Pane;

import java.io.*;
import java.util.ArrayList;
import java.util.prefs.Preferences;

public class Prefs implements Serializable {
    private static Preferences prefs = Preferences.userNodeForPackage(Prefs.class);
    static final long serialVersionUID = -6577980448693999319L;

    private int internetSpeed;
    private ArrayList<Structure> structures;


    public static void load(DDoSMap map) {
        try {
            Prefs loaded;
            try {
                FileInputStream fileOut =
                        new FileInputStream("prefs.dat");
                ObjectInputStream in = new ObjectInputStream(fileOut);
                loaded = (Prefs) in.readObject();
                in.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
                loaded = getDefaults();
            }
            PlayerState.setInternetSpeed(loaded.internetSpeed);
            StructureHandler.getInstance().setStructures(loaded.structures);
            System.out.println("Structures: ");
            for (Structure s : loaded.structures) {
                s.refresh();
                map.getChildren().add(s);

            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void save() {
        Prefs toSave = new Prefs();
        toSave.internetSpeed = PlayerState.getInternetSpeed();
        toSave.structures = StructureHandler.getInstance().getStructures();
        try {
            FileOutputStream fileOut =
                    new FileOutputStream("prefs.dat");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(toSave);
            out.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Prefs getDefaults() {
        Prefs def = new Prefs();
        def.internetSpeed = 100;
        def.structures = new ArrayList<>();

        return def;
    }
}
