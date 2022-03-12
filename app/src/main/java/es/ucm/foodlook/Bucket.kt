package es.ucm.foodlook

import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.PUT;
import retrofit2.http.Body
import retrofit2.http.Url

public interface BucketInterface {
    @PUT
    fun uploadImage(@Url url:String, @Body image:RequestBody): Call<Void>
}