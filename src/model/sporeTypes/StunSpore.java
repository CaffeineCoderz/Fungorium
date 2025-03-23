package sporeTypes;

import insect.Insect;

public class StunSpore extends Spore{
    
    /**
     * Consumes this spore and stuns the given insect.
     * 
     * This method is called when an insect consumes this spore.
     * It sets the effect of the insect to STUN, preventing it from performing actions,
     * and triggers the absorption process of the spore.
     * 
     * @param insect the insect which consumes this spore.
     */
    public void consumeSpore(Insect insect){
        insect.stun();
        absorbed();
    }
}
