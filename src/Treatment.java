public class Treatment implements Movable {
    @Override
    public boolean isAccessible() {
        return true;
    }

    @Override
    public void enter(Patient patient) {
        patient.setLocation(this);
        Logger.getInstance().log(patient, " enters Treatment.");
    }

    @Override
    public void leave(Patient patient) {
        patient.setLocation(null);
        Logger.getInstance().log(patient, " leaves Treatment.");
    }
}
