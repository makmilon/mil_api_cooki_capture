package com.milon.volleytest

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject

class DrupalApi(private val context: Context) {

    private val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(context.applicationContext)
    }

    fun login(username: String, password: String, onSuccess: (String) -> Unit, onError: (VolleyError) -> Unit) {
        val url = "https://www.v1.koushiknaikel.in/user/login?_format=json"

        val jsonObject = JSONObject()
        jsonObject.put("name", username)
        jsonObject.put("pass", password)

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, url, jsonObject,
            { response ->
                val sessionName = response.getJSONObject("session_name").getString("value")
                val sessionId = response.getJSONObject("sessid").getString("value")
                onSuccess("$sessionName=$sessionId")
            },
            { error ->
                onError(error)
            })

        requestQueue.add(jsonObjectRequest)
    }

    fun getNodes(sessionId: String, onSuccess: (JSONArray) -> Unit, onError: (VolleyError) -> Unit) {
        val url = "https://www.v1.koushiknaikel.in/user/login?_format=json"
        val jsonArrayRequest = object : JsonArrayRequest(Method.POST, url, null,
            { response ->
                onSuccess(response)
            },
            { error ->
                onError(error)
            }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Cookie"] = sessionId
                return headers
            }
        }

        requestQueue.add(jsonArrayRequest)
    }

}
