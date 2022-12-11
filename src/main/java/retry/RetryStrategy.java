package retry;

public enum RetryStrategy {
    RETRY_WITH_FIXED_DELAY,
    RETRY_WITH_EXPONENTIAL_BACKOFF,
    RETRY_WITH_MAX_RETRIES
}
