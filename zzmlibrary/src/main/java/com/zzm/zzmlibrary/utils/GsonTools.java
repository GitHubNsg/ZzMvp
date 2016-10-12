package com.zzm.zzmlibrary.utils;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class GsonTools {

	public GsonTools() {
		// TODO Auto-generated constructor stub
	}

	public static String createGsonString(Object object) {
		Gson gson = new Gson();
		String gsonString = gson.toJson(object);
		return gsonString;
	}

	public static <T> T changeGsonToBean(String gsonString, Class<T> cls) {
		if(TextUtils.isEmpty(gsonString)||"false".equals(gsonString)){
			return null;
		}
		try {
			Gson gson = new Gson();
			T t = gson.fromJson(gsonString, cls);
			return t;
		} catch (Exception e) {
			return null;
		}
	}

	public static <T> List<T> changeGsonToList(String gsonString, Class<T> cls) {
		if(TextUtils.isEmpty(gsonString)||"false".equals(gsonString)){
			return null;
		}
		Gson gson = new Gson();
		List<T> list = null;
		try {
			list = gson.fromJson(gsonString, new TypeToken<List<T>>() {
			}.getType());
		} catch (Exception e) {
			return null;
		}
		return list;
	}

	public static <T> List<T> changeGsonToList(String gsonString, Type type) {
		if(TextUtils.isEmpty(gsonString)||"false".equals(gsonString)){
			return null;
		}
		Gson gson = new Gson();
		List<T> list = null;
		try {
			list = gson.fromJson(gsonString, type);
		} catch (Exception e) {
			return null;
		}
		return list;
	}
	
	public static <T> List<Map<String, T>> changeGsonToListMaps(
			String gsonString) {
		if(TextUtils.isEmpty(gsonString)||"false".equals(gsonString)){
			return null;
		}
		List<Map<String, T>> list = null;
		Gson gson = new Gson();
		try {
			list = gson.fromJson(gsonString, new TypeToken<List<Map<String, T>>>() {
			}.getType());
		} catch (Exception e) {
			return null;
		}
		return list;
	}

	public static <T> Map<String, T> changeGsonToMaps(String gsonString) {
		if(TextUtils.isEmpty(gsonString)||"false".equals(gsonString)){
			return null;
		}
		Map<String, T> map = null;
		Gson gson = new Gson();
		try {
			map = gson.fromJson(gsonString, new TypeToken<Map<String, T>>() {
			}.getType());
		} catch (Exception e) {
			return null;
		}
		return map;
	}

	public static final String removeBOM(String data) {
		if (TextUtils.isEmpty(data)) {
			return data;
		}
		if (data.startsWith("\ufeff")) {
			return data.substring(1);
		} else {
			return data;
		}
	}
}
