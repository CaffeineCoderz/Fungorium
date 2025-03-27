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
    
    public void grow(FungusThread selectedThread) {
        mySpecies.growBody(selectedThread);
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
