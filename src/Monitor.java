import java.util.Arrays;

public class Monitor {

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

    public synchronized void pickUp(final int philosopherId, final int eatRound) throws InterruptedException {
        if (starvingPhilosphersIds().length > 0) {
            System.out.println(Arrays.toString(philosopherEatCount));
            System.out.println("Somebody(s) is/are STARVING");
               if(philosopherEatCount[philosopherId-1]){
                   while(starvingPhilosphersIds().length > 0) { //no one can request pickup while a philosopher is starving
                       wait();
                       System.out.println(philosopherId + " is waiting for " + getStarvingPhilosopherId() + "to eat");
                   }
                   System.out.println("nobody is starving anymore");
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

    private int[] starvingPhilosphersIds() {
       long average = Math.round(Arrays.stream(philosopherEatCount).average().getAsDouble());
       if(!(average == 0) && !(average < 2) && Arrays.stream(philosopherEatCount).findFirst(count -> (count+1) < average)){
           return Arrays.stream(philosopherEatCount).filter(count -> (count+1) < average).toArray();
       } else {
           return new int[0];
       }
    }

    private int getStarvingPhilosopherId(){
        for(int i=0; i<philosopherEatCount.length;i++){
            if(philosopherEatCount[i] == min(philosopherEatCount)){
                return i+1;
            }
        }
        return -1;
    }

    public int min(int [] array) {
        int min = array[0];
        for(int i=0; i<array.length; i++ ) {
            if(array[i]<min) {
                min = array[i];
            }
        }
        return min;
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
