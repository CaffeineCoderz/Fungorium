package tektonTypes;
import fungus.FungusThread;
public class OneThreadTekton extends Tekton {
    public OneThreadTekton() {
        super(true, true);
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
            getThreads().add(f);
        } else{
            return;
        }
    }
}
