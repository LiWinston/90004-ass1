/**
 * Represents a nurse as Thread in the emergency department.
 * <p>
 * Nurses are responsible for assisting patients and ensuring their well-being during their stay in the hospital.
 * They are allocated to patients and help them move between different locations within the hospital.
 * Nurses also work closely with other hospital staff, such as orderlies, to provide comprehensive care to patients.
 * <p>
 * This class extends the {@code java.lang.Thread} class
 * to allow nurses to run concurrently with other threads in the simulation.
 * Nurses will continuously check for arriving patients in the foyer and assist them if available.
 * When allocated to a patient, nurses will help transfer the patient to their next destination.
 * <p>
 * Note: This class is not thread-safe and should be used in a controlled environment to avoid race conditions.
 *
 * @author yongchunl@student.unimelb.edu.au
 * @version 1.0
 * @since 2024-03-16
 */
public class Nurse extends Thread {
    private final int nurseId;
    private final Foyer foyer;
    private final Triage triage;
    private final Orderlies orderlies;
    private final Treatment treatment;
    private Patient patient;
    private boolean allocated = false;

    /**
     * Constructs a nurse with the specified parameters.
     *
     * @param i         the unique identifier of the nurse
     * @param foyer     the foyer where patients arrive
     * @param triage    the triage area where patients are assessed
     * @param orderlies the orderlies who assist nurses
     * @param treatment the treatment area where patients receive care
     */
    public Nurse(int i, Foyer foyer, Triage triage, Orderlies orderlies, Treatment treatment) {
        this.nurseId = i;
        this.foyer = foyer;
        this.triage = triage;
        this.orderlies = orderlies;
        this.treatment = treatment;
        this.allocated = false;
    }

    /**
     * Returns the foyer associated with the nurse.
     *
     * @return the foyer where the nurse is stationed
     */
    public Foyer getFoyer() {
        return foyer;
    }

    /**
     * Returns the triage area associated with the nurse.
     *
     * @return the triage area where the nurse assesses patients
     */
    public Triage getTriage() {
        return triage;
    }

    /**
     * Returns the treatment area associated with the nurse.
     *
     * @return the treatment area where the nurse provides care to patients
     */
    public Treatment getTreatment() {
        return treatment;
    }

    @Override
    public void run() {
        super.run();
        while (!isInterrupted()) {
            synchronized (this) {
                //if the nurse is allocated, then conduct the duty routine
                while (allocated && !isInterrupted()) {
                    try {
                        //try moving the patient to the next destination
                        if (patient != null && patient.getLocation() != null) {
                            //Try to Employ the orderlies. TODO :Should here be synchronized or try{}catch{} again?
                            //Can‘t leave before assistance from orderlies is available
                            orderlies.recruitOrderlies(this, Params.TRANSFER_ORDERLIES);

                            //Refresh the destination before position change
                            patient.loadDestination();
                            //leave the current location prior to destination turning prepared is okay -- ED discussion
                            patient.getLocation().leave(patient);

                            if (patient.loadDestination().isAccessible()) {
                                //Logger.getInstance().log("Nurse " + nurseId + " is transferring Patient " + patient.getId() + " to " + patient.loadDestination().getClass().getSimpleName() + ".");
                                //Apply the transfer time
                                wait(Params.TRANSFER_TIME);
                                patient.loadDestination().enter(patient);
                                orderlies.releaseOrderlies(this);
                            }
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }

                //if the nurse is free, then try to help the patient arriving in the foyer
                Patient patient = foyer.getArrivingPatient();
                if (patient != null) {
                    synchronized (patient) {
                        if (patient.allocated) {
                            continue;
                        } else {
                            try {
                                allocatePatient(patient);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                //end of synchronized block
                notifyAll();
            }
        }
    }

    /**
     * Returns the unique identifier of the nurse.
     *
     * @return the nurse's unique identifier
     */
    public int getNurseId() {
        return nurseId;
    }

    /**
     * Allocates the nurse to a patient.
     *
     * @param patient the patient to allocate the nurse to
     */
    //TODO: question: What on earth should be locked?
    public void allocatePatient(Patient patient) {
        synchronized (patient) {
//            patient.allocated = true;
            patient.setNurse(this);
            this.setPatient(patient);
            allocated = true;
            Logger.getInstance().log(patient, " allocated to Nurse " + nurseId + ".");
//            /*
//            Testing purpose
//             Only for continuous adding and removing patients inside the foyer
//             TODO: remove this part
//             */
//            foyer.leave(patient);
//            try {
//                sleep(2000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            foyer.enter(patient);
//            /*
//            Testing purpose
//             Only for continuous adding and removing patients inside the foyer
//             TODO: remove this part
//             */ //end
            notifyAll();
        }
    }

    /**
     * Sets the patient associated with the nurse.
     * <p>
     * This method synchronizes access to the patient object to ensure thread safety.
     * Once the patient is set, it notifies all threads waiting on this nurse instance.
     *
     * @param patient the patient to be associated with the nurse
     */
    private void setPatient(Patient patient) {
        synchronized (this) {
            this.patient = patient;
            notifyAll();
        }
    }


    /**
     * Deallocates the nurse from a patient.
     *
     * @param patient the patient to deallocate the nurse from
     */
    public void deallocatePatient(Patient patient) {
        synchronized (this) {
//            patient.allocated = false;
            allocated = false;
            this.patient = null;
            //            patient.setNurse(null);
            // We don't need to set the nurse to null, else the patient will be allocated to another nurse,they just need to be departed from ED directly
            Logger.getInstance().log("Nurse " + nurseId + " deallocated from Patient " + patient.getId() + ".");
            notifyAll();
        }
    }
}
