package sporeTypes;

import insect.Insect;

public class SlowSpore extends Spore{
    
    /**
     * Consumes this spore and slows the given insect.
     * @param insect the insect which consumes this spore.
     */
    @Override
    public void consume(Insect insect){
        insect.slow();
        absorbed();
    }
}
