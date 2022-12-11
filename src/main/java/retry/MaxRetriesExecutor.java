package retry;

import helper.CheckedRunnable;
import helper.CheckedSupplier;

public class MaxRetriesExecutor extends BaseRetryExecutor implements RetryerExecutor {

    private int currentRetries;

    public MaxRetriesExecutor(Retryer retryer) {
        super(retryer);
        this.currentRetries = 0;
    }

    @Override
    public void run(CheckedRunnable checkedRunnable) throws Throwable {
        do {
            this.currentRetries = this.currentRetries + 1;
            shouldRetry = this.currentRetries < retryer.getMaxRetries();
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

    private void checkedExceptionHandler() throws InterruptedException {
        onRetryHandler();
        Thread.sleep(retryer.getDelay() * 1000);
    }

    @Override
    public <ResultT> ResultT run(CheckedSupplier<ResultT> checkedSupplier) throws Throwable {
        do {
            this.currentRetries = this.currentRetries + 1;
            shouldRetry = this.currentRetries < retryer.getMaxRetries();
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
}
