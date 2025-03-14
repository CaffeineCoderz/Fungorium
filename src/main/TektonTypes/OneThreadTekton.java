package TektonTypes;

public class OneThreadTekton extends Tekton {
    public void override addThread(FungusThread f){
        if(getThreads().size() == 0){
            getThreads().add(f);
        } else{
            return;
        }
    }
}
