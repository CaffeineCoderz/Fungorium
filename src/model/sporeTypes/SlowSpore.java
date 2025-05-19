package sporeTypes;

import utils.*;
import insect.Insect;
import tektonTypes.*;

import java.io.Serializable;

public class SlowSpore extends Spore implements Serializable {

    private static final long serialVersionUID = 1L;

    /*
        Default constructor for the SlowSpore class.
     */
    public SlowSpore(){
        // Default constructor
    }

    /**
     * Constructor for the SlowSpore class.
     * @param t the Tekton object associated with this spore.
     * @param nutval the nutrition value of the spore.
     */
    public SlowSpore(Tekton t, int nutval){
        super(t, nutval);
    }
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
