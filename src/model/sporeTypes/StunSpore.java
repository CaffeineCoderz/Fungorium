package sporeTypes;

import insect.Insect;
import utils.Logger;
import tektonTypes.*;

public class StunSpore extends Spore{
    
    
    public StunSpore(){
        
        log = Logger.getLogger("StunSporeLogger");
    }
    public StunSpore(Tekton t, int nt){
        super(t, nt);
    }
    /**
     * Consumes this spore and stuns the given insect.
     * 
     * This method is called when an insect consumes this spore.
     * It sets the effect of the insect to STUN, preventing it from performing actions,
     * and triggers the absorption process of the spore.
     * 
     * @param insect the insect which consumes this spore.
     */
    @Override
    public void consume(Insect insect){
        log.stepIn("insect.stun()");
        insect.stun();
        log.stepOut("insect.stun()", null);
        log.stepIn("absorbed()");
        absorbed();
        log.stepOut("absorbed()", null);
    }

}
