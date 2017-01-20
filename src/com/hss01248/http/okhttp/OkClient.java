package com.hss01248.http.okhttp;

import com.hss01248.http.config.ConfigInfo;
import com.hss01248.http.https.HttpsUtil;
import com.hss01248.http.interfaces.IClient;
import com.hss01248.http.okhttp.cookie.CookieManger;
import com.hss01248.http.okhttp.progress.UploadFileRequestBody;
import com.hss01248.http.util.MyJson;
import com.hss01248.http.util.TextUtils;
import com.hss01248.http.util.Tool;
import okhttp3.*;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2017/1/19 0019.
 */
public class OkClient extends IClient {
    private static OkClient okClient;
   private static OkHttpClient client;
    private static OkHttpClient allCerPassClient;


   private OkClient(){

   }

   private OkHttpClient getAllCerPassClient(){
       if(allCerPassClient ==null){
           OkHttpClient.Builder builder = new OkHttpClient.Builder();
           HttpsUtil.setAllCerPass(builder);
           OkClient.allCerPassClient = builder
                   .connectTimeout(6000, TimeUnit.MILLISECONDS)
                   .readTimeout(0,TimeUnit.MILLISECONDS)
                   .writeTimeout(0, TimeUnit.MILLISECONDS)
                   .cookieJar(new CookieManger())
                   .build();
       }
       return allCerPassClient;
   }

    public static OkClient getInstance(){
        if(okClient ==null){
            okClient = new OkClient();
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            HttpsUtil.setHttps(builder);
            OkClient.client = builder
                    .connectTimeout(6000, TimeUnit.MILLISECONDS)
                    .readTimeout(0,TimeUnit.MILLISECONDS)
                    .writeTimeout(0, TimeUnit.MILLISECONDS)
                    .cookieJar(new CookieManger())
                    .build();

        }
        return okClient;
    }





    public <E> ConfigInfo<E> getString(final ConfigInfo<E> info) {
         /* 如果需要参数 , 在url后边拼接 :  ?a=aaa&b=bbb..... */
        Request.Builder builder = new Request.Builder();
        String url = Tool.generateUrlOfGET(info);
        builder.url(url);
        addHeaders(builder,info.headers);

        handleStringRequest(info, builder);
        return info;
    }

    public <E> ConfigInfo<E> postString(ConfigInfo<E> info) {

        Request.Builder builder = new Request.Builder();
        builder.url(info.url);
        addHeaders(builder,info.headers);
        addPostBody(builder,info);

        handleStringRequest(info, builder);
        return info;
    }

    public  ConfigInfo download(final ConfigInfo info) {
        Request.Builder builder = new Request.Builder();
        String url = Tool.generateUrlOfGET(info);
        builder.url(url);
        addHeaders(builder,info.headers);
        //info.listener.registEventBus();
        requestAndHandleResoponse(info, builder, new ISuccessResponse() {
            public void handleSuccess(Call call, Response response) throws IOException{
                Tool.writeResponseBodyToDisk(response.body(),info);
            }
        });

        return info;
    }

    public  ConfigInfo upload(final ConfigInfo info) {
        Request.Builder builder = new Request.Builder();
        builder.url(info.url);
        addHeaders(builder,info.headers);
        //info.listener.registEventBus();
        addUploadBody(builder,info);

               requestAndHandleResoponse(info, builder, new ISuccessResponse() {
            public void handleSuccess(Call call, Response response) throws IOException{
                String str = response.body().string();
                info.listener.onSuccess(str,str);
            }
        });
        return info;
    }





    private void addHeaders(Request.Builder builder, Map<String, String> headers) {
        Headers.Builder headBuilder = new Headers.Builder();
        for(Map.Entry<String,String> header   : headers.entrySet()){

            headBuilder.set(header.getKey(),header.getValue());
        }
        builder.headers(headBuilder.build());
    }

    private FormBody getFormBody(Map<String,String> params) {
        FormBody.Builder builder = new FormBody.Builder();
        for(Map.Entry<String,String> param   : params.entrySet()){
            builder.add(param.getKey(),param.getValue());
        }
        return builder.build();
    }

    private void addPostBody(Request.Builder builder, ConfigInfo info) {
        RequestBody body = null;
        if(TextUtils.isNotEmpty(info.paramsStr)){
            body = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), info.paramsStr);
            builder.post(body);
            return;
        }

        if(info.paramsAsJson){
            String jsonStr = MyJson.toJsonStr(info.params);
            body = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), jsonStr);
        }else {
            body=   getFormBody(info.params);
        }
        builder.post(body);
    }

    private <E> void addUploadBody(Request.Builder builder0, ConfigInfo<E> configInfo) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        if (configInfo.params != null && configInfo.params.size() >0){
            Map<String,String> params = configInfo.params;
            int count = params.size();
            if (count>0){
                Set<Map.Entry<String,String>> set = params.entrySet();
                for (Map.Entry<String,String> entry : set){
                    String key = entry.getKey();
                    String value = entry.getValue();
                    builder.addFormDataPart(key,value);
                }
            }
        }

        if (configInfo.files != null && configInfo.files.size() >0){
            Map<String,String> files = configInfo.files;
            int count = files.size();
            int index=0;

            if (count>0){
                Set<Map.Entry<String,String>> set = files.entrySet();
                for (Map.Entry<String,String> entry : set){
                    String key = entry.getKey();
                    String value = entry.getValue();
                    File file = new File(value);
                    UploadFileRequestBody fileRequestBody = new UploadFileRequestBody(file, Tool.getMimeType(value),configInfo,index);
                    builder.addFormDataPart(key,file.getName(),fileRequestBody);
                    index++;
                }
            }
        }

        //MultipartBody body = builder.build();
        builder0.post(builder.build());
    }

    private <E> void handleStringRequest(final ConfigInfo<E> info, Request.Builder builder) {

        requestAndHandleResoponse(info, builder, new ISuccessResponse() {
            public void handleSuccess(Call call, Response response) throws IOException{
                Tool.parseStringByType(response.body().string(),info);
            }
        });
    }

    private <E> void requestAndHandleResoponse(final ConfigInfo<E> info, Request.Builder builder, final ISuccessResponse successResponse) {
        final Request request = builder.build();
        OkHttpClient theClient;
        if(info.isIgnoreCer()){
            if(allCerPassClient==null){
                allCerPassClient = getAllCerPassClient();
            }
            theClient = allCerPassClient;
        }else {
            theClient = client;
        }
        Call call = theClient.newCall(request);
        info.request = call;
        call.enqueue(new Callback() {
            public void onFailure(Call call, IOException e) {
                info.listener.onError(e.getMessage());
                e.printStackTrace();
            }
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    successResponse.handleSuccess(call,response);
                }else {
                    info.listener.onCodeError("http错误码:"+response.code(),response.message(),response.code());
                }
            }
        });
    }

    interface ISuccessResponse{
        void handleSuccess(Call call, Response response) throws IOException;
    }



}
