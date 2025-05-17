package sporeTypes;

import insect.Insect;
import tektonTypes.Tekton;

public class MultiplyInsectSpore extends Spore{
    public MultiplyInsectSpore(){
        // Default constructor
    }

    /**
     * Constructor for the MultiplyInsectSpore class.
     * 
     * @param t the Tekton object associated with this spore.
     * @param y the nutrition value of the spore.
     */
    public MultiplyInsectSpore(Tekton t, int nutval){
        super(t, nutval);
    }

    /**
     * Consumes this spore and the given insect duplicates.
     * @param insect the insect which consumes this spore.
     */
    @Override
    public void consume(Insect insect){
        insect.duplicate();
        absorbed();
    }

    /**
     * Consumes this spore and the given insect duplicates.
     * @param insect the insect which consumes this spore.
     * @return a new Insect object that is a duplicate of the consumed insect.
     * 
     * ! This is needed for the cmdproc class, to manage createdobjects
     */
    public Insect consumeMultiply(Insect insect){
        Insect newInsect = insect.duplicate();
        absorbed();
        return newInsect;
    }
}
