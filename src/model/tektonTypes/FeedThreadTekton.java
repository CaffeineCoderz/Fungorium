package tektonTypes;

import fungus.FungusThread;
import utils.Logger;

import java.io.Serializable;

public class FeedThreadTekton extends Tekton implements Serializable {
    private static final long serialVersionUID = 1L;
    private static Logger log;

    public FeedThreadTekton() {
        super(true, true);
    }

    public FeedThreadTekton(Boolean canGrowBody, Boolean canGrowThread) {
        super(canGrowBody, canGrowThread);
    }

    /**
     * Checks if a thread is connected to a FungusBody.
     * @param thread The thread to check.
     * @return true if the thread is connected to a FungusBody, false otherwise.
     */
    private boolean isThreadConnectedToBody(FungusThread thread) {
        boolean connected = body != null && body.getThreads().contains(thread);
        return connected;
    }

    @Override
    public void removeThread(FungusThread thread){ 
        if (thread.getLifeSpan()==0) {
            threads.remove(thread);
        }
    }
}
