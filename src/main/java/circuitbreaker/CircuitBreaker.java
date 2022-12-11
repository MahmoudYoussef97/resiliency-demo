package circuitbreaker;

import java.time.temporal.ChronoUnit;
import java.util.Arrays;

public class CircuitBreaker {

    public CircuitBreaker() {

    }

    public CircuitBreaker onOpen(Runnable runnable) {
        return this;
    }

    public CircuitBreaker onClose(Runnable runnable) {
        return this;
    }

    public CircuitBreaker onHalfOpen(Runnable runnable) {
        return this;
    }

    public static class CircuitBreakerBuilder {
        private int failuresThreshold = 3;
        private int successThreshold = 2;
        private long delay = 1;
        private Class<? extends Throwable>[] exceptions;
        private ChronoUnit chronoUnit = ChronoUnit.SECONDS;

        public CircuitBreakerBuilder withFailureThreshold(int failuresThreshold) {
            this.failuresThreshold = failuresThreshold;
            return this;
        }

        public CircuitBreakerBuilder withSuccessThreshold(int successThreshold) {
            this.successThreshold = successThreshold;
            return this;
        }

        public CircuitBreakerBuilder withDelay(long delay, ChronoUnit chronoUnit) {
            this.delay = delay;
            this.chronoUnit = chronoUnit;
            return this;
        }

        public CircuitBreakerBuilder handle(Class<? extends Throwable>... exceptions) {
            Arrays.stream(exceptions).findFirst().orElseThrow(NullPointerException::new);
            this.exceptions = exceptions;
            return this;
        }
    }
}
