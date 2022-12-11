package retry;

import java.util.Arrays;

public class BaseRetryExecutor {

    protected boolean shouldRetry;
    protected Retryer retryer;
    protected Exception exceptionToThrow;

    public BaseRetryExecutor(Retryer retryer) {
        this.retryer = retryer;
    }

    protected boolean isCheckedException(Exception ex) {
        return Arrays.stream(retryer.getExceptions())
                .anyMatch(e -> e.isInstance(ex));
    }

    protected void onRetryHandler() {
        retryer.getRetryRunnable().run();
    }

    protected void onFailureHandler() {
        retryer.getFailureRunnable().run();
    }
}
