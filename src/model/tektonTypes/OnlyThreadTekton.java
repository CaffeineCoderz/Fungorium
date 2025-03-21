package tektonTypes;

import fungus.FungusBody;

public class OnlyThreadTekton extends Tekton{

    public OnlyThreadTekton() {
        super(false, true);
    }
    
    @Override
    public void addBody(FungusBody fb) {
        // Do nothing, as OnlyThreadTekton should not add bodies
    }
}
