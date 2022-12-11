package retry;

import helper.CheckedRunnable;
import helper.CheckedSupplier;

public class ExponentialBackOffRetriesExecutor extends BaseRetryExecutor implements RetryerExecutor {

    private long currentDelay;

    public ExponentialBackOffRetriesExecutor(Retryer retryer) {
        super(retryer);
        this.currentDelay = retryer.getDelay();
    }

    @Override
    public void run(CheckedRunnable checkedRunnable) throws Throwable {
        do {
            shouldRetry = currentDelay < retryer.getMaxDelay();
            try {
                checkedRunnable.run();
            } catch (Exception ex) {
                exceptionToThrow = ex;
                if(isCheckedException(exceptionToThrow)) {
                    checkedExceptionHandler();
                    continue;
                }
                onFailureHandler();
                throw exceptionToThrow;
            }
            shouldRetry = false;
        } while (shouldRetry);
        onFailureHandler();
        throw exceptionToThrow;
    }

    private void updateCurrentDelay() {
        currentDelay = currentDelay * retryer.getDelayFactor();
    }

    @Override
    public <ResultT> ResultT run(CheckedSupplier<ResultT> checkedSupplier) throws Throwable {
        do {
            shouldRetry = currentDelay < retryer.getMaxDelay();
            try {
                return checkedSupplier.get();
            } catch (Exception ex) {
                exceptionToThrow = ex;
                if(isCheckedException(exceptionToThrow)) {
                    checkedExceptionHandler();
                    continue;
                }
                onFailureHandler();
                throw exceptionToThrow;
            }
        } while (shouldRetry);
        onFailureHandler();
        throw exceptionToThrow;
    }

    private void checkedExceptionHandler() throws InterruptedException {
        onRetryHandler();
        Thread.sleep(currentDelay * retryer.getDelayFactor() * 1000);
        updateCurrentDelay();
    }
}
