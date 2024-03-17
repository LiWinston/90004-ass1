import java.util.LinkedList;

/**
 * Produces new patients to present to the emergency department.
 *
 * @author ngeard@unimelb.edu.au
 * @date 13 February 2024
 * implemented by: Yongchunli 1378156
 *
 */

public class Producer extends Thread {

    private Foyer foyer;
    private LinkedList<Patient> patients = new LinkedList<Patient>();

    // create a new producer
    Producer(Foyer newFoyer) {
        this.foyer = newFoyer;
    }

    // quests
    public void run() {
        while (!isInterrupted()) {
            try {
                // create a new patient and add it to the queue
                Patient patient = Patient.getNewPatient();
                patients.addLast(patient);

                // try admitting patients from the queue
                tryAdmitPatients();

                // let some time pass before the next patient is admitted
                sleep(Params.arrivalPause());
            } catch (InterruptedException e) {
                this.interrupt();
            }
        }
    }

    // try admitting patients from the queue
    private void tryAdmitPatients() throws InterruptedException {
        synchronized (patients) {
            if (!patients.isEmpty()) {
                Patient nextPatient = patients.removeFirst();
                this.foyer.admitToEd(nextPatient);
            }
        }
    }
}
