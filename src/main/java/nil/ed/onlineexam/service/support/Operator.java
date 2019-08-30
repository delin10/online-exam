package nil.ed.onlineexam.service.support;

import nil.ed.onlineexam.common.Response;
import nil.ed.onlineexam.service.support.function.SupplierWithException;

import java.util.function.Supplier;

public interface Operator<T,R> {
    Response<T> operate(Supplier<R> supplier);
}
