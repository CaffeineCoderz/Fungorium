package tektonTypes;

import utils.Logger;

import fungus.FungusThread;

public class DecomposingTekton extends Tekton{

    public DecomposingTekton(){
        super(true, true);
        log = Logger.getLogger("DecomposingTektonLogger");
    }

    /**
     * Adds a FungusThread instance to the list of threads associated with this DecomposingTekton.
     * 
     * When a FungusThread is added to DecomposingTekton, it automatically sets a lifespan to thread, because this tekton consumes the threads that are on it.
     * @param t the FungusThread instance to add to the list of associated threads. 
     */
    @Override
    public void addThread(FungusThread t){
        log.stepIn("t.decreaseLife()");
        t.decreaseLife();
        log.stepOut("t.decreaseLife()", null);
        log.stepIn("threads.add(t)");
        log.stepOut("threads.add(t)", threads.add(t));
    }
}
