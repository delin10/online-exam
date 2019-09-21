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

    public static Short getScore(JSONObject jsonObject){
        return jsonObject.getShort("score");
    }

    public static List<LinkedHashMap<String,Object>> getArrayList(JSONObject jsonObject, String key){
        return jsonObject.getObject(key, ArrayList.class);
    }

    public static Integer getQid(JSONObject jsonObject){
        return jsonObject.getInteger("qid");
    }
}
