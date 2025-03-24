package tektonTypes;
import fungus.FungusThread;
public class OneThreadTekton extends Tekton {
    public OneThreadTekton() {
        super(true, true);
    }
    @Override
    public void addThread(FungusThread f){
        if(canGrowThread()||getThreads().contains(f.getPrev())){
            log.askQ("tekton has no thread", false);
            log.stepIn("getThreads().add(f)");
            getThreads().add(f);
            log.stepOut("getThreads().add(f)", null);
            if(canGrowThread()){
                log.askQ("Can grow thread", false);
                log.stepIn("setGrowThread(false)");
                setGrowThread(false);
                log.stepOut("setGrowThread(false)", null);
            }
        } 
        else
            log.askQ("tekton already has a thread", false);    
    }
}