import java.util.Random;

/**
 * Parameters that influence the behaviour of the system.
 *
 * @author ngeard@unimelb.edu.au
 * @date 13 February 2024
 * implemented by: Yongchunli 1378156
 */

public class Params {
  public final static String LOG_FILE = "log.txt";

  //the number of nurses in the emergency department
  public final static int NURSES = 20;

  //the number of orderlies in the emergency department
  public final static int ORDERLIES = 100;

  //the number of orderlies required to transfer a patient
  public final static int TRANSFER_ORDERLIES = 5;

  //the proportion of patients whose condition is severe
  public final static double SEVERE_PROPORTION = 0.65;

  //the time it takes for a patient to undergo triage
  public final static int TRIAGE_TIME = 20;

  //the time it takes for a patient to be treated
  public final static int TREATMENT_TIME = 50;

  //the time it takes for a nurse (+ orderlies) to transfer a patient between locations
  public final static int TRANSFER_TIME = 80;

  //the time a specialist spends elsewhere between treating patients
  public final static int SPECIALIST_AWAY_TIME = 2000;

  //the maximum amount of time between patient arrivals at ED (before admission)
  public final static int MAX_ARRIVE_INTERVAL = 1000;

  //the maximum amount of time between departures from ED (after discharge)
  public final static int MAX_DEPART_INTERVAL = 10;


  /**
   * For simplicity, we assume uniformly distributed time lapses.
   */

  public static int arrivalPause() {
    Random random = new Random();
    return random.nextInt(MAX_ARRIVE_INTERVAL);
  }

  public static int departurePause() {
    Random random = new Random();
    return random.nextInt(MAX_DEPART_INTERVAL);
  }

}

