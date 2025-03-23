package sporeTypes;

import insect.Insect;

public class DisableCutSpore extends Spore {
    
    /**
     * Disables the given insect's ability to cut fungus threads.
     * This method is called when an insect consumes this spore.
     * @param insect the insect which consumes this spore.
     */
    @Override
    public void consume(Insect insect){
        insect.disableCut();
        absorbed();
    }
}
