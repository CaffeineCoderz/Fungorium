package logic;

import fungus.FungusBody;
import fungus.FungusSpecies;
import fungus.FungusThread;

public class Mycologist {
    private FungusSpecies mySpecies;

    public Mycologist(FungusSpecies species) {
        mySpecies = species;
    }

    public FungusSpecies getSpecies() {
        return mySpecies;
    }

    public void setSpecies(FungusSpecies species) {
        mySpecies = species;
    }
    
    public void grow(Tekton selectedTekton, FungusThread newThread) {
        mySpecies.growBody(thread);
        // ! ide valahogy a selectedTekton-t is be kellene tenni
    }

    public void setThread(Tekton selectedTekton,, FungusThread selectedThread, FungusThread newThread) {
        mySpecies.growThread(selectedTekton, selectedThread, newThread);
    }

    public void kill(Insect selectedInsect, FungusThread selectedThread) {
        // ! ide nincs jelenleg nálam függvény ami használható lett volna
    }
}
