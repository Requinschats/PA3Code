package common;

import java.util.Random;

public class BaseThread extends Thread {
    public static int siNextTID = 1;
    protected int iTID;
    private static int siTurn = 1;

    public BaseThread() {
        setTID();
    }

    public BaseThread(ThreadGroup threadGroup, String threadName) {
        super(threadGroup, threadName);
        setTID();
    }

    public BaseThread(final int piTID) {
        this.iTID = piTID;
    }

    public final int getTID() {
        return this.iTID;
    }

    private final void setTID() {
        this.iTID = siNextTID++;
    }

    protected synchronized void phase1() {
        System.out.println(this.getClass().getName() + " thread [TID=" + this.iTID + "] starts PHASE I.");
        System.out.println("Some stats info in the PHASE I:\n" + "    iTID = " + this.iTID + ", siNextTID = " + siNextTID + ", siTurn = " + siTurn + ".\n    Their \"checksum\": " + (siNextTID * 100 + this.iTID * 10 + siTurn));
        System.out.println(this.getClass().getName() + " thread [TID=" + this.iTID + "] finishes PHASE I.");
    }

    protected synchronized void phase2() {
        System.out.println(this.getClass().getName() + " thread [TID=" + this.iTID + "] starts PHASE II.");
        System.out.println("Some stats info in the PHASE II:\n" + "    iTID = " + this.iTID + ", siNextTID = " + siNextTID + ", siTurn = " + siTurn + ".\n    Their \"checksum\": " + (siNextTID * 100 + this.iTID * 10 + siTurn));
        System.out.println(this.getClass().getName() + " thread [TID=" + this.iTID + "] finishes PHASE II.");
    }

    public synchronized boolean turnTestAndSet(boolean areThreadsInIncreasingOrder) {
        if (siTurn == this.iTID) {
            siTurn = areThreadsInIncreasingOrder ? siTurn +1 : siTurn-1;
            return true;
        }
        return false;
    }

     // Assumes the increasing order
    public synchronized boolean turnTestAndSet() {
        return turnTestAndSet(true);
    }

    public static void setInitialTurn(int piTurn) {
        siTurn = piTurn;
    }

    public static void setInitialTurnAscending() {
        setInitialTurn(1);
    }

    public static void setInitialTurnDescending() {
        setInitialTurn(siNextTID - 1);
    }

     // Must NOT be atomic
    public void randomYield() {
        // Generate from 5 to 40 yield()
        int iNumYields = (int) ((new Random()).nextFloat() * 35) + 5;
        for (int i = 0; i < iNumYields; i++)
            yield();
    }
}
