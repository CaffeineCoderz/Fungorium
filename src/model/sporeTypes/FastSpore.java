package sporeTypes;

import insect.Insect;

public class FastSpore extends Spore{
    
    @Override
    public void consume(Insect insect){
        insect.fast();
        absorbed();
    }
}
