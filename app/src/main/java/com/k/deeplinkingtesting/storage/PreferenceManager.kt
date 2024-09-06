package com.k.deeplinkingtesting.storage

import android.content.Context
import android.content.SharedPreferences
import org.json.JSONArray
import org.json.JSONObject

class PreferenceManager(context : Context) {
    private val PREF_NAME = "datb"
    private  val KEY_JSON_ARRAY = "json_aaray_key"
    private val sharedPreferences : SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    fun addObjectToJsonArray(newObject: JSONObject) {
        try {
            // Retrieve the existing JSON array from SharedPreferences
            val jsonArray = getJsonArray()

            // Check if the new object already exists in the JSON array
            var exists = false
            for (i in 0 until jsonArray.length()) {
                val existingObject = jsonArray.getJSONObject(i)
                if (areObjectsEqual(existingObject, newObject)) {
                    exists = true
                    break
                }
            }

            // If the new object does not exist, add it to the JSON array
            if (!exists) {
                jsonArray.put(newObject)

                // Save the updated JSON array back to SharedPreferences
                val editor = sharedPreferences.edit()
                editor.putString(KEY_JSON_ARRAY, jsonArray.toString())
                editor.apply()
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    private fun areObjectsEqual(obj1: JSONObject, obj2: JSONObject): Boolean {
        val keys1 = obj1.keys()
        val keys2 = obj2.keys()

        while (keys1.hasNext()) {
            val key = keys1.next()
            if (!obj2.has(key) || obj1.get(key) != obj2.get(key)) {
                return false
            }
        }

        while (keys2.hasNext()) {
            val key = keys2.next()
            if (!obj1.has(key) || obj1.get(key) != obj2.get(key)) {
                return false
            }
        }

        return true
    }
    fun getJsonArray(): JSONArray {
        val jsonArrayString = sharedPreferences.getString(KEY_JSON_ARRAY, null)
        return if (jsonArrayString.isNullOrEmpty()) {
            JSONArray()
        } else {
            JSONArray(jsonArrayString)
        }
    }
}