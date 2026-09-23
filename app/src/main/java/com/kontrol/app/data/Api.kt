package com.kontrol.app.data

import com.google.gson.annotations.SerializedName
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

object ApiConfig { const val BASE_URL = "http://10.0.2.2:8080/api/" }

data class LoginRequest(val email:String,val password:String)
data class RegisterRequest(val name:String,val email:String,val password:String)
data class AuthResponse(val token:String,val user:UserDto)
data class UserDto(val id:Long,val name:String,val email:String)
data class TransactionDto(val id:Long,val type:String,val amount:Double,val description:String?,val date:String,val category:CategoryDto?,val account:AccountDto?)
data class CategoryDto(val id:Long,val name:String,val type:String,val icon:String?)
data class AccountDto(val id:Long,val name:String,val type:String,val balance:Double)
data class TransactionRequest(val type:String,val amount:Double,val description:String?,val date:String,val categoryId:Long?,val accountId:Long?)
data class WorkRecordDto(val id:Long,val date:String,val entryTime:String?,val breakStart:String?,val breakEnd:String?,val exitTime:String?,val workedMinutes:Int,val expectedMinutes:Int)
data class WorkRecordRequest(val date:String,val entryTime:String?,val breakStart:String?,val breakEnd:String?,val exitTime:String?,val expectedMinutes:Int=480)
data class NoteDto(val id:Long,val title:String,val content:String,val favorite:Boolean,val createdAt:String,val updatedAt:String)
data class NoteRequest(val title:String,val content:String,val favorite:Boolean=false)
data class ShoppingListDto(val id:Long,val name:String,val items:List<ShoppingItemDto>)
data class ShoppingItemDto(val id:Long,val name:String,val quantity:String?,val completed:Boolean)
data class ShoppingListRequest(val name:String)
data class ShoppingItemRequest(val name:String,val quantity:String?,val completed:Boolean=false)
data class TaskDto(val id:Long,val title:String,val description:String?,val date:String,val priority:String,val completed:Boolean)
data class TaskRequest(val title:String,val description:String?,val date:String,val priority:String="NORMAL",val completed:Boolean=false)
data class FinanceSummary(val balance:Double,val expensesToday:Double)
data class WorkSummary(val workedMinutes:Int,val expectedMinutes:Int)
data class RoutineSummary(val totalTasks:Int,val completedTasks:Int)
data class ShoppingSummary(val pendingItems:Int)
data class DashboardDto(val finance:FinanceSummary,val work:WorkSummary,val routine:RoutineSummary,val shopping:ShoppingSummary)

interface KontrolApi {
 @POST("auth/login") suspend fun login(@Body body:LoginRequest):Response<AuthResponse>
 @POST("auth/register") suspend fun register(@Body body:RegisterRequest):Response<AuthResponse>
 @GET("dashboard") suspend fun dashboard():Response<DashboardDto>
 @GET("transactions") suspend fun transactions():Response<List<TransactionDto>>
 @POST("transactions") suspend fun createTransaction(@Body body:TransactionRequest):Response<TransactionDto>
 @GET("work-records/{date}") suspend fun work(@Path("date") date:String):Response<WorkRecordDto?>
 @GET("work-records") suspend fun workHistory():Response<List<WorkRecordDto>>
 @POST("work-records") suspend fun createWork(@Body body:WorkRecordRequest):Response<WorkRecordDto>
 @PUT("work-records/{id}") suspend fun updateWork(@Path("id") id:Long,@Body body:WorkRecordRequest):Response<WorkRecordDto>
 @GET("notes") suspend fun notes():Response<List<NoteDto>>
 @POST("notes") suspend fun createNote(@Body body:NoteRequest):Response<NoteDto>
 @PUT("notes/{id}") suspend fun updateNote(@Path("id") id:Long,@Body body:NoteRequest):Response<NoteDto>
 @DELETE("notes/{id}") suspend fun deleteNote(@Path("id") id:Long):Response<Unit>
 @GET("shopping-lists") suspend fun shopping():Response<List<ShoppingListDto>>
 @POST("shopping-lists") suspend fun createShopping(@Body body:ShoppingListRequest):Response<ShoppingListDto>
 @POST("shopping-lists/{id}/items") suspend fun createItem(@Path("id") id:Long,@Body body:ShoppingItemRequest):Response<ShoppingItemDto>
 @PUT("shopping-items/{id}") suspend fun updateItem(@Path("id") id:Long,@Body body:ShoppingItemRequest):Response<ShoppingItemDto>
 @GET("tasks") suspend fun tasks():Response<List<TaskDto>>
 @POST("tasks") suspend fun createTask(@Body body:TaskRequest):Response<TaskDto>
 @PUT("tasks/{id}") suspend fun updateTask(@Path("id") id:Long,@Body body:TaskRequest):Response<TaskDto>
}

object ApiFactory {
 lateinit var tokenProvider:()->String?
 val api:KontrolApi by lazy {
  val interceptor=Interceptor { chain ->
   val token=if(::tokenProvider.isInitialized) tokenProvider() else null
   val request=chain.request().newBuilder().apply { if(!token.isNullOrBlank()) addHeader("Authorization","Bearer $token") }.build()
   chain.proceed(request)
  }
  val client=OkHttpClient.Builder().addInterceptor(interceptor).build()
  Retrofit.Builder().baseUrl(ApiConfig.BASE_URL).client(client).addConverterFactory(GsonConverterFactory.create()).build().create(KontrolApi::class.java)
 }
}
