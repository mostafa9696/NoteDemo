package com.example.mostafahussien.rx_demo.Network;

import com.example.mostafahussien.rx_demo.Model.Note;
import com.example.mostafahussien.rx_demo.Model.User;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by Mostafa Hussien on 04/06/2018.
 */

public interface ApiService {
    // Register new user
    @FormUrlEncoded                 //  The @Body paramater  it should be only parameter. It is helpful when you have already a JsonObject and you want to send it as it with you api call.
    @POST("notes/user/register")    //  The @Field parameter works only with a POST. @Field requires a mandatory parameter. In cases when @Field is optional, we can use @Query instead and pass a null value.
    Single<User> register(@Field("device_id") String deviceId);     // Single completes with a value successfully or an error, always makes sure there is an emission

    // Create note
    @FormUrlEncoded
    @POST("notes/new")
    Single<Note> createNote(@Field("note") String note);

    // Fetch all notes
    @GET("notes/all")
    Single<List<Note>> fetchAllNotes();

    // Update single note
    @FormUrlEncoded
    @PUT("notes/{id}")      // The @Path paramter work with {var} form, @Query use after ?
    Completable updateNote(@Path("id") int noteId, @Field("note") String note);     // Completable just signals if it has completed successfully or with an error.

    // Delete note
    @DELETE("notes/{id}")
    Completable deleteNote(@Path("id") int noteId);
}

// @Multipart with @Post request uses to send file to server like image with description, audio, video
// Call<ResponseBody> ResponseBody use when there is not json mapping object
