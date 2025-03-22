package sporeTypes;

import insect.Insect;

public class DisableCutSpore extends Spore {

    
    public void consumeSpore(Insect insect){
        insect.disableCut();
        absorbed();
    }
}
