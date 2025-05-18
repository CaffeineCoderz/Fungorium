package sporeTypes;

import insect.Insect;
import tektonTypes.Tekton;
import utils.*;

public class DisableCutSpore extends Spore {
    /**
     * Default constructor for the DisableCutSpore class.
     */
    public DisableCutSpore(){
        // Default constructor
    }

    /**
     * Constructor for the DisableCutSpore class.
     * @param t the Tekton object associated with this spore.
     * @param nutval the nutrition value of the spore.
     */
    public DisableCutSpore(Tekton t, int nutval){
        super(t, nutval);
    }
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
