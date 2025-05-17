package sporeTypes;

import insect.Insect;
import tektonTypes.*;

public class FastSpore extends Spore {

    /**
     * Default constructor for the FastSpore class.
     * This constructor initializes a FastSpore object without any specific Tekton
     * or nutrition value.
     */
    public FastSpore() {
        // Default constructor
    }

    /**
     * Constructor for the FastSpore class.
     * 
     * @param t      the Tekton object associated with this spore.
     * @param nutval the nutrition value of the spore.
     */
    public FastSpore(Tekton t, int nutval) {
        super(t, nutval);
    }

    @Override
    /**
     * Speeds up the given insect.
     * 
     * This method is called when an insect consumes this spore.
     * It increases the insect's speed by one.
     * 
     * @param insect the insect which consumes this spore.
     */
    public void consume(Insect insect) {
        insect.fast();
        absorbed();
    }
}
