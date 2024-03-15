public class Specialist extends Thread{
    private Treatment treatment;
    public Specialist(Treatment treatment) {
        this.treatment = treatment;
    }

    @Override
    public void run() {
        super.run();
    }
}
