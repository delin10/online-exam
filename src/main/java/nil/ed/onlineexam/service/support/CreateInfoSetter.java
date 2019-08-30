package nil.ed.onlineexam.service.support;

import java.util.function.Consumer;

public interface CreateInfoSetter<C,CT> {
    InsertHelper setCreatorSetterAndCreator(Consumer<C> creatorSetter, C creator);

    InsertHelper setCreateTimeSetterAndCreateTime(Consumer<CT> createTimeSetter, CT createTime);
}
