package com.example.mam.services

import com.example.mam.dto.user.UserResponse
import com.example.mam.dto.vo.PageVO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface UserService {
    @GET("users/{userId}")
    suspend fun getUserById(@Path("id") userId: String): Response<UserResponse>
    @PUT("users/{userId}")
    suspend fun updateUser(@Path("id") id: String): Response<UserResponse>
    @DELETE("users/{userId}")
    suspend fun deleteUser(@Path("id") userId: String): Response<Unit>
    @POST("users/{id}/assign-role/{roleId}")
    suspend fun assignRoleToUser(
        @Path("id") userId: Long,
        @Path("roleId") roleId: Long): Response<Unit>
    @GET("users")
    suspend fun getAllUsers(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20,
        @Query("sort") sort: List<String>? = null,
        @Query("filter") filter: String
    ):Response<PageVO<UserResponse>>
}