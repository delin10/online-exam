package nil.ed.onlineexam.service.support;

import java.util.function.Consumer;

public interface UpdateInfoSetter<U, UT> {
    InsertHelper setUpdaterSetterAndUpdater(Consumer<U> updaterSetter, U updater);

    InsertHelper setUpdateTimeSetterAndUpdateTime(Consumer<UT> updateTimeSetter, UT updateTime);

}
