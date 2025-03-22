package tektonTypes;
import fungus.FungusThread;
public class OneThreadTekton extends Tekton {
    public OneThreadTekton() {
        super(true, true);
    }
    @Override
    public void addThread(FungusThread f){
        if(getThreads().isEmpty()){
            getThreads().add(f);
        } 
        else if(getThreads().get(0).getSpecies()==f.getSpecies()) {
            getThreads().add(f);
        else
            return;    
        }
    }
}
