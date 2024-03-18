/**
 * Represents a treatment room where patients receive specialized medical treatment.
 * Implements the Movable interface to manage patient movement within the emergency department.
 *
 * @author yongchunl@student.unimelb.edu.au
 */
public class Treatment implements Movable {
    private Patient patient;

    /**
     * Gets the patient currently in the treatment room.
     *
     * @return the patient in the treatment room, or null if no patient is present
     */
    public Patient getPatient() {
        return patient;
    }

    /**
     * Checks if the treatment room is accessible (not occupied by a patient).
     *
     * @return true if the treatment room is not occupied, otherwise false
     */
    @Override
    public boolean isAccessible() {
        return patient == null;
    }

    /**
     * Allows a patient to enter the treatment room.
     *
     * @param patient the patient entering the treatment room
     */
    @Override
    public synchronized void enter(Patient patient) {
        synchronized (this) {
            patient.setLocation(this);
            this.patient = patient;
            Logger.getInstance().log(patient, " enters treatment room.");
            notifyAll();
        }
    }

    /**
     * Allows a patient to leave the treatment room.
     *
     * @param patient the patient leaving the treatment room
     */
    @Override
    public void leave(Patient patient) {
        patient.setLocation(null);
        this.patient = null;
        Logger.getInstance().log(patient, " leaves treatment room.");
    }
}
