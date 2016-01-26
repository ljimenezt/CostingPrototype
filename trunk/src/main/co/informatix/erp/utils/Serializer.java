package co.informatix.erp.utils;

import java.io.Serializable;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * This class handles the transformation of a string JSON format to an object
 * and vice versa
 * 
 * @author Gabriel.Moreno
 */
@SuppressWarnings("serial")
public class Serializer implements Serializable {

	/**
	 * Becomes an object to JSON format
	 * 
	 * @param object
	 *            : Object to convert in JSON format.
	 * 
	 * @return String in JSON format.
	 */
	public static String serialize(Object object) {
		Gson gson = new GsonBuilder().create();
		return gson.toJson(object);
	}

	/**
	 * Becomes a JSON format to a object
	 * 
	 * @param json
	 *            : String in JSON format.
	 * @param javaClass
	 *            : Class than represent the JSON.
	 * 
	 * @return Java class list than represent the JSON.
	 */
	public static <T> T deserialize(String json, Class<T> javaClass) {
		return (T) new Gson().fromJson(json, javaClass);
	}

	/**
	 * Becomes a JSON format to a object
	 * 
	 * @param json
	 *            : String in JSON format.
	 * @param typeList
	 *            : Type than represent the JSON.
	 * 
	 * @return Java class list than represent the JSON.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T deserialize(String json, Type typeList) {
		return (T) new Gson().fromJson(json, typeList);
	}
}