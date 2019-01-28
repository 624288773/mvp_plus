package top.mvpplus.global.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzh on 2018/7/11.
 */

public class JsonUtils {
    private static Gson gson = new GsonBuilder().disableHtmlEscaping().create();

    /**
     * 返回JsonString
     */
    public static String toJson(Object obj) {
        try {
            if (obj != null) {
                return gson.toJson(obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 返回对象
     */
    public static <T> T toBean(String data, Class<T> clazz) {
        try {
            if (data != null) {
                return gson.fromJson(data, clazz);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 返回数组对象
     */
    public static <T> List<T> toBeanList(String data, Class<T> clazz) {
        try {
            if (data != null) {
                Type type = new TypeToken<ArrayList<JsonObject>>() {
                }.getType();
                ArrayList<JsonObject> jsonObjects = gson.fromJson(data, type);
                ArrayList<T> arrayList = new ArrayList<>();
                for (JsonObject jsonObject : jsonObjects) {
                    arrayList.add(new Gson().fromJson(jsonObject, clazz));
                }
                return arrayList;
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }


}
