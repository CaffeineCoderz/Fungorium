package tektonTypes;

import utils.Logger;

import fungus.FungusThread;

public class DecomposingTekton extends Tekton{

    /**
     * Default constructor for the DecomposingTekton class.
     */
    public DecomposingTekton(){
        super(true, true);
    }

    

    /**
     * Adds a FungusThread instance to the list of threads associated with this DecomposingTekton.
     * 
     * When a FungusThread is added to DecomposingTekton, it automatically sets a lifespan to thread, because this tekton consumes the threads that are on it.
     * @param t the FungusThread instance to add to the list of associated threads. 
     */
    @Override
    public void addThread(FungusThread t){
        t.decreaseLife();
        threads.add(t);
    }
}
