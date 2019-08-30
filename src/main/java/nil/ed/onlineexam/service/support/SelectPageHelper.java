package nil.ed.onlineexam.service.support;

import nil.ed.onlineexam.common.PageResult;

import java.util.List;
import java.util.function.Supplier;

public interface SelectPageHelper<T> extends Operator<PageResult<T>, List<T>>  {
    SelectPageHelper<T> setPageNo(Integer pageNo);

    SelectPageHelper<T> setPageSize(Integer pageSize);

    SelectPageHelper<T> setCounter(Supplier<Integer> counter);

}
