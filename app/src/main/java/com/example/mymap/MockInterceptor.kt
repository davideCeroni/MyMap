package com.example.mymap

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull

class MockInterceptor: Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (BuildConfig.DEBUG) {
            val uri = chain.request().url.toUri().toString()
            val responseString = when {
                uri.endsWith("fight-points") -> getListOfReposBeingStarredJson
                else -> ""
            }

            return chain.proceed(chain.request())
                .newBuilder()
                .code(0)
                .protocol(Protocol.HTTP_2)
                .message(responseString)
                .body(ResponseBody.create(
                        "application/json".toMediaTypeOrNull(),
                    responseString.toByteArray()))
                .addHeader("content-type", "application/json")
                .build()
        } else {
            throw IllegalAccessError("MockInterceptor is only meant for Testing Purposes and " +
                    "bound to be used only with DEBUG mode")
        }
    }
}

const val getListOfReposBeingStarredJson = """
[{
	"uuid": 1296269,
	"state": "Italy",
	"city": "Rome",
	"posizione": "14.0, 13.0",
	"user": null,
	"n_questions": 5,
	"created_at": "2011-01-26T19:01:12Z",
	"updated_at": "2011-01-26T19:14:43Z"
}]
"""