public class Foyer {
    volatile private Patient ArrivingPatient;
    volatile private Patient DepartingPatient;

    public synchronized void departFromED() {
        if(DepartingPatient != null) {
            if(DepartingPatient.Severe()) {
                Logger.getInstance().log("Patient " + DepartingPatient.getId() + " (S) discharged from ED.");
            } else {
                Logger.getInstance().log("Patient " + DepartingPatient.getId() + " discharged from ED.");
            }
            DepartingPatient = null;
        }
    }

    public synchronized void arriveAtED(Patient patient) {
        if(ArrivingPatient == null) {
            ArrivingPatient = patient;
            if(patient.Severe()) {
                Logger.getInstance().log("Patient " + patient.getId() + " (S) admitted to ED.");
            } else {
                Logger.getInstance().log("Patient " + patient.getId() + " admitted to ED.");
            }
        }
    }

    public synchronized boolean isEntryAvailable() {
        return ArrivingPatient == null;
    }

    public synchronized boolean isExitAvailable() {
        return DepartingPatient == null;
    }
}
