package com.zzm.zzmlibrary.model.bean;


import com.zzm.zzmlibrary.utils.UIUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by itzhong on 2016/7/14.
 */
public class BaseResponse extends JSONObject{

    private int code;
    private String message;
    private String data;

    public BaseResponse(String responseStr) throws JSONException {
        super(responseStr);
    }

    public int getCode() {
        try {
            return getInt("code");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public String getMessage() {
        try {
            return getString("message");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getData() {
        try {
            return getString("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    public JSONObject getDataJSONObject() {
        JSONObject data = optJSONObject("data");
        if (data == null) {
            return this;
        }
        return data;
    }

    public boolean isOk() {
        if (getCode() == 200) {
            return true;
        }else{
            UIUtils.showToast(getMessage());
        }
        return false;
    }
}
