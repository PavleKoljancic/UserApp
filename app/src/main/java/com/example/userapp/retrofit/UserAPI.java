package com.example.userapp.retrofit;

import com.example.userapp.models.Document;
import com.example.userapp.models.DocumentType;
import com.example.userapp.models.Route;
import com.example.userapp.models.ScanInteraction;
import com.example.userapp.models.TicketType;
import com.example.userapp.models.Transaction;
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
    @POST("api/user/files/upload/document&userId={UserId}&DocumentTypeId={DocumentTypeId}")
    Call<Boolean> uploadDocument(@Part MultipartBody.Part filePart,
                                           @Path("UserId") Integer userId, @Path("DocumentTypeId") Integer DocumentName,
                                 @Header("Authorization") String BarerToken);
    @GET("api/user/files/get/document&userId={UserId}&DocumentId={DocumentId}")
    public Call<ResponseBody> getDocument(@Path("UserId") Integer userId, @Path("DocumentId") Integer DocumentId, @Header("Authorization") String BarerToken);




    //Tickets
    @GET("/api/tickets/getInUseTickets/pagesize={pagesize}size={size}")
    public Call<List<TicketType>> getTicketsInUse(@Path("pagesize") int page,
                                                                @Path("size") int size,@Header("Authorization") String BarerToken);


    @POST("/api/users/getUserTickets")
    public Call<List<UserTicket>> getUserTickets(@Body User user, @Header("Authorization") String BarerToken);


    @GET("/api/ticketRequests/addTicketRequest={ticketTypeId}&UserId={UserID}&DocumentId={DocumentId}")
    public Call<String> addTicketRequest(@Path("ticketTypeId") Integer ticketTypeId, @Path("UserID") Integer userId, @Path("DocumentId") Integer documentId,@Header("Authorization") String BarerToken);

    @GET("/api/tickets/getAllTickets/pagesize={pagesize}size={size}")
    public Call<List<TicketType>> getAllTicketTypes(@Path("pagesize") Integer pagesize, @Path("size") Integer size,@Header("Authorization") String BarerToken);

    @GET("/api/users/getUserKeyById={Id}")
    public  Call<String> getUserKey(@Path("Id") Integer Id,@Header("Authorization") String BarerToken);

    @GET("/api/documents/validDocumentType")
    public Call<List<DocumentType>> getValidDocumentTypes(@Header("Authorization") String BarerToken);
    @GET("/api/documents/documents&userId={UserId}")
    public Call<List<Document>> getUserDocuemnts(@Path("UserId") Integer Id,@Header("Authorization") String BarerToken);

    //Transactions and Interactions

    @GET("/api/terminals/ByUserIdGetScanInterractions={UserId}")
    public Call<List<ScanInteraction>> getUserScanInteractions(@Path("UserId") Integer Id, @Header("Authorization") String BarerToken);

    @GET("/api/routes/getAll")
    public Call<List<Route>> getRoutes(@Header("Authorization") String BarerToken);

    @GET("/api/transactions/getTransactionsForUser={UserId}")
    public Call<List<Transaction>> getUserTransactions(@Path("UserId") Integer Id, @Header("Authorization") String BarerToken);
}
