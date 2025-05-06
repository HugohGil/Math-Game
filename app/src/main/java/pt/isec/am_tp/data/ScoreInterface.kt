package pt.isec.am_tp.data

import pt.isec.am_tp.data.model.Score
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ScoreInterface {
    @GET("/api/scores/leaderboard")
    fun getLeaderboard(): Call<List<Score>>

    @POST("/api/scores")
    fun submitScore(@Body score: Score): Call<Score>
}