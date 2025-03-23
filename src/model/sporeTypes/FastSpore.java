package sporeTypes;

import insect.Insect;

public class FastSpore extends Spore{
    
    /**
     * Speeds up the given insect.
     * 
     * This method is called when an insect consumes this spore.
     * It increases the insect's speed by one.
     * @param insect the insect which consumes this spore.
     */
    public void consumeSpore(Insect insect){
        insect.fast();
        absorbed();
    }
}
