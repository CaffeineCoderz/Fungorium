package tektonTypes;

import fungus.FungusThread;
import utils.Logger;

public class FeedThreadTekton extends Tekton {

    private static Logger log;

    public FeedThreadTekton() {
        super(true, true);
        log = Logger.getLogger("FeedThreadTektonLogger");
    }

    public FeedThreadTekton(Boolean canGrowBody, Boolean canGrowThread) {
        super(canGrowBody, canGrowThread);
        log = Logger.getLogger("FeedThreadTektonLogger");
    }

    /**
     * Checks if a thread is connected to a FungusBody.
     * @param thread The thread to check.
     * @return true if the thread is connected to a FungusBody, false otherwise.
     */
    private boolean isThreadConnectedToBody(FungusThread thread) {
        log.stepIn("isThreadConnectedToBody(thread: " + thread + ")");
        boolean connected = body != null && body.getThreads().contains(thread);
        log.stepOut("isThreadConnectedToBody()", connected);
        return connected;
    }

    @Override
    public void removeThread(FungusThread thread){ 
        if (thread.getLifeSpan()==0) {
            threads.remove(thread);
        }
    }
}
