public class DiningPhilosophers {
	public static final int DEFAULT_NUMBER_OF_PHILOSOPHERS = 4;
	public static final int DINING_STEPS = 5;
	public static Monitor monitor = null;

	public static void main(String[] argv) {
		try {
			// TODO: Should be settable from the command line
			monitor = new Monitor(DEFAULT_NUMBER_OF_PHILOSOPHERS);
			Philosopher philosophers[] = new Philosopher[DEFAULT_NUMBER_OF_PHILOSOPHERS];

			philosophersSitDown(philosophers);
			philosophersFinishDinner(philosophers);


		} catch(InterruptedException e) {
			System.err.println("main():");
			reportException(e);
			System.exit(1);
		}
	}

	public static void reportException(Exception poException) {
		System.err.println("Caught exception : " + poException.getClass().getName());
		System.err.println("Message          : " + poException.getMessage());
		System.err.println("Stack Trace      : ");
		poException.printStackTrace(System.err);
	}

	private static void philosophersSitDown(Philosopher philosophers[]){
        for(int j = 0; j < DEFAULT_NUMBER_OF_PHILOSOPHERS; j++) {
            philosophers[j] = new Philosopher();
            philosophers[j].start();
        }
        System.out.println(DEFAULT_NUMBER_OF_PHILOSOPHERS + " philosopher(s) came in for a dinner.");
    }

    private static void philosophersFinishDinner(Philosopher philosophers[]) throws InterruptedException{
        for(int j = 0; j < DEFAULT_NUMBER_OF_PHILOSOPHERS; j++) {
            philosophers[j].join();
        }
        System.out.println("All philosophers have left. System terminates normally.");
    }
}