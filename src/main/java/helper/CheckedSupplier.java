package helper;

@FunctionalInterface
public interface CheckedSupplier<ResultT> {
    ResultT get() throws Throwable;
}
