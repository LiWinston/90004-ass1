
import java.util.Objects;

public class Foyer implements Movable {
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

    public synchronized void admitToEd(Patient patient) {
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

    @Override
    public boolean isAccessible() {
        return DepartingPatient == null;
    }

    @Override
    public void enter(Patient patient) {
        synchronized (this) {
            if (ArrivingPatient == null) {
                ArrivingPatient = patient;
                Logger.getInstance().log(ArrivingPatient, " enters Foyer.");
            }
        }
    }

    @Override
    public void leave(Patient patient) {
        if(Objects.equals(patient, ArrivingPatient)) {
            ArrivingPatient = null;
        }else{
            System.out.println("### WARNING: Patient " + patient.getId() + " to leave is not in the foyer.");
        }
        Logger.getInstance().log(DepartingPatient, " leaves Foyer.");
    }
}
