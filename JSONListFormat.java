package com;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONListFormat {

	private static Map<String, Properties> languagesMapsMap = new HashMap<String, Properties>();
	private Logger logger = Logger.getLogger(this.getClass());
	private String languageName = "NameZh-CN";

	private String showMsg = "";
	private String serverCode = "";
	private String serverMsg = "";

	private int dataTotal = 0;
	private int startIndex = 0;
	private int endIndex = 100000;
	private int currentIndex = 0;
	
	//private String parameter;

	private JSONArray jsonArray = new JSONArray();

	// [2017/11/24]
	static {
		try {
			loadLanguage("NameZh-CN", "language-cn.properties");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// [2017/11/24]
	public static void loadLanguage(String langName, String resourcePath) throws Exception {

		InputStream inputStream = JSONListFormat.class.getClassLoader().getResourceAsStream(resourcePath);
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
		Properties languagesProperties = new Properties();
		languagesProperties.load(inputStreamReader);
		inputStream.close();
		inputStreamReader.close();
		languagesMapsMap.put(langName, languagesProperties);
	}

	public String getLanguageName() {
		return languageName;
	}

	public void setLanguageName(String languageName) {
		this.languageName = languageName;
	}

	public static void putLanguage(String languageFileName, Properties p) {
		languagesMapsMap.put(languageFileName, p);
	}

	public String getShowMsg() {
		return showMsg;
	}

	public void setShowMsg(String showMsg) {
		this.showMsg = showMsg;
	}

	public String getServerCode() {
		return serverCode;
	}

	public void setServerCode(String serverCode) {
		this.serverCode = serverCode;
	}

	public String getServerMsg() {
		return serverMsg;
	}

	public void setServerMsg(String str) {
		this.serverMsg = str;
	}

	public JSONArray getDataJsonArray0() {
		return jsonArray;
	}

	public void setLimit(int start, int offset) {
		startIndex = start;
		endIndex = start + offset;
	}
	

	public void addMap(HashMap<String, String> map) throws Exception {
		if (startIndex <= currentIndex & currentIndex < endIndex) {
			jsonArray.put(createJSONObject(map));
		}
		currentIndex++;
	}

	public void addJSONObject(JSONObject jObject) throws Exception {
		if (startIndex <= currentIndex & currentIndex < endIndex) {
			jsonArray.put(jObject);
		}
		currentIndex++;
	}

	// 新加 的
	public void addJSONArray(JSONArray jArray) throws Exception {
		if (startIndex <= currentIndex & currentIndex < endIndex) {
			jsonArray.put(jArray);
		}
		currentIndex++;
	}

	// 直接加入JSONArray
	public void setJsonArray(JSONArray jsonArray) {
		this.jsonArray = jsonArray;
	}

	public static JSONObject createJSONObject(HashMap<String, String> map) throws Exception {

		JSONObject reposeDataObject = new JSONObject();

		for (String key : map.keySet()) {
			reposeDataObject.put(key, map.get(key));
		}

		return reposeDataObject;

	}

	public void setDataTotal(int dataCount) {
		this.dataTotal = dataCount;
	}

	@Override
	public String toString() {
		try {

			if (serverMsg == "")
				serverMsg = "success";
			if (showMsg == "") {
				if (languageName == null)
					languageName = "";
				Properties languages = languagesMapsMap.get(languageName);
				if (languages != null)
					showMsg = languages.getProperty(serverMsg);
				if (showMsg == null) {
					showMsg = "【" + serverMsg + "】";
					logger.error("JSONListFormat:错误码提示不存在：" + showMsg);
				}

			}

			JSONObject jsonObject = new JSONObject();

			jsonObject.put("serverMsg", serverMsg);
			jsonObject.put("serverCode", serverCode);
			jsonObject.put("dataTotal", dataTotal);
			jsonObject.put("showMsg", showMsg);

			if (dataTotal == 0) {
				jsonObject.put("dataTotal", currentIndex);
			}
			jsonObject.put("data", jsonArray);

			return jsonObject.toString(4);

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return "";
	}

	public static void main(String[] args) {

		JSONListFormat js = new JSONListFormat();
		System.out.println(js.toString());
	}

}
