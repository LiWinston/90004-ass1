import java.util.Random;

/**
 * Represents a patient who presents to the emergency department, identified by a unique ID.
 * Patients may have varying levels of severity in their conditions.
 * Patients can be allocated to a nurse for treatment.
 *
 * @author ngeard@unimelb.edu.au
 * &#064;date 13 February 2024
 * modified by: yongchunl@student.unimelb.edu.au
 */

public class Patient {
    // the next ID to be allocated
    private static int nextId = 1;
    // a unique identifier for this patient
    private final int id;
    // a flag indicating whether a patient's condition is severe
    private final boolean severe;
    // a flag indicating whether a patient is allocated to a nurse
    protected volatile boolean allocated;
    // a flag indicating whether a patient has been treated
    protected volatile boolean treated;
    private volatile Movable location;
    private volatile Movable destination;
    private Nurse nurse;
    // private constructor to create a new patient with a given identifier
    private Patient(int id) {
        this.id = id;
//        Random random = new Random();
        this.severe = new Random().nextDouble() <= Params.SEVERE_PROPORTION;
        this.allocated = false;
        this.treated = false;
    }

    /**
     * Returns a new Patient instance with a unique identifier.
     *
     * @return a new Patient instance
     */
    public static Patient getNewPatient() {
        return new Patient(nextId++);
    }

    /**
     * Gets the current location of the patient.
     *
     * @return the current location of the patient
     */
    public Movable getLocation() {
        return location;
    }

    /**
     * Sets the location of the patient.
     *
     * @param location the location to set
     */
    public void setLocation(Movable location) {
        this.location = location;
    }

    /**
     * Gets the unique identifier of the patient.
     *
     * @return the patient's ID
     */
    public int getId() {
        return id;
    }

    public boolean Severe() {
        return this.severe;
    }

    // produce an identifying string for the patient
    public String toString() {
        String s = "Patient " + id;
        if (this.severe) {
            s = s + " (S)";
        }
        return s;
    }

    /**
     * Gets the nurse allocated to the patient.
     *
     * @return the nurse allocated to the patient
     */
    public Nurse getNurse() {
        return nurse;
    }

    /**
     * Sets the nurse allocated to the patient.
     *
     * @param nurse the nurse to allocate to the patient
     */
    public void setNurse(Nurse nurse) {
        synchronized (this) {
            if (!this.allocated) {
                this.nurse = nurse;
                this.allocated = true;
                notify();
            } else if (this.allocated && nurse == null) {
                this.nurse = null;
                this.allocated = false;
            } else {
                throw new IllegalStateException("Patient " + this.id + " has already been allocated to a nurse.");
            }
        }
    }

    /**
     * serves as a getter for the patient's destination and also have the side effect *:
     * set the patient's destination based on the patient's location and the patient's condition (refresher)
     * <p>
     * This method controls the patient's movement within the emergency department,
     * directing them to the appropriate location based on their condition and current location.
     *
     * @return the patient's destination
     */
    public synchronized Movable loadDestination() {
        // Centrally control the patient's movement on the basis of the patient's location and the patient's condition
        if (location == null) {
            return destination;
        }
        switch (location.getClass().getSimpleName()) {
            case "Foyer":
                destination = nurse.getTriage();
                break;
            case "Triage":
                destination = Severe() ? nurse.getTreatment() : nurse.getFoyer();
                break;
            case "Treatment":
                destination = nurse.getFoyer();
                break;
            default:
                throw new IllegalStateException("????");
        }
        return destination;
    }
}
