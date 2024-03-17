import java.util.Hashtable;

public class Orderlies {
    private static Orderlies instance;
    private final Hashtable<Integer, Integer> recruitRecord = new Hashtable<>();
    private int TRANSFER_ORDERLIES;
    private volatile int freeOrderlies;

    public Orderlies() {
        this.freeOrderlies = Params.ORDERLIES;
        this.TRANSFER_ORDERLIES = Params.TRANSFER_ORDERLIES;
    }

    public static Orderlies getInstance() {
        if (instance == null) {
            instance = new Orderlies();
        }
        return instance;
    }

    public void recruitOrderlies(Nurse nurse, int numOrderlies) {
        //get orderlies in thread safe way
        synchronized (this) {
            if (recruitRecord.containsKey(nurse.getNurseId())) {
                //prohibit the nurse from recruiting orderlies again before the previous orderlies are released
                Logger.getInstance().log("♦️Nurse " + nurse.getNurseId() + " is not allowed to recruit orderlies again" +
                        " before the previous orderlies are released");
                return;
            }
            while (numOrderlies > freeOrderlies) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            freeOrderlies -= numOrderlies;
            recruitRecord.put(nurse.getNurseId(), numOrderlies);
            Logger.getInstance().log("Nurse " + nurse.getNurseId() + " recruits " + numOrderlies + " orderlies " +
                    "(" + freeOrderlies + " free).");
            notify();
        }
    }

    public void releaseOrderlies(Nurse nurse) {
        synchronized (this) {
            if (recruitRecord.containsKey(nurse.getNurseId())) {
                int numOrderlies = recruitRecord.get(nurse.getNurseId());
                freeOrderlies += numOrderlies;
                recruitRecord.remove(nurse.getNurseId());
                Logger.getInstance().log("Nurse " + nurse.getNurseId() + " releases " + numOrderlies + " orderlies " +
                        "(" + freeOrderlies + " free).");
                this.notifyAll();
            }
        }
    }

}
