import java.util.Random;

/**
 * A patient, with a unique id, who will present to the emergency department.
 *
 * @author ngeard@unimelb.edu.au
 * &#064;date 13 February 2024
 * implemented by: Yongchunli 1378156
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
    // create a new patient with a given identifier
    private Patient(int id) {
        this.id = id;
//        Random random = new Random();
        this.severe = new Random().nextDouble() <= Params.SEVERE_PROPORTION;
        this.allocated = false;
        this.treated = false;
    }

    // get a new Patient instance with a unique identifier
    public static Patient getNewPatient() {
        return new Patient(nextId++);
    }

    public Movable getLocation() {
        return location;
    }

    public void setLocation(Movable location) {
        this.location = location;
    }

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

    public Nurse getNurse() {
        return nurse;
    }

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
     * Side effect: set the patient's destination based on the patient's location and the patient's condition (refresh)
     *
     * @return the patient's destination (used as getter)
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
