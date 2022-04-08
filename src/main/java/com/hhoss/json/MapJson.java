package com.hhoss.json;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.sf.json.JSONObject;

import com.google.gson.Gson;

public class MapJson {
	private static final Set<String> ignoreKeys = new HashSet<String>() {
		{
			add("sign");
			add("traceNo");
			add("lgnUserCode");
			add("lgnMerchCode");
		}
	};

	public static <T> String mapToString(Map<String, T> map) {
		if (map != null)
			try {
				return new Gson().toJson(map);
			} catch (Exception e) {
				e.printStackTrace();
			}
		return null;
	}

	public static Map<String, Object> stringToMap(String str) {
		Map<String, Object> map = new HashMap<>();
		try {
			if (str != null && str.length() > 0
					&& str.substring(0, 1).equals("{")) {
				JSONObject json = JSONObject.fromObject(str);
				Set<String> set = json.keySet();
				for (String key : set) {
					map.put(key, (Object) json.optString(key));
				}
				return map;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param str
	 * @return map
	 * */
	public static Map<String, Object> str2Map(String str) {
		try {
			if (str != null) {
				return new Gson()
						.<Map<String, Object>> fromJson(str, Map.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new HashMap<>();
	}

	/**
	 * @param <T>
	 * @param datum
	 *            : the Map for getJson String
	 * @param ignores
	 *            : ignores keys ;which needn't return;
	 * @return
	 */
	public static <T> String getJsonLite(Map<String, T> datum) {
		Map<String, Object> map = new HashMap<>();
		Iterator<Entry<String, T>> i = datum.entrySet().iterator();
		while (i.hasNext()) {
			Entry<String, T> entry = i.next();
			String k = entry.getKey();
			Object v = entry.getValue();
			if (ignoreKeys.contains(k)) {
				continue;
			}
			if (v == null) {
				map.put(k, "");
			} else {
				String s = String.valueOf(v);
				if (s.length() > 192) {
					map.put(k, s.substring(0, 192));
				}
			}
		}
		return mapToString(map);
	}

	/**
	 * @param datum
	 *            : the Map for getJson String
	 * @param ignores
	 *            : ignores keys ;which needn't return;
	 * @return
	 */
	public static <T> String getJsonFull(Map<String, T> datum) {
		Map<String, Object> map = new HashMap<>();
		Iterator<Entry<String, T>> i = datum.entrySet().iterator();
		while (i.hasNext()) {
			Entry<String, T> entry = i.next();
			String k = entry.getKey();
			Object v = entry.getValue();
			if (ignoreKeys.contains(k)) {
				continue;
			}
			map.put(k, v == null ? "" : v);
		}
		return mapToString(map);
	}

}
