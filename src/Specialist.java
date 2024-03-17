public class Specialist extends Thread{
    //There is only one Specialist, who also has duties elsewhere in the hospital. Therefore, they leave the ED to attend to these other duties after treating each Patient, returning after period of time.
    private Treatment treatment;
    private boolean isAtTreatment = true;

    public Specialist(Treatment treatment) {
        this.treatment = treatment;
    }

    @Override
    public void run() {
        super.run();

    }
}
