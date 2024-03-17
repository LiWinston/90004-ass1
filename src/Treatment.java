public class Treatment implements Movable {
    public Patient getPatient() {
        return patient;
    }

    private Patient patient;

    @Override
    public boolean isAccessible() {
        return patient == null;
    }

    @Override
    public synchronized void enter(Patient patient) {
        patient.setLocation(this);
        this.patient = patient;
        Logger.getInstance().log(patient, "  enters treatment room.");
    }

    @Override
    public void leave(Patient patient) {
        patient.setLocation(null);
        this.patient = null;
        Logger.getInstance().log(patient, " leaves treatment room.");
    }
}
