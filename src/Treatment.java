public class Treatment implements Movable {
    public Patient getPatient() {
        return patient;
    }

    private Patient patient;

    @Override
    public boolean isAccessible() {
        return true;
    }

    @Override
    public void enter(Patient patient) {
        this.patient = patient;
        patient.setLocation(this);
        Logger.getInstance().log(patient, "  enters treatment room.");
    }

    @Override
    public void leave(Patient patient) {
        this.patient = null;
        patient.setLocation(null);
        Logger.getInstance().log(patient, " leaves treatment room.");
    }
}
