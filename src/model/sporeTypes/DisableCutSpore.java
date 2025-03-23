package sporeTypes;

import insect.Insect;

public class DisableCutSpore extends Spore {

    @Override
    public void consume(Insect insect){
        insect.disableCut();
        absorbed();
    }
}
