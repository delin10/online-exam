package nil.ed.onlineexam.common;

import com.alibaba.fastjson.JSONObject;

public class CommonVO extends JSONObject{
    public CommonVO() {
        super();
    }

    public CommonVO add(String field, Object data){
        super.put(field, data);
        return this;
    }
}
