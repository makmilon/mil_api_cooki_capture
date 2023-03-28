package com.milon.volleytest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.android.volley.*
import com.android.volley.Request.Method.POST
import com.milon.volleytest.databinding.ActivityMainBinding
import com.android.volley.toolbox.Volley

import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import okhttp3.Cookie
import org.json.JSONObject
import java.lang.reflect.Method
import java.net.CookieManager
import java.net.CookiePolicy
import java.net.HttpCookie


class MainActivity : AppCompatActivity() {

    var repoCode: String?=null
    var logCode: String?=null

    lateinit var binding: ActivityMainBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener {
            tokenApi()
        }

        binding.button2.setOnClickListener {
           // getResponseApi()
           cookiesApi()
        }

    }

    private fun getResponseApi() {
        val url = "https://www.v1.koushiknaikel.in/user/login?_format=json"
        val headers = HashMap<String, String>()
        headers["Content-Type"] = "application/json"
        headers["X-CSRF-Token"] = repoCode.toString()
        val jsonBody = JSONObject()
        jsonBody.put("name", "dev")
        jsonBody.put("pass", "asklfTh,isy&4KDG%")
        val jsonObjectRequest = object : JsonObjectRequest(
            POST,
            url,
            jsonBody,
            Response.Listener { response ->
                // handle response
                logCode= response.toString()
                binding.textView2.text= logCode
                // Toast.makeText(this@MainActivity, response.toString(), Toast.LENGTH_SHORT).show()
            },
            Response.ErrorListener { error ->
                // handle error
            }) {
            override fun getHeaders(): MutableMap<String, String> {
                return headers
            }
        }
        val cookieManager = CookieManager()
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL)
        jsonObjectRequest.setShouldCache(false)
        jsonObjectRequest.setRetryPolicy(DefaultRetryPolicy(
            0,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        )
        //  jsonObjectRequest.setHeaders(headers)
        //   jsonObjectRequest.setCookieManager(cookieManager)
        val queue = Volley.newRequestQueue(this)
        queue.add(jsonObjectRequest)
        val cookieStore = cookieManager.cookieStore
        val cookies = cookieStore.cookies
        for (cookie in cookies) {
            if (cookie.name == "Name") {
                val cookieValue = cookie.value
                Toast.makeText(this@MainActivity, cookieValue.toString(), Toast.LENGTH_SHORT).show()
                break
            }
        }


    }

    private fun tokenApi() {
        val requestQueue = Volley.newRequestQueue(this)

        val url = "https://www.v1.koushiknaikel.in/session/token"
        val request = StringRequest(Request.Method.GET, url, Response.Listener {
                response ->
            repoCode= response.toString()
            binding.textView.text= repoCode
        }, Response.ErrorListener {
                error ->
            // Handle error response
        })

        requestQueue.add(request)

    }

    private fun cookiesApi(){

        // Instantiate the RequestQueue
        val queue = Volley.newRequestQueue(this)

// Define the URL and request body
        val url = "https://www.v1.koushiknaikel.in/user/login?_format=json"
        val requestBody = JSONObject()
        requestBody.put("name", "dev")
        requestBody.put("pass", "asklfTh,isy&4KDG%")

// Define the request headers
        val headers = HashMap<String, String>()
        headers["Content-Type"] = "application/json"
        headers["X-CSRF-Token"] = repoCode.toString()

// Create a JsonObjectRequest with the request method, URL, request body, and headers
        val request = object : JsonObjectRequest(
            Request.Method.POST,
            url,
            requestBody,
            Response.Listener { response ->
                // Handle the response
                // ...
               binding.textView2.text= response.toString()
              // Toast.makeText(this@MainActivity, response.toString(), Toast.LENGTH_SHORT).show()
            },
            Response.ErrorListener { error ->
                // Handle the error
                // ...
            }) {

            // Override the getHeaders method to return the request headers
            override fun getHeaders(): MutableMap<String, String> {
                return headers
            }

            override fun parseNetworkResponse(response: NetworkResponse?): Response<JSONObject> {
                // Call the super method to parse the response
                val parsedResponse = super.parseNetworkResponse(response)

                // Get the cookies from the response headers
                val cookieHeaders = response?.headers?.get("Set-Cookie")
               // Toast.makeText(this@MainActivity, cookieHeaders, Toast.LENGTH_SHORT).show()
              Log.d("Cookies", "Response Headers: ${response?.headers}")
              Log.d("Cookies", "Set-Cookie Headers: $cookieHeaders")
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.textView3.text= cookieHeaders
                    //Toast.makeText(applicationContext, cookieHeaders, Toast.LENGTH_SHORT).show()
                }, 1000)
           //    Toast.makeText(applicationContext, cookieHeaders.toString(), Toast.LENGTH_SHORT).show()
                if (cookieHeaders != null) {
                    val cookieManager = CookieManager()
                    cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL)

                    // Set the cookies in the cookie manager
                    HttpCookie.parse(cookieHeaders).forEach { cookie ->
                        cookieManager.cookieStore.add(null, cookie)
                    }

                    // Save the cookies to a shared preference or other storage mechanism
                    val cookieStore = cookieManager.cookieStore
                    val cookies = cookieStore.cookies
                    for (cookie in cookies) {
                        if (cookie.name == "Name") {
                            val cookieValue = cookie.value
                           Log.d("Cookies", "Cookie value: $cookieValue")
                           // Toast.makeText(this@MainActivity, cookieValue.toString(), Toast.LENGTH_SHORT).show()
                            break
                        }
                    }
                }

                // Return the parsed response
                return parsedResponse
            }

        }

// Add the request to the queue
        queue.add(request)


    }
    
    private fun cookiesAPi1(){

    }



}