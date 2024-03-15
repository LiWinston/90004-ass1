public class Foyer {
    Patient ArrivingPatient;
    Patient DepartingPatient;

    public void departFromED() {
        if(DepartingPatient != null) {
            DepartingPatient = null;
        }
    }

    public void arriveAtED(Patient patient) {
    }
}
