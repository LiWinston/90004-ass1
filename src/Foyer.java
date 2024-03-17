
import java.util.Objects;

public class Foyer implements Movable {
    public Patient getArrivingPatient() {
        return ArrivingPatient;
    }

    volatile private Patient ArrivingPatient;
    volatile private Patient DepartingPatient;

    public synchronized void departFromED() {
        if(DepartingPatient != null) {
//            DepartingPatient.getNurse().deallocatePatient(DepartingPatient);
            Logger.getInstance().log(DepartingPatient, " departed from ED.");
            DepartingPatient = null;
        }
    }

    public synchronized void admitToEd(Patient patient) {
        //patient from outside to ED -- referred as admitToEd()
        if(ArrivingPatient == null) {
            Logger.getInstance().log(patient, " admitted to ED.");
            ArrivingPatient = patient;
            patient.setLocation(this);
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
        //patient from inside ED to Foyer -- referred as enter()
        synchronized (this) {
            if (DepartingPatient == null) {
                DepartingPatient = patient;
                patient.setLocation(this);
                Logger.getInstance().log(patient, " enters Foyer.");
                //patient returning doesn't need to be accompanied by a nurse, weather the patient is severe or not
                patient.getNurse().deallocatePatient(DepartingPatient);
            }
        }
    }

    @Override
    public void leave(Patient patient) {
        if(Objects.equals(patient, ArrivingPatient)) {
            ArrivingPatient = null;
            patient.setLocation(null);
            Logger.getInstance().log(patient, " leaves Foyer.");
        }else{
            System.out.println("### WARNING: Patient " + patient.getId() + " to leave is not in the foyer.");
        }
    }
}
