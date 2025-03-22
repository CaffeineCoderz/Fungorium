package sporeTypes;

import insect.Insect;

public class SlowSpore extends Spore{
    
    public void consumeSpore(Insect insect){
        insect.slow();
        absorbed();
    }
}
