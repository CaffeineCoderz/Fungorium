package tektonTypes;

import fungus.FungusBody;

public class OnlyThreadTekton extends Tekton{

    public OnlyThreadTekton() {
        super(false, true);
    }
    
    /**
     * Does nothing, as OnlyThreadTekton should not add bodies.
     * 
     * @param fb the FungusBody to be set, which is ignored.
     */
    @Override
    public void setBody(FungusBody fb) {
        // Do nothing, as OnlyThreadTekton should not add bodies
    }
}
