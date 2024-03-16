public class Nurse extends Thread {
    private int nurseId;
    private Foyer foyer;
    private Triage triage;
    private Orderlies orderlies;
    private Treatment treatment;

    public Nurse(int i, Foyer foyer, Triage triage, Orderlies orderlies, Treatment treatment) {
        this.nurseId = i;
        this.foyer = foyer;
        this.triage = triage;
        this.orderlies = orderlies;
        this.treatment = treatment;
    }

    @Override
    public void run() {
        super.run();
        while (!isInterrupted()) {
            // allocate a patient to the nurse
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

    public int getNurseId() {
        return nurseId;
    }

    public void allocatePatient(Patient patient) {
        synchronized (this) {
            patient.allocated = true;
            String severe = patient.Severe() ? " (S)" : "";
            Logger.getInstance().log("Nurse " + nurseId + " allocated to Patient " + patient.getId() + severe + ".");
        }
    }
}
