package sporeTypes;

import insect.Insect;
import utils.*;

public class DisableCutSpore extends Spore {
    
    public DisableCutSpore(){
        log = Logger.getLogger("DisableCutSpore");
    }

    /**
     * Disables the given insect's ability to cut fungus threads.
     * This method is called when an insect consumes this spore.
     * @param insect the insect which consumes this spore.
     */
    @Override
    public void consume(Insect insect){
        log.stepIn("insect.disableCut()");
        insect.disableCut();
        log.stepOut("insect.disableCut()", null);
        log.stepIn("absorbed()");
        absorbed();
        log.stepOut("absorbed()", null);
    }
}
