package sporeTypes;

import insect.Insect;

public class SlowSpore extends Spore{
    
    @Override
    public void consume(Insect insect){
        insect.slow();
        absorbed();
    }
}
