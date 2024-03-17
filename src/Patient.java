import java.util.Random;

/**
 * A patient, with a unique id, who will present to the emergency department.
 *
 * @author ngeard@unimelb.edu.au
 * &#064;date 13 February 2024
 * implemented by: Yongchunli 1378156
 */

public class Patient {
    private Nurse nurse;

    public int getId() {
        return id;
    }

    // a unique identifier for this patient
    private final int id;

    // a flag indicating whether a patient is allocated to a nurse
    protected volatile boolean allocated;

    // a flag indicating whether a patient's condition is severe
    private final boolean severe;

    // a flag indicating whether a patient has been treated
    protected volatile boolean treated;

    // the next ID to be allocated
    private static int nextId = 1;

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

    public Nurse getNurse() {
        return nurse;
    }
}
