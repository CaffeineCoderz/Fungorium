package logic;

import fungus.FungusBody;
import fungus.FungusSpecies;
import fungus.FungusThread;

public class Mycologist {
<<<<<<< HEAD
<<<<<<< HEAD
    private FungusSpecies mySpecies;

=======
    FungusSpecies mySpecies;
>>>>>>> 0dffc08 (Mycologist majdnem kész)
=======
    private FungusSpecies mySpecies;

>>>>>>> 098513a6908733e28c5ce89264663b736b0fc360
    public Mycologist(FungusSpecies species) {
        mySpecies = species;
    }

    public FungusSpecies getSpecies() {
        return mySpecies;
    }

    public void setSpecies(FungusSpecies species) {
        mySpecies = species;
    }
    
    public void grow(FungusThread selectedThread) {
        mySpecies.growBody(thread);
    }

    public void setThread(FungusThread selectedThread, FungusThread newThread) {
        mySpecies.growThread(selectedThread.getTekton(), selectedThread, newThread);
    }

    public void kill(FungusThread selectedThread) {
        mySpecies.eatInsect(selectedThread);
    }

    public void sporulate(FungusBody selectedBody) {
        for(FungusBody body : mySpecies.getBodies()) {
            if(body == selectedBody) {
                selectedBody.sporulate();
            }
        }
    }
}
