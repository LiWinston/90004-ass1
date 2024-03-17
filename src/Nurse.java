public class Nurse extends Thread {
    private int nurseId;
    private Foyer foyer;
    private Triage triage;
    private Orderlies orderlies;
    private Treatment treatment;

    private boolean allocated = false;

    public Nurse(int i, Foyer foyer, Triage triage, Orderlies orderlies, Treatment treatment) {
        this.nurseId = i;
        this.foyer = foyer;
        this.triage = triage;
        this.orderlies = orderlies;
        this.treatment = treatment;
        this.allocated = false;
    }

    @Override
    public void run() {
        super.run();
        while (!isInterrupted()) {
            synchronized (this) {
                while (allocated && !isInterrupted()) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
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
            }
        }
    }

    public int getNurseId() {
        return nurseId;
    }

    //TODO: question: What on earth should be locked?
    public void allocatePatient(Patient patient) {
        synchronized (this) {
//            patient.allocated = true;
            patient.setNurse(this);
            allocated = true;
            String severe = patient.Severe() ? " (S)" : "";
            Logger.getInstance().log("Nurse " + nurseId + " allocated to Patient " + patient.getId() + severe + ".");
//            /*
//            Testing purpose
//             Only for continuous adding and removing patients inside the foyer
//             TODO: remove this part
//             */
//            foyer.setArrivingPatient(null);
//            foyer.setDepartingPatient(patient);
//            /*
//            Testing purpose
//             Only for continuous adding and removing patients inside the foyer
//             TODO: remove this part
//             */ //end

        }
    }

    public void deallocatePatient(Patient patient) {
        synchronized (this) {
//            patient.allocated = false;
            allocated = false;
//            patient.setNurse(null);
// We don't need to set the nurse to null, else the patient will be allocated to another nurse,they just need to be departed from ED directly
            Logger.getInstance().log("Nurse " + nurseId + " deallocated from Patient " + patient.getId() + ".");
            notifyAll();
        }
    }
}
