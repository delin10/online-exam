package nil.ed.onlineexam.service.support.impl;

import nil.ed.onlineexam.common.NormalResponseBuilder;
import nil.ed.onlineexam.common.Response;
import nil.ed.onlineexam.common.ResponseCodeEnum;
import nil.ed.onlineexam.service.support.DeleterHelper;
import nil.ed.onlineexam.service.support.function.SupplierWithException;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component("simpleDeleteHelper")
public class SimpleDeleteHelper implements DeleterHelper {
    @Override
    public Response<Void> operate(Supplier<Integer> supplier) {
        Integer deleteResult = supplier.get();

        if (deleteResult == 0){
            return new NormalResponseBuilder<Void>()
                    .setCodeEnum(ResponseCodeEnum.NOT_FOUND)
                    .build();
        }

        return new NormalResponseBuilder<Void>()
                .setCodeEnum(ResponseCodeEnum.SUCCESS)
                .build();
    }
}
