package nil.ed.onlineexam.service.support.impl;

import nil.ed.onlineexam.common.NormalResponseBuilder;
import nil.ed.onlineexam.common.Response;
import nil.ed.onlineexam.common.ResponseCodeEnum;
import nil.ed.onlineexam.service.support.SelectOneHelper;
import nil.ed.onlineexam.vo.UserVO;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.function.Supplier;

public class SimpleSelectOneHelper<T> implements SelectOneHelper<T> {
    @Override
    public Response<T> operate(Supplier<T> supplier) {
        T result = supplier.get();

        if (Objects.isNull(result)){
            return new NormalResponseBuilder<T>()
                    .setCodeEnum(ResponseCodeEnum.NOT_FOUND)
                    .build();
        }
        return new NormalResponseBuilder<T>()
                .setData(result)
                .setCodeEnum(ResponseCodeEnum.SUCCESS)
                .build();
    }
}
