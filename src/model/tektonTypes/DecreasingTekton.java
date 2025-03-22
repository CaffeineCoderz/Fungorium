package tektonTypes;

import insect.Insect;

public class DecreasingTekton extends Tekton{
     public DecreasingTekton(){
        super(true, true);
    }

    @Override
    public void addInsect(Insect insect){
        if (!insects.contains(insect)) {
            insect.setDecrease(true);
            insects.add(insect);
        }
    }  
    @Override
    public void removeInsect(Insect insect){
        if (!insects.contains(insect) || insects.isEmpty()) {
            return;
        }
        insect.setDecrease(false);
        insects.remove(insect);
    }    
}
