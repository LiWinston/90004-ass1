import java.util.Objects;

/**
 * Represents the foyer area in a medical facility.
 * Implements the Movable interface.
 *
 * @author yongchunl@student.unimelb.edu.au
 */
public class Foyer implements Movable {

    /**
     * The space for patient arriving at the foyer.
     */
    private volatile Patient arrivingPatient;

    /**
     * The space for patient discharging from the foyer.
     */
    private volatile Patient departingPatient;

    /**
     * Gets the arriving patient.
     *
     * @return The arriving patient.
     */
    public Patient getArrivingPatient() {
        return arrivingPatient;
    }

    /**
     * Sets the arriving patient.
     *
     * @param patient The arriving patient.
     */
    public void setArrivingPatient(Patient patient) {
        arrivingPatient = patient;
    }

    /**
     * Discharge the patient from the emergency department (ED).
     */
    public synchronized void departFromED() {
        if (departingPatient != null) {
            Logger.getInstance().log(departingPatient, " discharged from ED.");
            departingPatient = null;
        }
    }

    /**
     * Admits a patient to the emergency department (ED).
     *
     * @param patient The patient to be admitted.
     */
    public synchronized void admitToEd(Patient patient) {
//        if (arrivingPatient == null) {
//            Logger.getInstance().log(patient, " admitted to ED.");
//            arrivingPatient = patient;
//            patient.setLocation(this);
//        }
        synchronized (this) {
            if (arrivingPatient == null) {
                Logger.getInstance().log(patient, " admitted to ED.");
                arrivingPatient = patient;
                patient.setLocation(this);
            }
            notifyAll();
        }
    }

    /**
     * Checks if the entry to the foyer is available.
     *
     * @return True if the entry is available, otherwise false.
     */
    public synchronized boolean isEntryAvailable() {
        return arrivingPatient == null;
    }

    /**
     * Checks if the exit from the foyer is available.
     *
     * @return True if the exit is available, otherwise false.
     */
    public synchronized boolean isExitAvailable() {
        return departingPatient == null;
    }

    /**
     * Sets the departing patient.
     *
     * @param patient The departing patient.
     */
    public void setDepartingPatient(Patient patient) {
        departingPatient = patient;
    }

    @Override
    public boolean isAccessible() {
        return departingPatient == null;
    }

    @Override
    public synchronized void enter(Patient patient) {
        synchronized (this) {
            if (departingPatient == null) {
                departingPatient = patient;
                patient.setLocation(this);
                Logger.getInstance().log(patient, " enters Foyer.");
                patient.getNurse().deallocatePatient(departingPatient);
            }
            notify();
        }
    }

    @Override
    public void leave(Patient patient) {
        synchronized (this) {
            if (Objects.equals(patient, arrivingPatient)) {
                arrivingPatient = null;
                notifyAll();
                patient.setLocation(null);
                Logger.getInstance().log(patient, " leaves Foyer.");
            } else {
                System.out.println("### WARNING: Patient " + patient.getId() + " to leave is not in the foyer.");
            }
        }
    }
}
