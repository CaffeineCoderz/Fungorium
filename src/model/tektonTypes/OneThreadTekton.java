package tektonTypes;
import fungus.FungusThread;
import utils.*;
public class OneThreadTekton extends Tekton {
    public OneThreadTekton() {
        super(true, true);
        log = Logger.getLogger("OneThreadTektonLogger");
    }
    /**
     * Adds a FungusThread instance to the list of threads associated with this OneThreadTekton.
     * 
     * If the list of associated threads is empty, the FungusThread is added to the list.
     * Otherwise, the method simply returns without adding the FungusThread.
     * @param f the FungusThread instance to be added.
     */
    @Override
    public void addThread(FungusThread f){
        if(getThreads().isEmpty()){
            log.askQ("There is no thread on tekton", false);
            log.stepIn("getThreads().add(f)");
            log.stepOut("getThreads().add(f)", getThreads().add(f));
        } else{
            log.askQ("There is a thread on the tekton", false);
            return;
        }
    }
}
