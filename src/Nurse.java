public class Nurse extends Thread {
    private final int nurseId;
    private final Foyer foyer;
    private final Triage triage;
    private final Orderlies orderlies;
    private final Treatment treatment;
    private Patient patient;
    private boolean allocated = false;

    public Nurse(int i, Foyer foyer, Triage triage, Orderlies orderlies, Treatment treatment) {
        this.nurseId = i;
        this.foyer = foyer;
        this.triage = triage;
        this.orderlies = orderlies;
        this.treatment = treatment;
        this.allocated = false;
    }

    public Foyer getFoyer() {
        return foyer;
    }

    public Triage getTriage() {
        return triage;
    }

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
                            //Cant leave before assistance from orderlies is available
                            orderlies.recruitOrderlies(this, Params.TRANSFER_ORDERLIES);
                            //leave the current location first is okay, according to ED discussion
                            //String dst = patient.loadDestination().getClass().getSimpleName();
                            //Logger.getInstance().log("➡️-- patient " + patient.getId() + " is HEADING to " + dst);
                            patient.loadDestination();
                            patient.getLocation().leave(patient);

                            if (patient.loadDestination().isAccessible()) {
                                //Logger.getInstance().log("Nurse " + nurseId + " is transferring Patient " + patient.getId() + " to " + patient.loadDestination().getClass().getSimpleName() + ".");

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

    public int getNurseId() {
        return nurseId;
    }

    //TODO: question: What on earth should be locked?
    public void allocatePatient(Patient patient) {
        synchronized (patient) {
//            patient.allocated = true;
            patient.setNurse(this);
            this.setPatient(patient);
            allocated = true;
            String severe = patient.Severe() ? " (S)" : "";
            Logger.getInstance().log("Nurse " + nurseId + " allocated to Patient " + patient.getId() + severe + ".");
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

    private void setPatient(Patient patient) {
        synchronized (this) {
            this.patient = patient;
            notifyAll();
        }
    }

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
