/**
 * Represents a specialist who performs treatment procedures on patients in a treatment room.
 * The specialist periodically treats patients and alternates between being in the treatment room and being away.
 * @author yongchunl@student.unimelb.edu.au
 * @version 1.0
 * @since 2024-03-18

 */
public class Specialist extends Thread {
    private final Treatment treatment;
    private boolean isAtTreatment;

    /**
     * Constructs a specialist with the specified treatment room.
     *
     * @param treatment the treatment room where the specialist performs treatments
     */
    public Specialist(Treatment treatment) {
        this.treatment = treatment;
        this.isAtTreatment = true;
    }

    /**
     * Executes the specialist's treatment routine, alternating between treating patients and being away.
     */
    @Override
    public void run() {
        super.run();
        while (!isInterrupted()) {
            try {
                // Check if there is a patient in treatment
                if (isAtTreatment) {
                    if (null == treatment.getPatient()) {
                        continue;
                    }
                    // Treat the patient
                    synchronized (treatment.getPatient()) {
                        treatPatient();
                    }
                    // Leave the treatment room
                    leaveTreatmentRoom();
                }
                // Sleep for a specified period before returning to treatment
                sleep(Params.SPECIALIST_AWAY_TIME);
                // Return to the treatment room
                returnToTreatmentRoom();
            } catch (InterruptedException e) {
                // Handle interrupted exception
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    /**
     * Treats the patient in the treatment room.
     * This method simulates the specialist performing treatment operations on the patient.
     */
    private synchronized void treatPatient() {
        // Get the patient from the treatment room
        Patient patient = treatment.getPatient();
        // Treat the patient (perform treatment operations)
        // Assuming some treatment operations are performed here
        // Log treatment completion
        Logger.getInstance().log(patient, " treatment started.");
        // Sleep for treatment time
        try {
            sleep(Params.TREATMENT_TIME);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Logger.getInstance().log(patient, " treatment complete.");
    }

    /**
     * Leaves the treatment room after completing treatment.
     */
    private synchronized void leaveTreatmentRoom() {
        Logger.getInstance().log("Specialist leaves treatment room.");
        isAtTreatment = false;
    }

    /**
     * Returns to the treatment room after being away for a specified period.
     */
    private synchronized void returnToTreatmentRoom() {
        Logger.getInstance().log("Specialist enters treatment room.");
        isAtTreatment = true;
    }
}
