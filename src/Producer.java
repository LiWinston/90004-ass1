/**
 * Produces new patients to present to the emergency department.
 *
 * @author ngeard@unimelb.edu.au
 * @date 13 February 2024
 *
 * Yongchunli 1378156
 */

public class Producer extends Thread {

    private final Foyer foyer;

    // create a new producer
    Producer(Foyer newFoyer) {
        this.foyer = newFoyer;
    }

    // quests 
    public void run() {
        while (!isInterrupted()) {
            try {
                // create a new patient and admit it to the ED.
                Patient patient = Patient.getNewPatient();
                this.foyer.admitToEd(patient);

                // let some time pass before the next patient is admitted
                sleep(Params.arrivalPause());
            } catch (InterruptedException e) {
                this.interrupt();
            }
        }
    }
}