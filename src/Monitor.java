
public class Monitor {

    //TODO: protect against starvation

    public boolean[] chopSticks;
    public boolean silence;

    public Monitor(int numberOfPhilosophers) {
        chopSticks = new boolean[numberOfPhilosophers];
        for (int i = 0; i < chopSticks.length; i++) {
            chopSticks[i] = true;
        }
    }

    /**
     * Grants request (returns) to eat when both chopsticks/forks are available.
     * Else forces the philosopher to wait()
     */
    public synchronized void pickUp(final int philosopherId) throws InterruptedException {
        if (philosopherId == 1) {
            while (!(chopSticks[chopSticks.length - 1] && chopSticks[0])) {
                wait();
            }
            chopSticks[chopSticks.length - 1] = false;
            chopSticks[0] = false;
        } else {
            while (!(chopSticks[philosopherId - 1] && chopSticks[philosopherId - 2])) {
                wait();
            }
            chopSticks[philosopherId - 1] = false;
            chopSticks[philosopherId - 2] = false;
        }

    }

    /**
     * When a given philosopher's done eating, they put the chopstiks/forks down
     * and let others know they are available.
     */
    public synchronized void putDown(final int philosopherId) {
        if (philosopherId == 1) {
            chopSticks[chopSticks.length - 1] = true;
            chopSticks[0] = true;
            notify();
        } else {
            chopSticks[philosopherId - 1] = true;
            chopSticks[philosopherId - 2] = true;
            notify();
        }
    }

    /**
     * Only one philopher at a time is allowed to philosophy
     * (while she is not eating).
     */
    public synchronized void requestTalk() throws InterruptedException {
        while(!silence){
            wait();
        }
        silence = false;
    }

    /**
     * When one philosopher is done talking stuff, others
     * can feel free to start talking.
     */
    public synchronized void endTalk() {
        silence = true;
        notify();
    }
}
