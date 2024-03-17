public class Triage implements Movable {

    private Patient patient;
    @Override
    public boolean isAccessible() {
        return patient == null;
    }

    @Override
    public synchronized void enter(Patient patient) {
        this.patient = patient;
        patient.setLocation(this);
        Logger.getInstance().log(patient, " enters triage.");
        try {
            Thread.sleep(Params.TRIAGE_TIME);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void leave(Patient patient) {
        patient.setLocation(null);
        this.patient = null;
        Logger.getInstance().log(patient, " leaves triage.");
    }
}
