package logic;

import fungus.FungusSpecies;
import fungus.FungusThread;
import insect.Insect;
import insect.InsectSpecies;
import sporeTypes.Spore;

public class Entymologist {
    private InsectSpecies mySpecies;

    public void setSpecies(InsectSpecies species) {
        mySpecies = species;
    }

    public InsectSpecies getSpecies() {
        return mySpecies;
    }

    public void move(Insect selectedInsect, FungusThread selectedThread) {
        for(Insect insect : mySpecies.getInsects()) {
            if (insect == selectedInsect) {
                selectedInsect.move(selectedThread);
            }
        }
    }
    
    public void eat(Insect selectedInsect, Spore selectedSpore) {
        for(Insect insect : mySpecies.getInsects()) {
            if(insect == selectedInsect) {
                selectedInsect.consumeSpore(selectedSpore);
            }
        }
    }

    public void cut(Insect selectedInsect, FungusThread selectedThread) {
        for(Insect insect : mySpecies.getInsects()) {
            if(insect == selectedInsect) {
                selectedInsect.cut(selectedThread);
            }
        }
    }
}
