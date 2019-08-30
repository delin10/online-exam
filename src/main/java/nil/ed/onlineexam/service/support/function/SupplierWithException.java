package nil.ed.onlineexam.service.support.function;

public interface SupplierWithException<T> {
    T supply() throws Exception;
}
