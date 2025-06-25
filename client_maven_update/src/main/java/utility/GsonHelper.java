package utility;

import com.google.gson.Gson;

public class GsonHelper {
    private static Gson gson = new Gson();
    public static <T extends Object> String  toJsonObj(T obj){
        return gson.toJson(obj);
    }
    public static<T> T fromJsonToObj(String json,Class<T> classOf){
        return gson.fromJson(json, classOf);
    }
}
