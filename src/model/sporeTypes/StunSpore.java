package sporeTypes;

import insect.Insect;
import utils.Logger;
import tektonTypes.*;

import java.io.Serializable;

public class StunSpore extends Spore implements Serializable {
    private static final long serialVersionUID = 1L;

    public StunSpore(){    }

    /**
     * Constructor for the StunSpore class.
     * @param t the Tekton object associated with this spore.
     * @param nutval the nutrition value of the spore.
     */
    public StunSpore(Tekton t, int nutval){
        super(t, nutval);

    }
    /**
     * Consumes this spore and stuns the given insect.
     * 
     * This method is called when an insect consumes this spore.
     * It sets the effect of the insect to STUN, preventing it from performing actions,
     * and triggers the absorption process of the spore.
     * 
     * @param insect the insect which consumes this spore.
     */
    @Override
    public void consume(Insect insect){
        insect.stun();
        absorbed();
    }

}
