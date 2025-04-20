package tektonTypes;

import insect.Insect;
import utils.Logger;

public class DecreasingTekton extends Tekton{

    public DecreasingTekton(){
        super(true, true);
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
            insect.setDecrease(true);
            insects.add(insect);
        }else System.err.println("Insect is already on the tekton!");
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
            System.err.println("There are no insects on the Tekton or it does not contain the insect");
            return;
        }
        insect.setDecrease(false);
        insects.remove(insect);
    }    
}
