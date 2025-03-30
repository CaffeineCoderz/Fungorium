package fungus;

import logic.Player;
import tektonTypes.Tekton;

public class Mycologist extends Player{
    private FungusSpecies mySpecies;
    private Integer score;

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

    public void setThread(FungusThread selectedThread) {
        mySpecies.growThread(selectedThread.getTekton(null), selectedThread);
    }
    public void setBridge(FungusThread fromThread,Tekton toTekton){
        mySpecies.growBridge(fromThread, toTekton);
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
