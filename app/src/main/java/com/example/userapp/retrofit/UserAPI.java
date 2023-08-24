package com.example.userapp.retrofit;

import com.example.userapp.models.TicketRequest;
import com.example.userapp.models.TicketRequestResponse;
import com.example.userapp.models.TicketType;
import com.example.userapp.models.User;

import com.example.userapp.models.UserTicket;
import com.example.userapp.models.UserWithPassword;

import java.sql.Date;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;


public interface UserAPI {

    //USER


    @POST("/api/users/register")
    public Call<Integer> registerUser(@Body UserWithPassword userLoginData);

    @POST("/api/users/login")
    public Call<String> loginUser(@Body UserWithPassword userLoginData);

    @GET("/api/users/getUserById={Id}")
    public Call<User> getUser(@Path("Id") Integer Id, @Header("Authorization") String BarerToken);

    //FILES
    @Multipart
    @POST("api/user/files/upload/profilepicture&userId={UserId}")
    public Call<Boolean> uploadProfilePicture(@Part MultipartBody.Part image,
                                              @Path("UserId") Integer userId, @Header("Authorization") String BarerToken);

    @GET("api/user/files/get/profilepicture&userId={UserId}")
    Call<ResponseBody> getUserProfilePicture(@Path("UserId") Integer UserId, @Header("Authorization") String BarerToken);


    @GET("api/user/files/get/getNextPossibleChangeDate&userId={UserId}")
    Call<Date>  whenIsNextProfilePictureChangePossible(@Path("UserId") Integer userId,@Header("Authorization") String BarerToken);
    @Multipart
    @POST("api/user/files/upload/document&userId={UserId}&DocumentName={DocumentName}")
    Call<Boolean> uploadDocument(@Part MultipartBody.Part filePart,
                                           @Path("UserId") Integer userId, @Path("DocumentName") String DocumentName,
                                 @Header("Authorization") String BarerToken);
    @GET("api/user/files/get/document&userId={UserId}&DocumentName={DocumentName}")
    public Call<ResponseBody> getDocument(@Path("UserId") Integer userId, @Path("DocumentName") String DocumentName, @Header("Authorization") String BarerToken);


    @GET("api/user/files/remove/document&userId={UserId}&DocumentName={DocumentName}")
    public Call<Boolean> removeDocument(@Path("UserId") Integer userId,
                                        @Path("DocumentName") String DocumentName,
                                        @Header("Authorization") String BarerToken);

    //Tickets
    @GET("/api/tickets/getInUseTickets/pagesize={pagesize}size={size}")
    public Call<List<TicketType>> getTicketsInUse(@Path("pagesize") int page,
                                                                @Path("size") int size,@Header("Authorization") String BarerToken);


    @POST("/api/users/getUserTickets")
    public Call<List<UserTicket>> getUserTickets(@Body User user, @Header("Authorization") String BarerToken);
    @GET("/api/ticketRequests/getTicketRequestByUserId={userId}/pagesize={pagesize}size={size}")
    public Call<List<TicketRequest>> getUnprocessTicketRequests(
            @Path("userId") Integer userId, @Path("pagesize") Integer page,
            @Path("size") Integer size, @Header("Authorization") String BarerToken);

    @GET("/api/ticketRequests/getTicketResponseByUserId={userId}/pagesize={pagesize}size={size}")
    public Call<List<TicketRequestResponse>> getTicketResponseByUserId(
            @Path("userId") Integer userId, @Path("pagesize") Integer page,
            @Path("size") Integer size, @Header("Authorization") String BarerToken);
    @GET("/api/ticketRequests/addTicketRequest={ticketTypeId}&UserId={UserID}")
    public Call<String> addTicketRequest(@Path("ticketTypeId") Integer ticketTypeId, @Path("UserID") Integer userId,@Header("Authorization") String BarerToken);

    @GET("/api/tickets/getAllTickets/pagesize={pagesize}size={size}")
    public Call<List<TicketType>> getAllTicketTypes(@Path("pagesize") Integer pagesize, @Path("size") Integer size,@Header("Authorization") String BarerToken);

}
