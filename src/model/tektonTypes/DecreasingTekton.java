package tektonTypes;

import insect.Insect;
import utils.Logger;

public class DecreasingTekton extends Tekton{

    public DecreasingTekton(){
        super(true, true);
        log = Logger.getLogger("DecreasingTektonLogger");
    }

    /**
     * Adds an insect to the DecreasingTekton.
     * 
     * This method first checks if the insect is already present in the list
     * of insects. If not, it sets the insect's onDecreasing flag to true and
     * adds the insect to the list.
     * 
     * @param insect the Insect object to be added to the DecreasingTekton.
     */
    @Override
    public void addInsect(Insect insect){
        if (!insects.contains(insect)) {
            log.askQ("insects not contains insect", false);
            log.stepIn("insect.setDecrease(true)");
            insect.setDecrease(true);
            log.stepOut( "insect.setDecrease(true)", null);
            log.stepIn("insects.add(insect)");
            log.stepOut("insects.add(insect)", insects.add(insect));
        }else log.askQ("insects contains insect", false);
    }  
    /**
     * Removes an insect from the DecreasingTekton.
     * 
     * This method first checks if the insect is already present in the list
     * of insects. If so, it sets the insect's onDecreasing flag to false and
     * removes the insect from the list.
     * 
     * @param insect the Insect object to be removed from the DecreasingTekton.
     */
    @Override
    public void removeInsect(Insect insect){
        if (!insects.contains(insect) || insects.isEmpty()) {
            log.askQ("'insects' is empty or it not contains param:insect", false);
            return;
        }
        log.stepIn("insect.setDecrease(false)");
        insect.setDecrease(false);
        log.stepOut("insect.setDecrease(false)", null);
        
        log.stepIn("insects.remove(insect)");
        log.stepOut("insects.remove(insect)",insects.remove(insect));
    }    
}
