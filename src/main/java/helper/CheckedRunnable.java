package helper;

@FunctionalInterface
public interface CheckedRunnable {
    void run() throws Throwable;
}