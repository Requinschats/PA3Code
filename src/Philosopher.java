import common.BaseThread;

public class Philosopher extends BaseThread {

	public static final long MAXIMUM_ACTION_TIME = 1000;

	public void eat() {
		System.out.println(this.getTID() + " has started eating");
		try {
			sleep((long)(Math.random() * MAXIMUM_ACTION_TIME));
		}
		catch(InterruptedException e) {
			System.err.println("Philosopher.eat():");
			DiningPhilosophers.reportException(e);
			System.exit(1);
		}
        System.out.println(this.getTID() + " has finished eating");
	}

	public void think() {
        System.out.println(this.getTID() + " has started thinking");
        try {
            sleep((long)(Math.random() * MAXIMUM_ACTION_TIME));
        }
        catch(InterruptedException e) {
            System.err.println("Philosopher.think:");
            DiningPhilosophers.reportException(e);
            System.exit(1);
        }
        System.out.println(this.getTID() + " has finished thinking");
	}

	public void talk(){
        System.out.println(this.getTID() + " has started talking");
		saySomething();
        System.out.println(this.getTID() + " has finished talking");
	}

	public void run() {
		for(int i = 0; i < DiningPhilosophers.DINING_STEPS; i++) {
			try {
				DiningPhilosophers.monitor.pickUp(getTID(), i);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			eat();
			DiningPhilosophers.monitor.putDown(getTID());
			think();
			if(Math.random() < 0.5) {
				try {
					DiningPhilosophers.monitor.requestTalk();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				talk();
				DiningPhilosophers.monitor.endTalk();
			}
		}
	}

	private void saySomething() {
		String[] astrPhrases = {
			"Eh, it's not easy to be a philosopher: eat, think, talk, eat...",
			"You know, true is false and false is true if you think of it",
			"2 + 2 = 5 for extremely large values of 2...",
			"If thee cannot speak, thee must be silent",
			"My number is " + getTID() + ""
		};
		System.out.println("Philosopher " + getTID() + " says: " + astrPhrases[(int)(Math.random() * astrPhrases.length)]);
	}
}