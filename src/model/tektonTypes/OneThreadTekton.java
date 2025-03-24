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
            log.askQ("tekton has no thread", false);
            log.stepIn("getThreads().add(f)");
            getThreads().add(f);
            log.stepOut("getThreads().add(f)", null);
            if (canGrowThread()) {
                log.askQ("Can grow thread", false);
                log.stepIn("setGrowThread(false)");
                setGrowThread(false);
                log.stepOut("setGrowThread(false)", null);
            }
        } else
            log.askQ("tekton already has a thread", false);
    }
}