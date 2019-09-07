package nil.ed.onlineexam.service.getter;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class TestPaperServiceGetter {
    public static List<LinkedHashMap<String,Object>> getOptions(JSONObject jsonObject){
        return getArrayList(jsonObject, "options");
    }

    public static List<LinkedHashMap<String,Object>> getSubjectives(JSONObject jsonObject){
        return getArrayList(jsonObject, "subjectives");
    }

    public static Integer getScore(JSONObject jsonObject){
        return jsonObject.getInteger("score");
    }

    public static List<LinkedHashMap<String,Object>> getArrayList(JSONObject jsonObject, String key){
        return jsonObject.getObject(key, ArrayList.class);
    }
}
