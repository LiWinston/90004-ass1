/**
 * Represents a location within the hospital that patients can access.
 * <p>
 * This interface defines methods to check if the location is accessible,
 * and to allow patients to enter or leave the location.
 * <p>
 * Implementing classes should provide functionality to manage patient movement
 * within the hospital.
 *
 * @author Yongchunli 1378156
 * @version 1.0
 * @since 2024-03-16
 */
public interface Movable {
    /**
     * Checks if the location is accessible.
     *
     * @return true if the location is accessible, false otherwise
     */
    boolean isAccessible();

    /**
     * Allows a patient to enter the location.
     *
     * @param patient the patient entering the location
     */
    void enter(Patient patient);

    /**
     * Allows a patient to leave the location.
     *
     * @param patient the patient leaving the location
     */
    void leave(Patient patient);
}
