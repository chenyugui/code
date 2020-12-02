package com.taichuan.code.utils;

import android.annotation.SuppressLint;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.taichuan.code.result.ResultData;
import com.taichuan.code.result.ResultDataList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gui on 2017/8/8.
 * Json转化工具
 */
@SuppressLint("LogNotTimber")
public class JsonUtil {
    private static final String TAG = "JsonUtil";

    public static <T> List<T> jsonToArray(String jsonString, Class<T> cls) throws JSONException {
        JSONArray array = new JSONArray(jsonString);
        int size = array.length();
        ArrayList<T> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            JSONObject object = array.getJSONObject(i);
            if (cls == String.class) {
                list.add((T) object.toString());
            } else {
                list.add(GsonUtil.getGson().fromJson(object.toString(), cls));
            }
        }
        return list;
    }

    public static <T> ResultData<T> toResultData(String jsonString, Class<T> cls) {
        ResultData<T> resultData = null;
        try {
            JSONObject j = new JSONObject(jsonString);
            resultData = new ResultData<>();
            resultData.setCode(j.getInt("code"));
            resultData.setMsg(j.getString("msg"));
            if (j.get("data") != null) {
                String data = j.getString("data");
                if (cls == String.class) {
                    resultData.setData((T) data);
                } else {
                    T t = JSON.parseObject(data, cls);
                    resultData.setData(t);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, jsonString);
        }
        return resultData;
    }

    public static <T> ResultDataList<T> toResultDataList(String jsonString, Class<T> cls) {
        ResultDataList<T> resultList = null;
        try {
            JSONObject j = new JSONObject(jsonString);
            resultList = new ResultDataList<>();
            resultList.setCode(j.getInt("code"));
            resultList.setMsg(j.getString("msg"));
            if (j.has("data")) {
                resultList.setData(jsonToArray(j.getString("data"), cls));
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, jsonString);
        }
        return resultList;
    }

}
