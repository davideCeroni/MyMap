package com.example.mymap

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import okhttp3.*
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiInterface {
    @GET("fight-points")
    fun getFightPoints(): Call<List<FightPoint>>?

    @GET("/qa/")
    fun getQuestionAnswers(
        @Query("fightpoint_uuid") fightpoint_uuid: String,
        @Query("n_question") n_question: Int,
        ): Call<QuestionAnswers>

    @GET("/auth/user-exist")
    fun getUsers(): Call<ErrorMessage>?

    @GET("/user/info")
    fun getUserInfo(): Call<UserInfo>?

    @GET("/scoreboard")
    fun getStandings(): Call<ArrayList<StandingsRecord>>?

    @POST("/auth/user-create")
    fun createUser(
        @Body user: User
    ): Call<ResponseBody>

    @POST("/user/update")
    fun updateUser(
        @Body user: User
    ): Call<ResponseBody>

    @POST("/fight-points/update-owner")
    fun updateOwner(
        @Body updateOwnerObj: UpdateOwnerObj
    ): Call<ResponseBody>

    @DELETE("/notifications/delete-all")
    fun clearNotifications(): Call<ResponseBody>

    companion object {
        private var BASE_URL = "http://192.168.1.104:3000/"

        private var instance: ApiInterface? = null

        @RequiresApi(Build.VERSION_CODES.N)
        fun create() : ApiInterface {
            val client: OkHttpClient = OkHttpClient.Builder()
                //refresh token
                .authenticator(object: Authenticator{
                override fun authenticate(route: Route?, response: Response): Request {
                    val currentUser = FirebaseAuth.getInstance().currentUser?: return response.request
                    val result = Tasks.await(currentUser.getIdToken(true))
                    MyApplication.instance.token = result.token
                    MyApplication.instance.currentFirebaseUser = currentUser
                    return response.request.newBuilder()
                        .header("Authorization", "Bearer ${result.token}")
                        .build()
                }
            })
                //put token in header
                .addInterceptor { chain->
                    val newRequest: Request = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer ${MyApplication.instance.token}")
                        .build()
                    chain.proceed(newRequest)
                }
                .build()

            if (instance == null) {
                val retrofit = Retrofit.Builder()
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(BASE_URL)
                    .build()
                instance = retrofit.create(ApiInterface::class.java)
            }
            return instance!!
        }
    }
}
