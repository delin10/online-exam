package nil.ed.onlineexam.util;

import com.esotericsoftware.reflectasm.MethodAccess;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Predicate;

public class ReflectionAsmUtils {
    private static final ConcurrentMap<Class, MethodAccess> localCache = Maps.newConcurrentMap();

    public static MethodAccess get(Class clazz) {
        if(localCache.containsKey(clazz)) {
            return localCache.get(clazz);
        }

        MethodAccess methodAccess = MethodAccess.get(clazz);
        localCache.putIfAbsent(clazz, methodAccess);
        return methodAccess;
    }

    public static <F,T> void copyProperties(F from, T to) {
        copyProperties(from, to, name -> true, null);

    }

    public static <F,T> void copyProperties(F from, T to, Predicate<String> exclude) {
        copyProperties(from, to, exclude, null);

    }

    public static <F,T> void copyProperties(F from, T to, Predicate<String> exclude, Map<String, String> mapper) {
        MethodAccess fromMethodAccess = get(from.getClass());
        MethodAccess toMethodAccess = get(to.getClass());
        Field[] fromDeclaredFields = from.getClass().getDeclaredFields();
        for (Field field : fromDeclaredFields) {
            String name = field.getName();
            try {
                if (exclude.negate().test(name)) {
                    String toPropertyName = Objects.isNull(mapper) ? name : mapper.getOrDefault(name, name);
                    Object value = fromMethodAccess.invoke(from, "get" + StringUtils.capitalize(name), (Object[])null);
                    toMethodAccess.invoke(to, "set" + StringUtils.capitalize(toPropertyName), value);
                }
            } catch (Exception e) {/*ignore*/}
        }
    }
}
