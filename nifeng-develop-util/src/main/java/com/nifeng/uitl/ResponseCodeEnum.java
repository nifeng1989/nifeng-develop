package com.nifeng.uitl;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by fengni on 2015/8/14.
 */

public enum ResponseCodeEnum {
    ERROR_PARAMS(101, "params err", "{\"status\":101,\"desc\":\"params err\"}"),
    NO_DATA(401, "no data", "{\"status\":401,\"desc\":\"no data\"}"),
    ERROR(500, "error", "{\"status\":500,\"desc\":\"error\"}"),
    SUCCESS(200, "success", "{\"status\":200,\"desc\":\"success\"}");

    private int status;
    private String desc;
    private String defaultResponse;

    ResponseCodeEnum(int status, String desc, String defaultResponse) {
        this.status = status;
        this.desc = desc;
        this.defaultResponse = defaultResponse;
    }

    public String toJSON() {
        return defaultResponse;
    }

    public String toJSONMsg(String desc) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("status", status);
        map.put("desc", desc);
        return JSonUtil.getJsonStringFromObject(map);
    }

    public String toJSONData(Object data) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("status", status);
        map.put("desc", desc);
        map.put("data", data);
        return JSonUtil.getJsonStringFromObject(map);
    }


    public String toJSON(String desc, Object data) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("status", status);
        map.put("desc", desc);
        map.put("data", data);
        return JSonUtil.getJsonStringFromObject(map);
    }
}
