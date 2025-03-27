package logic;

import insect.Insect;
import insect.InsectSpecies;
import sporeTypes.Spore;

public class Entymologist {
    InsectSpecies mySpecies;

    public void setSpecies(FungusSpecies species) {
        mySpecies = species;
    }

    public FungusSpecies getSpecies() {
        return mySpecies;
    }

    public void move(Insect selectedInsect, FungusThread selectedThread) {
        for(Insect insect : mySpecies.getInsects()) {
            if (insect == selectedInsect) {
                selectedInsect.move(selectedThread);
            }
        }

    public void eat(Insect selectedInsect, Spore selectedSpore) {
        for(Insect insect : mySpecies.getInsects()) {
            if(insect == selectedInsect) {
                selectedInsect.eat(selectedSpore);
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
