import java.util.Arrays;
import java.util.Collections;

public class Monitor {

    //TODO: protect against starvation

    public boolean[] chopSticks;
    public boolean silence;
    public int[] philosopherEatCount;

    public Monitor(int numberOfPhilosophers) {
        chopSticks = new boolean[numberOfPhilosophers];
        philosopherEatCount = new int[numberOfPhilosophers];

        for (int i = 0; i < chopSticks.length; i++) {
            chopSticks[i] = true;
        }
        for (int j= 0; j < philosopherEatCount.length; j++) {
            philosopherEatCount[j] = 0;
        }
        silence = true;
    }

    public synchronized void pickUp(final int philosopherId) throws InterruptedException {
        if (isAPhilosphereStarving()) {
               if(philosopherId != getStarvingPhilosopherId()){
                   while(isAPhilosphereStarving()) {
                       wait();
                   }
               }
               pickupChopSticksFromTable(philosopherId);
        } else {
           pickupChopSticksFromTable(philosopherId);
        }
    }

    public synchronized void putDown(final int philosopherId) {
        if (philosopherId == 1) {
            chopSticks[chopSticks.length - 1] = true;
            chopSticks[0] = true;
            philosopherEatCount[philosopherId-1]++;
            notify();
        } else {
            chopSticks[philosopherId - 1] = true;
            chopSticks[philosopherId - 2] = true;
            philosopherEatCount[philosopherId-1]++;
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

    private boolean isAPhilosphereStarving() {
       double average = (Arrays.stream(philosopherEatCount).average().getAsDouble());
       int philosopherCount = philosopherEatCount.length;
       return !(average == 0) && !(average < 2 * philosopherCount) && Arrays.stream(philosopherEatCount).anyMatch(count -> count < 2 * average);
    }

    private int getStarvingPhilosopherId(){
        return Arrays.asList(philosopherEatCount).indexOf(Arrays.stream(philosopherEatCount).max().getAsInt())+1;
    }

    private void pickupChopSticksFromTable(int philosopherId) throws InterruptedException {
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
}
