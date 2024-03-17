public class Specialist extends Thread {
    private final Treatment treatment;
    private boolean isAtTreatment;

    public Specialist(Treatment treatment) {
        this.treatment = treatment;
        this.isAtTreatment = true;
    }

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

    // Method to treat the patient
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

    // Method to leave the treatment room
    private synchronized void leaveTreatmentRoom() {
        // Log leaving treatment room
        Logger.getInstance().log("Specialist leaves treatment room.");
        isAtTreatment = false;
    }

    // Method to return to treatment room
    private synchronized void returnToTreatmentRoom() {
        // Log returning to treatment room
        Logger.getInstance().log("Specialist enters treatment room.");
        isAtTreatment = true;
    }
}
