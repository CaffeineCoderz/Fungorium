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

    public void move(Insect selectedInsect, FungusThread thread) {
        mySpecies.moveInsect(selectedInsect, thread);
    }

    public void eat(Insect selectedInsect, Spore spore) {
        mySpecies.eatSpore(selectedInsect, spore);
    }
}
