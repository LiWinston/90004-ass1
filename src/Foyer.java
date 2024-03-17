public class Foyer {
    public Patient getArrivingPatient() {
        return ArrivingPatient;
    }

    volatile private Patient ArrivingPatient;
    volatile private Patient DepartingPatient;

    public synchronized void departFromED() {
        if(DepartingPatient != null) {
            DepartingPatient.getNurse().deallocatePatient(DepartingPatient);
            Logger.getInstance().log(DepartingPatient, " departed from ED.");
            DepartingPatient = null;
        }
    }

    public synchronized void arriveAtED(Patient patient) {
        if(ArrivingPatient == null) {
            Logger.getInstance().log(patient, " admitted to ED.");
            ArrivingPatient = patient;
        }
    }

    public synchronized boolean isEntryAvailable() {
        return ArrivingPatient == null;
    }

    public synchronized boolean isExitAvailable() {
        return DepartingPatient == null;
    }

    public void setDepartingPatient(Patient patient) {
        DepartingPatient = patient;
    }

    public void setArrivingPatient(Patient patient) {
        ArrivingPatient = patient;
    }
}
