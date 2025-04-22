package sporeTypes;

import utils.*;
import insect.Insect;
import tektonTypes.*;

public class SlowSpore extends Spore{
    
    public SlowSpore(){
    }
    public SlowSpore(Tekton t, int v){
        super(t,v);
    }
    /**
     * Consumes this spore and slows the given insect.
     * @param insect the insect which consumes this spore.
     */
    @Override
    public void consume(Insect insect){
        insect.slow();
        absorbed();
    }
}
