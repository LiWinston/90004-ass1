import java.util.Hashtable;

/**
 * The Orderlies class manages the recruitment and release of orderlies in the emergency department.
 * <p>
 * It ensures thread safety during the recruitment and release processes.
 * Orderlies are recruited by nurses to assist with patient transfers.
 * @author Yongchunli 1378156
 * @version 1.0
 * @since 2024-03-16
 */
public class Orderlies {
    private static Orderlies instance;
    private final Hashtable<Integer, Integer> recruitRecord = new Hashtable<>();
    private volatile int freeOrderlies;

    /**
     * Constructs an Orderlies instance with default values for the number of available orderlies and transfer time.
     */
    public Orderlies() {
        this.freeOrderlies = Params.ORDERLIES;
    }

    /**
     * Returns the singleton instance of the Orderlies class.
     *
     * @return the singleton instance of the Orderlies class
     */
    public static Orderlies getInstance() {
        if (instance == null) {
            instance = new Orderlies();
        }
        return instance;
    }

    /**
     * Recruits the specified number of orderlies to assist a nurse.
     * <p>
     * This method ensures thread safety by synchronizing access to the recruitment process.
     * If the nurse has already recruited orderlies, the recruitment is prohibited until the previous orderlies are released.
     * If there are not enough free orderlies, the method waits until more become available.
     * Once orderlies are recruited, the method updates the recruitment record and logs the recruitment details.
     *
     * @param nurse      the nurse requesting the orderlies
     * @param numOrderlies the number of orderlies to recruit
     */
    public void recruitOrderlies(Nurse nurse, int numOrderlies) {
        synchronized (this) {
            if (recruitRecord.containsKey(nurse.getNurseId())) {
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

    /**
     * Releases the orderlies previously recruited by a nurse.
     * <p>
     * This method ensures thread safety by synchronizing access to the release process.
     * It updates the number of free orderlies and removes the nurse's recruitment record.
     * After releasing the orderlies, it notifies all waiting threads.
     *
     * @param nurse the nurse releasing the orderlies
     */
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
