/**
 * The top-level component of the emergency department simulator.
 * <p>
 * It is responsible for:
 * - creating all the components of the system;
 * - starting all of the processes;
 *
 * @author ngeard@unimelb.edu.au
 * implemented by: Yongchunli 1378156
 */

public class Main {
    /**
     * 1. Only one Patient at a time can be in Triage.
     * 2. Only one Patient at a time can be in Treatment.
     * 3. A Patient must be allocated to a Nurse before they can leave the Foyer.
     * 4. A Patient is only ever allocated to one Nurse.
     * 5. A Nurse can only have one Patient allocated to them at a time.
     * 6. The Foyer may contain one newly admitted Patient and one Patient waiting to be discharged at the same time
     * (i.e., it is still possible for a new Patient to be admitted to the ED even if there is still a Patient waiting
     * to be discharged).
     * 7. The Specialist leaves the Treatment location in between treating each Patient.
     */
    public static void main(String[] args) throws InterruptedException {
        // set up the log
        Logger.getInstance().setConsoleOutputEnabled(true);
        Logger.getInstance().setFileOutputEnabled(true);

        // generate the system components
        Foyer foyer = new Foyer();
        Triage triage = new Triage();
        Treatment treatment = new Treatment();
        Orderlies orderlies = Orderlies.getInstance();
        Specialist specialist = new Specialist(treatment);

        // generate the producer and consumer processes
        Producer producer = new Producer(foyer);
        Consumer consumer = new Consumer(foyer);

        // create arrays of Nurses and Beds
        Nurse[] nurses = new Nurse[Params.NURSES];
//        Treatment[] beds = new Treatment[Params.NURSES];

        // generate and start the individual nurse processes
        for (int i = 0; i < Params.NURSES; i++) {
//        	beds[i] = new Treatment(i + 1);
//            nurses[i] = new Nurse(i + 1, foyer, triage, orderlies, beds[i]);
            nurses[i] = new Nurse(i + 1, foyer, triage, orderlies, treatment);
            nurses[i].start();
        }

        // start the remaining processes
        producer.start();
        consumer.start();
        specialist.start();

        // join all processes
        for (int i = 0; i < Params.NURSES; i++) {
            nurses[i].join();
        }
        producer.join();
        consumer.join();
        specialist.join();
    }
}