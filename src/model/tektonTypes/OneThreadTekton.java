package tektonTypes;

public class OneThreadTekton extends Tekton {
    @Override
    public void addThread(FungusThread f){
        if(getThreads().size() == 0){
            getThreads().add(f);
        } else{
            return;
        }
    }
}
