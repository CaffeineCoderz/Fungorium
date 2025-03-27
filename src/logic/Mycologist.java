package logic;

import fungus.FungusBody;
import fungus.FungusSpecies;
import fungus.FungusThread;

public class Mycologist {
    FungusSpecies mySpecies;
    public Mycologist(FungusSpecies species) {
        mySpecies = species;
    }

    public FungusSpecies getSpecies() {
        return mySpecies;
    }

    public void setSpecies(FungusSpecies species) {
        mySpecies = species;
    }
    
    public void grow(FungusThread thread) {
        mySpecies.growBody(thread);
    }

    public void setThread(Tekton tekton, FungusThread selectedThread, FungusThread newThread) {
        mySpecies.growThread(tekton, selectedThread, newThread);
    }

    public void killInsect(FungusThread thread) {
        
    }
}
