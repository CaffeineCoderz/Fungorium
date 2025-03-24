package sporeTypes;

import insect.Insect;
import utils.*;
import tektonTypes.*;

public class FastSpore extends Spore{
    
    public FastSpore(){
        log = Logger.getLogger("FastSporeLogger");
    }
    public FastSpore(Tekton t, int v){
        super(t,v);
    }
    @Override
    /**
     * Speeds up the given insect.
     * 
     * This method is called when an insect consumes this spore.
     * It increases the insect's speed by one.
     * @param insect the insect which consumes this spore.
     */
    public void consume(Insect insect){
        log.stepIn("insect.fast()");
        insect.fast();
        log.stepOut("insect.fast()",null);
        log.stepIn("absorbed()");
        absorbed();
        log.stepOut("absorbed()", null);
    }
}
