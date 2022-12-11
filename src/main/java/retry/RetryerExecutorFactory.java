package retry;

public class RetryerExecutorFactory {

    public RetryerExecutor getInstanceOf(Retryer retryer) {
        return switch (retryer.getRetryStrategy()) {
            case RETRY_WITH_MAX_RETRIES -> new MaxRetriesExecutor(retryer);
            case RETRY_WITH_EXPONENTIAL_BACKOFF -> new ExponentialBackOffRetriesExecutor(retryer);
            case RETRY_WITH_FIXED_DELAY -> new FixedDelayRetryExecutor(retryer);
        };
    }
}
