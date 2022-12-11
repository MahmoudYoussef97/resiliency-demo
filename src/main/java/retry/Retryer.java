package retry;

import java.time.temporal.ChronoUnit;
import java.util.Arrays;

public class Retryer {
    private RetryPolicyBuilder retryPolicyBuilder;

    private Retryer(RetryPolicyBuilder retryPolicyBuilder) {
        this.retryPolicyBuilder = retryPolicyBuilder;
    }

    public long getMaxRetries() {
        return retryPolicyBuilder.maxRetries;
    }

    public long getDelay() {
        return retryPolicyBuilder.delay;
    }

    public long getMaxDelay() {
        return retryPolicyBuilder.maxDelay;
    }

    public long getDelayFactor() {
        return retryPolicyBuilder.delayFactor;
    }

    public RetryStrategy getRetryStrategy() {
        return retryPolicyBuilder.retryStrategy;
    }

    public ChronoUnit getChronoUnit() {
        return retryPolicyBuilder.chronoUnit;
    }

    public Class<? extends Throwable>[] getExceptions() {
        return retryPolicyBuilder.exceptions;
    }

    public Runnable getRetryRunnable() {return retryPolicyBuilder.retryRunnable;}
    public Runnable getFailureRunnable() {return retryPolicyBuilder.failureRunnable;}

    public static class RetryPolicyBuilder {
        private long maxRetries = 3;
        private long delay = 1;
        private long maxDelay = 0;
        private long delayFactor = 2;
        private RetryerExecutorFactory retryerFactory = new RetryerExecutorFactory();
        private RetryStrategy retryStrategy;
        private ChronoUnit chronoUnit;
        private Class<? extends Throwable>[] exceptions;
        private Runnable retryRunnable;
        private Runnable failureRunnable;

        public RetryPolicyBuilder withMaxRetries(int maxRetries) {
            this.maxRetries = maxRetries;
            this.retryStrategy = RetryStrategy.RETRY_WITH_MAX_RETRIES;
            return this;
        }

        public RetryPolicyBuilder withMaxRetries(int maxRetries, long delay) {
            this.maxRetries = maxRetries;
            this.delay = delay;
            this.retryStrategy = RetryStrategy.RETRY_WITH_MAX_RETRIES;
            return this;
        }

        public RetryPolicyBuilder withFixedDelay(long delay, long maxDelay, ChronoUnit chronoUnit) {
            this.delay = delay;
            this.maxDelay = maxDelay;
            this.chronoUnit = chronoUnit;
            this.retryStrategy = RetryStrategy.RETRY_WITH_FIXED_DELAY;
            return this;
        }

        public RetryPolicyBuilder withBackOff(long delay, long maxDelay, ChronoUnit chronoUnit, long delayFactor) {
            this.delay = delay;
            this.maxDelay = maxDelay;
            this.chronoUnit = chronoUnit;
            this.delayFactor = delayFactor;
            this.retryStrategy = RetryStrategy.RETRY_WITH_EXPONENTIAL_BACKOFF;
            return this;
        }

        public RetryPolicyBuilder handle(Class<? extends Throwable>... exceptions) {
            Arrays.stream(exceptions).findFirst().orElseThrow(NullPointerException::new);
            this.exceptions = exceptions;
            return this;
        }

        public RetryPolicyBuilder onRetry(Runnable retryRunnable) {
            this.retryRunnable = retryRunnable;
            return this;
        }

        public RetryPolicyBuilder onFailure(Runnable failureRunnable) {
            this.failureRunnable = failureRunnable;
            return this;
        }

        public RetryerExecutor build() {
            return retryerFactory.getInstanceOf(new Retryer(this));
        }

    }
}
