package sample.Game;

import java.util.ArrayList;

public class StructureHandler {
    private static StructureHandler ourInstance = new StructureHandler();

    public static StructureHandler getInstance() {
        return ourInstance;
    }

    public ArrayList<Structure> structures;

    private StructureHandler() {
    }
    public void registerStructure(Structure structure) {
        structures.add(structure);
    }

    public Structure getByName(String name) {
        for (Structure s:structures) {
            if(s.getName().equals(name)) {
                return s;
            }
        }
        return null;
    }

    public ArrayList<Structure> getStructures() {
        return structures;
    }

    public void setStructures(ArrayList<Structure> structures) {
        this.structures = structures;
    }
}
