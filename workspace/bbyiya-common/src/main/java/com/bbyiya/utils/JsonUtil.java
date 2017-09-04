package com.bbyiya.utils;

import java.io.StringReader;
//import java.lang.reflect.Field;
//import java.lang.reflect.Type;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
import java.util.ArrayList;
//import java.util.Date;
import java.util.List;
//import java.util.Map;
//import java.util.Set;

//import net.sf.json.JSONObject;
//import net.sf.json.JsonConfig;
//import net.sf.json.util.JavaIdentifierTransformer;

import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import com.google.gson.JsonDeserializationContext;
//import com.google.gson.JsonDeserializer;
//import com.google.gson.JsonElement;
//import com.google.gson.JsonParseException;
//import com.google.gson.JsonPrimitive;
//import com.google.gson.JsonSerializationContext;
//import com.google.gson.JsonSerializer;
//import com.google.gson.reflect.TypeToken;
import com.sdicons.json.mapper.JSONMapper;
import com.sdicons.json.mapper.MapperException;
import com.sdicons.json.model.JSONArray;
import com.sdicons.json.model.JSONValue;
import com.sdicons.json.parser.JSONParser;

public class JsonUtil {

	private static Gson gson = null;
	static {
		if (gson == null) {
			gson = new Gson();
		}
	}

	private JsonUtil() {
	}

	/**
	 * JAVA对象转换成JSON字符串
	 * 
	 * @param obj
	 * @return
	 * @throws MapperException
	 */
	public static String objectToJsonStr(Object obj) throws MapperException {
		JSONValue jsonValue = JSONMapper.toJSON(obj);
		String jsonStr = jsonValue.render(false);
		String sreturn = jsonStr.replace("\"basemodle\"", "\"BaseModle\"")
				.replace("\"statusreson\"", "\"StatusReson\"")
				.replace("\"statu\"", "\"Statu\"");
		return sreturn;
	}

	/**
	 * JSON字符串转换成JAVA对象
	 * 
	 * @param jsonStr
	 * @param cla
	 * @return
	 * @throws MapperException
	 * @throws TokenStreamException
	 * @throws RecognitionException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Object jsonStrToObject(String jsonStr, Class<?> cla) {
		Object obj = null;
		try {
			JSONParser parser = new JSONParser(new StringReader(jsonStr));
			JSONValue jsonValue = parser.nextValue();
			if (jsonValue instanceof com.sdicons.json.model.JSONArray) {
				List list = new ArrayList();
				JSONArray jsonArray = (JSONArray) jsonValue;
				for (int i = 0; i < jsonArray.size(); i++) {
					JSONValue jsonObj = jsonArray.get(i);
					try {
						Object javaObj = JSONMapper.toJava(jsonObj, cla);
						list.add(javaObj);
					} catch (Exception ex) {
						continue;
					}

				}
				obj = list;
			} else if (jsonValue instanceof com.sdicons.json.model.JSONObject) {
				obj = JSONMapper.toJava(jsonValue, cla);
			} else {
				obj = jsonValue;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}

	/**
	 * 
	 * @Title: jsonToList
	 * @Description: 将json格式转换成list对象
	 * @param @param jsonStr
	 * @param @return
	 * @return List<?>
	 * @throws
	 */
	public static List<?> jsonToList(String jsonStr) {
		List<?> objList = null;
		if (gson != null) {
			java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<List<?>>() {
			}.getType();
			objList = gson.fromJson(jsonStr, type);
		}
		return objList;
	}
}
