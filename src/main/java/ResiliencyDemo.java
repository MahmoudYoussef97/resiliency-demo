import retry.Retryer;
import retry.RetryerExecutor;

import java.io.IOException;
import java.time.temporal.ChronoUnit;

public class ResiliencyDemo {
    private static int counter;

    public static void main(String[] args) {
        RetryerExecutor retryer = getFixedDelayRetryExecutor();
        try {
            var result = retryer.run(() -> testFunction());
            System.out.println(result);
        } catch (Throwable e) {
            System.out.println("ERROR:");
            throw new RuntimeException(e);
        }
    }

    private static RetryerExecutor getMaxRetryExecutor() {
        var retryer = new Retryer.RetryPolicyBuilder()
                .handle(IllegalStateException.class, IOException.class)
                .withMaxRetries(3)
                .build();
        return retryer;
    }

    private static RetryerExecutor getFixedDelayRetryExecutor() {
        var retryer = new Retryer.RetryPolicyBuilder()
                .handle(IllegalStateException.class, IOException.class)
                .withFixedDelay(2, 8, ChronoUnit.SECONDS)
                .build();
        return retryer;
    }

    private static RetryerExecutor getExponentialBackOffRetryExecutor() {
        var retryer = new Retryer.RetryPolicyBuilder()
                .handle(IllegalStateException.class, IOException.class)
                .withBackOff(1, 16, ChronoUnit.SECONDS, 2)
                .build();
        return retryer;
    }

    private static String testFunction() throws IOException {
        System.out.println("Started..");
        counter ++;
        if(counter != 4) {
            throw new IOException();
        }
        return "Succeeded!";
    }
}

