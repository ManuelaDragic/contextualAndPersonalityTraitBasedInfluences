package com.uniulm.social_media_interventions

import com.android.volley.*


class Helper {
    var volleyRequestQueue: RequestQueue? = null

    // todo change to live backend
//    val BACKEND_URL: String = "http://10.0.2.2:3000/api"
//    val BACKEND_URL: String = "http://134.60.245.174/api"
    val BACKEND_URL: String ="http://134.60.245.93/api"
    val TAG = "HELPER"

    /**
     * Sends the request to create a new user for the server using their credentials
     */
    /*@RequiresApi(Build.VERSION_CODES.N)
    fun createNewUser(
        activity: MainActivity,
        context: Context,
        questionnaire: JSONObject,
        history: JSONObject,
        code: String,
        deviceID: String
    ) {

        val registerjson = JSONObject()
            .put("code", code)
            .put("DEVICE_ID", deviceID)
        sendRequest(

            context,
            object : VolleyCallBack {
                override fun onSuccess(response: JSONObject?) {
                    val sharedPref: SharedPreferences = activity.getSharedPreferences(
                        "InfiniteScroll",
                        AppCompatActivity.MODE_PRIVATE
                    )
                    val editor: SharedPreferences.Editor = sharedPref.edit()
                    editor.putString("CODE", code)
                    editor.putBoolean("Registered", true)
                    editor.apply()
                    sendRequest(

                        context,
                        object : VolleyCallBack {
                            override fun onSuccess(response: JSONObject?) {

                            }

                            override fun onFailure(errorResponse: JSONObject?) {

                            }
                        },
                        "/user/questionnaire",
                        "POST",
                        questionnaire.toString()
                    )
                    sendRequest(

                        context,
                        object : VolleyCallBack {
                            override fun onSuccess(response: JSONObject?) {

                            }

                            override fun onFailure(errorResponse: JSONObject?) {

                            }
                        },
                        "/user/history",
                        "POST",
                        history.toString()
                    )
                }

                override fun onFailure(response: JSONObject?) {

                }
            },
            "/register",
            "POST",
            registerjson.toString()
        );
    }

    /**
     * Allows a more simple method to send a request to the server.
     * Underlying is the volley architecture using the http Method, url and body to send
     * the correct request. If no internet connection exists or an error occured,
     * the request is added to a queue and tried again later to not loose any requests.
     */
    @RequiresApi(Build.VERSION_CODES.N)
    fun sendRequest(

        context: Context,
        callBack: VolleyCallBack,
        url: String,
        httpMethod: String,
        body: String?
    ) {
        val sharedPref: SharedPreferences = context.getSharedPreferences(
            "InfiniteScroll",
            AppCompatActivity.MODE_PRIVATE
        )
        val editor: SharedPreferences.Editor = sharedPref.edit()
        Log.e("INTERNET", hasInternetConnectionM(context).toString())

        // hasInternetConnection does not work on emulator
//        if (true) {
        if (!hasInternetConnectionM(context)) {
            appendToRequestQueue(context, httpMethod, url, body)
        } else {
            val savedRequestQueueString = sharedPref.getString("RequestQueue", "true")
            if (savedRequestQueueString != null && savedRequestQueueString != "true") {
                val requestQueueJSON = JSONArray(savedRequestQueueString)
                // todo is there a possibility that this fails? empty queue -> go into for and it cancels?
                sharedPref.edit().remove("RequestQueue").apply()
                for (i in 0 until requestQueueJSON.length()) {
                    val item = requestQueueJSON.getJSONObject(i)
                    var itemBody = ""
                    if (item.has("body")) {
                        itemBody = item.getString("body")
                    }
                    sendRequest(context, object : VolleyCallBack {
                        override fun onSuccess(response: JSONObject?) {}
                        override fun onFailure(errorResponse: JSONObject?) {}
                    }, item.getString("URL"), item.getString("METHOD"), itemBody)
                }
                sharedPref.edit().remove("RequestQueue").apply()
            }

            volleyRequestQueue = Volley.newRequestQueue(context)

            val fullURL = BACKEND_URL + url;

            var methodNr = 0;
            if (httpMethod == "GET") {
                methodNr = Request.Method.GET;
                //0
            } else if (httpMethod == "POST") {
                methodNr = Request.Method.POST;
                // 1
            } else if (httpMethod == "PATCH") {
                methodNr = Request.Method.PATCH;
            }

            try {
                val strReq: StringRequest = object : StringRequest(
                    methodNr,
                    fullURL,
                    Response.Listener { response ->
                        Log.e(TAG, "response: " + response)
                        val json = JSONObject(response)

                        if (url == "/login" || url == "/register" || url == "/refresh") {

                            editor.putString("TOKEN", json.getString("token"))
                            editor.apply()
                        }
                        callBack.onSuccess(JSONObject(response));
                    },
                    Response.ErrorListener { volleyError -> // error occurred
                        var requestQueueJSON = JSONArray();
                        val requestJSON = JSONObject()
                            .put("URL", url)
                            .put("METHOD", httpMethod)
                            .put("body", body)

                        if (url.contains("login") || url.contains("register") || url.contains("refresh")) {
                            // todo add popup like try again later with internet
//                        return;
                        } else {
                            appendToRequestQueue(context, httpMethod, url, body)
                        }

                        var message: String? = null
                        if (volleyError is NetworkError) {
                            message = "Cannot connect to Internet...Please check your connection!"
                        } else if (volleyError is ServerError) {
                            message =
                                "The server could not be found. Please try again after some time!!"
                        } else if (volleyError is AuthFailureError) {
                            message = "Cannot connect to Internet...Please check your connection!"
                        } else if (volleyError is ParseError) {
                            message = "Parsing error! Please try again after some time!!"
                        } else if (volleyError is NoConnectionError) {
                            message = "Cannot connect to Internet...Please check your connection!"
                        } else if (volleyError is TimeoutError) {
                            message = "Connection TimeOut! Please check your internet connection."
                        }

                        if (volleyError.networkResponse != null) {
                            if (volleyError.networkResponse.data != null) {
                                val errorResponseBody =
                                    String(volleyError.networkResponse.data, Charsets.UTF_8)
                                callBack.onFailure(JSONObject(errorResponseBody));
                                Log.e(TAG, "Error Response: $errorResponseBody")
                            }
                        }
                        Log.e(TAG, "problem occurred, volley error: " + volleyError.message)
                        Log.e(TAG, "volley error: " + message)
                    }) {

//            override fun getParams(): MutableMap<String, String> {
//                return parameters;
//            }

                    override fun getBody(): ByteArray {
                        val bodyBytes: ByteArray;
                        if (body != null) {
                            bodyBytes = body.toByteArray(Charsets.UTF_8)
                        } else bodyBytes = "".toByteArray(Charsets.UTF_8)
                        return bodyBytes
                    }

                    override fun getBodyContentType(): String {
                        return "application/json"
                    }

                    @Throws(AuthFailureError::class)
                    override fun getHeaders(): Map<String, String> {

                        val headers: MutableMap<String, String> = HashMap()
                        val sharedPref: SharedPreferences = context.getSharedPreferences(
                            "InfiniteScroll",
                            AppCompatActivity.MODE_PRIVATE
                        )
                        if (url.contains("/user") || url.contains("/refresh")) {
                            val token = sharedPref.getString("TOKEN", "true")
                            if (token != null && token != "true") {
//                        Log.e("BEARER_STORED_TOKEN", token)
                                headers.put("Authorization", "Bearer $token")
                            }
                        }
                        return headers
                    }
                }
//        Log.e("REQUEST", strReq.toString())
                // Adding request to request queue
                volleyRequestQueue?.add(strReq)
            } catch (e: Exception) {

            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private fun hasInternetConnectionM(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network: Network? = connectivityManager.activeNetwork
        val capabilities = connectivityManager
            .getNetworkCapabilities(network)
        return capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) && capabilities.hasCapability(
            NetworkCapabilities.NET_CAPABILITY_VALIDATED
        )
    }

    private fun appendToRequestQueue(context: Context, httpMethod: String, url: String, body: String?) {
        val sharedPref: SharedPreferences = context.getSharedPreferences("InfiniteScroll",0)
        val editor: SharedPreferences.Editor = sharedPref.edit()

        var requestQueueJSON = JSONArray();
        val requestJSON = JSONObject()
            .put("URL", url)
            .put("METHOD", httpMethod)
            .put("body", body)

        if (url.contains("login") || url.contains("register")) {
            // todo add popup like try again later with internet
            return;
        }

        val savedRequestQueueString = sharedPref.getString("RequestQueue", "true")
        println(savedRequestQueueString)
        if (savedRequestQueueString != null && savedRequestQueueString != "true") {
            requestQueueJSON = JSONArray(savedRequestQueueString)
                .put(requestJSON)
        } else {
            requestQueueJSON.put(requestJSON)
        }
        editor.putString("RequestQueue", requestQueueJSON.toString())
        editor.apply()
        println(requestQueueJSON)
    }*/
}