package com.taichuan.code.http;

import java.util.LinkedHashMap;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * RESTful请求Service接口
 */
public interface RestService {

    @GET
    Call<String> get(@Url String url, @HeaderMap Map<String, Object> headers, @QueryMap LinkedHashMap<String, Object> params);

    @FormUrlEncoded
    @POST
    Call<String> post(@Url String url, @HeaderMap Map<String, Object> headers, @FieldMap LinkedHashMap<String, Object> params);

    @Multipart
    @POST
    Call<String> postMultipart(@Url String url, @HeaderMap Map<String, Object> headers, @PartMap LinkedHashMap<String, RequestBody> params);

    @POST
    Call<String> postRaw(@Url String url, @HeaderMap Map<String, Object> headers, @Body RequestBody body);

    @PATCH
    Call<String> patch(@Url String url, @HeaderMap Map<String, Object> headers, @PartMap LinkedHashMap<String, Object> params);

    @PATCH
    Call<String> patchRaw(@Url String url, @HeaderMap Map<String, Object> headers, @Body RequestBody body);

    @FormUrlEncoded
    @PUT
    Call<String> put(@Url String url, @HeaderMap Map<String, Object> headers, @FieldMap LinkedHashMap<String, Object> params);

    @PUT
    Call<String> putRaw(@Url String url, @HeaderMap Map<String, Object> headers, @Body RequestBody body);

    @DELETE
    Call<String> delete(@Url String url, @HeaderMap Map<String, Object> headers, @QueryMap LinkedHashMap<String, Object> params);

    @Streaming
    @GET
    Call<ResponseBody> download(@Url String url, @HeaderMap Map<String, Object> headers, @QueryMap LinkedHashMap<String, Object> params);

    @Multipart
    @POST
    Call<String> upload(@Url String url, @HeaderMap Map<String, Object> headers, @Part MultipartBody.Part file);
}
