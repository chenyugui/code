package com.taichuan.code.requestcallback;

import android.util.Log;

import com.taichuan.code.http.ErrCode;
import com.taichuan.code.http.callback.RequestCallbacks;
import com.taichuan.code.result.ResultData;
import com.taichuan.code.utils.JsonUtil;

import retrofit2.Call;

/**
 * @author gui
 * @date 2020/6/11
 */
public abstract class ResultDataCallBack<T> extends RequestCallbacks<T> {
    private static final String TAG = "ResultDataCallBack";
    private Class<T> mClass;

    public ResultDataCallBack(Class<T> classT) {
        super(null, null, null);
        mClass = classT;
    }


    @Override
    protected void onRequestSuccess(Call<String> call, String responseString) {
        ResultData<T> result = JsonUtil.toResultData(responseString, mClass);
        if (result != null) {
            int code = result.getCode();
            if (code == 0) {
                onSuccess(result.getData());
            } else {
                Log.e(TAG, "result false， responseString = " + responseString);
                onFail(result.getCode(), result.getMsg());
            }
        } else {
            Log.e(TAG, "result null");
            onFail(ErrCode.CODE_JSON_ERR, "Request Fail");
        }
    }

    @Override
    protected void onRequestFail(Call<String> call, int errCode) {
        onFail(errCode, call.toString());
    }

    public abstract void onSuccess(T data);

    public abstract void onFail(int code, String err);
}
