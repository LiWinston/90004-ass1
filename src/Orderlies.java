import java.util.Hashtable;

public class Orderlies {
    private static Orderlies instance;
    private int TRANSFER_ORDERLIES;

    private volatile int freeOrderlies;
//    private final Hashtable<Integer, Integer> recruitRecord = new Hashtable<>();

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

    public synchronized void recruitOrderlies(Nurse nurse, int numOrderlies) {
        //thread-safe method to recruit orderlies

    }
}
