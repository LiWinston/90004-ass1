public class Nurse extends Thread{
    private int nurseId;
    private Foyer foyer;
    private Triage triage;
    private Orderlies orderlies;
    private Treatment treatment;

    public Nurse(int i, Foyer foyer, Triage triage, Orderlies orderlies, Treatment treatment) {
        this.nurseId = i;
        this.foyer = foyer;
        this.triage = triage;
        this.orderlies = orderlies;
        this.treatment = treatment;
    }

    @Override
    public void run() {
        super.run();
    }
}
