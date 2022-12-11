package retry;

import helper.CheckedRunnable;
import helper.CheckedSupplier;

public interface RetryerExecutor {
    void run(CheckedRunnable checkedRunnable) throws Throwable;
    <ResultT> ResultT run(CheckedSupplier<ResultT> checkedSupplier) throws Throwable;
}
