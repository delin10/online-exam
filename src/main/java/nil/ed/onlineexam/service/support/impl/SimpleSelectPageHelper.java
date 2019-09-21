package nil.ed.onlineexam.service.support.impl;

import nil.ed.onlineexam.common.PageResult;
import nil.ed.onlineexam.common.PageResultResponseBuilder;
import nil.ed.onlineexam.common.Response;
import nil.ed.onlineexam.common.ResponseCodeEnum;
import nil.ed.onlineexam.service.support.SelectPageHelper;
import nil.ed.onlineexam.util.PageUtils;

import java.util.List;
import java.util.concurrent.*;
import java.util.function.Supplier;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class SimpleSelectPageHelper<T> implements SelectPageHelper<T> {
    private Supplier<Integer> counter;

    private Integer pageNo, pageSize;

    private Executor executor;

    private long timeout = 3;

    public SimpleSelectPageHelper(Executor executor) {
        this.executor = executor;
    }

    @Override
    public Response<PageResult<T>> operate(Supplier<List<T>> supplier) {
        PageUtils.checkPageParam(pageNo, pageSize);

        CompletableFuture<List<T>> listCompletableFuture = CompletableFuture.supplyAsync(supplier, executor);
        CompletableFuture<Integer> counterCompletableFuture = CompletableFuture.supplyAsync(counter, executor);

        try {
            return new PageResultResponseBuilder<T>()
                    .setPageNo(pageNo)
                    .setPageSize(pageSize)
                    .setTotal(counterCompletableFuture.get(timeout, TimeUnit.SECONDS))
                    .setPageData(listCompletableFuture.get(timeout, TimeUnit.SECONDS))
                    .build();
        } catch (TimeoutException e){
            return new PageResultResponseBuilder<T>()
                    .setCodeEnum(ResponseCodeEnum.TIMEOUT)
                    .build();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public SelectPageHelper<T> setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
        return this;
    }

    @Override
    public SelectPageHelper<T> setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    @Override
    public SelectPageHelper<T> setCounter(Supplier<Integer> counter) {
        this.counter = counter;
        return this;
    }
}
