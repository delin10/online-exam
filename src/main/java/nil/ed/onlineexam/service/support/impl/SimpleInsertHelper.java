package nil.ed.onlineexam.service.support.impl;

import nil.ed.onlineexam.common.NormalResponseBuilder;
import nil.ed.onlineexam.common.Response;
import nil.ed.onlineexam.common.ResponseCodeEnum;
import nil.ed.onlineexam.service.support.CreateInfoSetter;
import nil.ed.onlineexam.service.support.InsertHelper;
import nil.ed.onlineexam.service.support.UpdateInfoSetter;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class SimpleInsertHelper implements InsertHelper{
    @Override
    public Response<Void> operate(Supplier<Void> supplier) {
        supplier.get();

        return new NormalResponseBuilder<Void>()
                .setCodeEnum(ResponseCodeEnum.SUCCESS)
                .build();
    }
}
