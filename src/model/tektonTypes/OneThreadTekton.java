package tektonTypes;

import fungus.FungusThread;
import utils.Logger;

public class OneThreadTekton extends Tekton {
    public OneThreadTekton() {
        super(true, true);
        log = Logger.getLogger("OneThreadTektonLogger");
    }

    /**
     * Adds a FungusThread instance to the list of threads associated with this OneThreadTekton.
     * If the OneThreadTekton has no thread yet, it will be added and the canGrowThread flag will be set to false.
     * If the OneThreadTekton already has a thread, the new thread will not be added and a log message is printed.
     * @param f the FungusThread instance to add to the list of associated threads.
     */
    @Override
    public void addThread(FungusThread f) {
        if (canGrowThread() || getThreads().contains(f.getPrev())) {
            getThreads().add(f);
            if (canGrowThread()) {
                setGrowThread(false);
            }
        } else
            log.askQ("OneThreadTekton already has a thread on it.", false);
    }
}