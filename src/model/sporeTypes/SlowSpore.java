package sporeTypes;

import insect.Insect;

public class SlowSpore extends Spore{
    
    /**
     * Consumes this spore and slows the given insect.
     * @param insect the insect which consumes this spore.
     */
    public void consumeSpore(Insect insect){
        insect.slow();
        absorbed();
    }
}
