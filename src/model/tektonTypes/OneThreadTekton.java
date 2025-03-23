package tektonTypes;
import fungus.FungusThread;
public class OneThreadTekton extends Tekton {
    public OneThreadTekton() {
        super(true, true);
    }
    @Override
    public void addThread(FungusThread f){
        if(canGrowThread()||getThreads().contains(f.getPrev())){
            getThreads().add(f);
            if(canGrowThread()){
                setGrowThread(false);
            }
        } 
        else    
            return;
    }
}