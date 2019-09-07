package nil.ed.onlineexam.util;

import com.alibaba.fastjson.JSONObject;
import com.esotericsoftware.reflectasm.MethodAccess;
import com.google.common.collect.Maps;
import nil.ed.onlineexam.entity.TestPaper;
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

    public static <T> void copyProperties(JSONObject from, T to) {
        MethodAccess toMethodAccess = get(to.getClass());
        Field[] toDeclaredFields = to.getClass().getDeclaredFields();
        for (Field field : toDeclaredFields) {
            String name = field.getName();
            String setterName = "set"+StringUtils.capitalize(name);
            System.out.println("copy:"+name);
            toMethodAccess.invoke(to, setterName, from.getObject(name, field.getType()));
        }
    }

    public static void main(String[] args) {
        JSONObject jo = JSONObject.parseObject("{\"score\":\"100\",\"startTime\":1568044800000,\"endTime\":1568131200000,\"testDuration\":\"60\",\"options\":[{\"qid\":165,\"score\":\"1\"},{\"qid\":133,\"score\":\"1\"}],\"subjectives\":[{\"qid\":165,\"score\":\"1\"},{\"qid\":133,\"score\":\"1\"}]}");
        TestPaper testPaper = new TestPaper();
        copyProperties(jo,testPaper);
    }
}
