package sporeTypes;

import utils.*;
import insect.Insect;

public class SlowSpore extends Spore{
    
    public SlowSpore(){
        log = Logger.getLogger("SlowSporeLogger");
    }
    /**
     * Consumes this spore and slows the given insect.
     * @param insect the insect which consumes this spore.
     */
    @Override
    public void consume(Insect insect){
        log.stepIn("insect.slow()");
        insect.slow();
        log.stepOut("insect.slow()", insect);
        log.stepIn("absorbed()");
        absorbed();
        log.stepOut("absorbed()", insect);
    }
}
