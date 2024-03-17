public interface Movable {
    boolean isAccessible();

    void enter(Patient patient);

    void leave(Patient patient);

}
