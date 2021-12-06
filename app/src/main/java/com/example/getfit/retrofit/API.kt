package com.example.getfit.retrofit
import com.example.getfit.data.Activity
import com.example.getfit.data.Goal
import com.example.getfit.data.Sport
import com.example.getfit.data.User
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface API {

        //user functions
        @GET("/UserController/")
        fun getUsers() : Call<List<User>>

        @Headers("Content-Type: application/json")
        @POST("/UserController")
        fun createUser(
            @Body user: User
        ): Call<ResponseBody?>

        @GET("/UserController/name={name}")
        fun findUserByName(
                @Path("name") name: String?
        ): Call<User>

        @GET("/UserController/{id}")
        fun findUserById(
                @Path("id") id:Int
        ): Call<User>

        @PUT("/UserController/{id}")
        fun updateUser(
                @Path ("id")
                id: Int, @Body user: User
        ): Call<ResponseBody?>


        //goal functions
        @GET("/GoalController/")
        fun getGoals() : Call<List<Goal>>

        @Headers("Content-Type: application/json")
        @POST("/GoalController")
        fun createGoal(
                @Body goal:Goal
        ): Call<ResponseBody?>

        @GET("/GoalController/userId={id}")
        fun getGoalByUSerId(
                @Path("id")
                id:Int
        ): Call<Goal>

        @PUT ("/GoalController/{id}")
        fun updateGoal(
                @Path ("id")
                id: Int, @Body goal: Goal
        ): Call<ResponseBody?>

        @GET("/GoalController/week={date}/{id}")
        fun getGoalsForWeek(
                @Path("date") date:Long,
                @Path("id") id:Int
        ): Call<List<Goal>>



        //activity functions
        @GET("/ActivityController/")
        fun getActivities() : Call<List<Activity>>

        @Headers("Content-Type: application/json")
        @POST("/ActivityController")
        fun createActivity(
                @Body activity: Activity
        ): Call<ResponseBody?>

        @GET("/ActivityController/week={date}/{id}")
        fun getActivitiesForWeek(
                @Path("date") date:Long,
                @Path("id") id:Int
        ): Call<List<Activity>>

        @GET("/ActivityController/userId={id}")
        fun getActivityByUserId(
                @Path("id") id:Int
        ): Call<List<Activity>>


        //sport functions
        @GET("/SportController/")
        fun getSports() : Call<List<Sport>>

        @Headers("Content-Type: application/json")
        @POST("/SportController")
        fun createSport(
                @Body sport: Sport
        ): Call<ResponseBody?>

}