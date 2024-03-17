public class Triage implements Movable {

    @Override
    public boolean isAccessible() {
        return true;
    }

    @Override
    public void enter(Patient patient) {
        patient.setLocation(this);
        Logger.getInstance().log(patient, " enters Triage.");
    }

    @Override
    public void leave(Patient patient) {
        patient.setLocation(null);
        try {
            Thread.sleep(Params.TRIAGE_TIME);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Logger.getInstance().log(patient, " leaves Triage.");
    }
}
