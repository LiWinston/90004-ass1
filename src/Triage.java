/**
 * Represents a triage area where patients are initially assessed and prioritized for treatment.
 * Implements the Movable interface to manage patient movement within the emergency department.
 *
 * @author yongchunl@student.unimelb.edu.au
 */
public class Triage implements Movable {

    private Patient patient;

    /**
     * Checks if the triage area is accessible (not occupied by a patient).
     *
     * @return true if the triage area is not occupied, otherwise false
     */
    @Override
    public boolean isAccessible() {
        return patient == null;
    }

    /**
     * Allows a patient to enter the triage area and undergo initial assessment.
     *
     * @param patient the patient entering the triage area
     */
    @Override
    public synchronized void enter(Patient patient) {
        this.patient = patient;
        patient.setLocation(this);
        Logger.getInstance().log(patient, " enters triage.");
        try {
            Thread.sleep(Params.TRIAGE_TIME);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Allows a patient to leave the triage area after initial assessment.
     *
     * @param patient the patient leaving the triage area
     */
    @Override
    public void leave(Patient patient) {
        patient.setLocation(null);
        this.patient = null;
        Logger.getInstance().log(patient, " leaves triage.");
    }
}
