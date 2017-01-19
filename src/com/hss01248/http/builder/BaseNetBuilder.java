package com.hss01248.http.builder;


import com.hss01248.http.config.ConfigInfo;
import com.hss01248.http.config.NetDefaultConfig;
import com.hss01248.http.util.TextUtils;
import com.hss01248.http.util.Tool;
import com.hss01248.http.wrapper.MyNetListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/16 0016.
 */
public class BaseNetBuilder<T> {

    public Map<String,String> params = new HashMap<String,String>();
    public Map<String,String> headers = new HashMap<String,String>();
    public MyNetListener<T> listener;
    public String url;
    public int method ;
    public int type ;//= ConfigInfo.TYPE_STRING;

    public BaseNetBuilder(){
        headers = new HashMap<String,String>();
        if(TextUtils.isNotEmpty(NetDefaultConfig.USER_AGENT)){
            headers.put("User-Agent", NetDefaultConfig.USER_AGENT);
        }

        headers.put("Accept","*/*");
        headers.put("Connection","Keep-Alive");


    }



    //todo 以下是http请求基本组成
    public BaseNetBuilder<T> addHeader(String key,String value){
        if(key!=null && value!=null){
            headers.put(key,value);
        }

        return this;
    }


    /**
     * 在此处完成urlencode功能
    * */
    public BaseNetBuilder<T> addParams(String key,String value){

        if(key!=null && value!=null){
            params.put(Tool.urlEncode(key),Tool.urlEncode(value));
        }
        return this;
    }

    public BaseNetBuilder<T> callback(MyNetListener<T> listener){
        this.listener = listener;
        return this;
    }

    public BaseNetBuilder<T> url(String url ){
        this.url = url;
        return this;
    }

    public ConfigInfo<T> get(){
        method = NetDefaultConfig.Method.GET;
        //client.start(this);
      return   execute();

    }

    public ConfigInfo<T> post(){
        method = NetDefaultConfig.Method.POST;
        // client.start(this);
        return  execute();

    }

    protected ConfigInfo<T> execute(){
        return new ConfigInfo<T>(this);
    }





    //todo 以下是缓存控制策略
    public boolean shouldReadCache = false;
    public boolean shouldCacheResponse = false;
    public long cacheTime = NetDefaultConfig.CACHE_TIME; //单位秒
    public boolean isFromCache = false;//内部控制,不让外部设置
    /**
     * 只支持String和json类型的请求,不支持文件下载的缓存.
     * @param shouldReadCache 是否先去读缓存
     * @param shouldCacheResponse 是否缓存response  内部已做判断,只会缓存状态是成功的那些请求
     * @param cacheTimeInSeconds 缓存的时间,单位是秒
     * @return
     *

     */
    public BaseNetBuilder<T> setCacheControl(boolean shouldReadCache,boolean shouldCacheResponse,long cacheTimeInSeconds){
        this.shouldReadCache = shouldReadCache;
        this.shouldCacheResponse = shouldCacheResponse;
        this.cacheTime = cacheTimeInSeconds;
        return this;

    }




    //todo 以下是超时以及重试策略
    public int retryCount = NetDefaultConfig.RETRY_TIME;
    public int timeout = NetDefaultConfig.TIME_OUT;

    public void setTagForCancle(Object tagForCancle) {
        this.tagForCancle = tagForCancle;
    }

    public Object tagForCancle;

    public BaseNetBuilder<T> setRetryCount(int retryCount){
        this.retryCount = retryCount;
        return this;
    }

    public BaseNetBuilder<T> setTimeout(int timeoutInMills){
        this.timeout = timeoutInMills;
        return this;
    }

    //TODO https自签名证书的处理策略:单个请求是否忽略
    public boolean ignoreCer = false;

    public BaseNetBuilder<T> setIgnoreCer() {
        this.ignoreCer = true;
        return this;
    }

    public boolean isIgnoreCer() {
        return ignoreCer;
    }



    //TODO token身份验证字段的拼接
    public boolean isAppendToken = false;//默认没有token验证
    public boolean isInHeaderOrParam = false;//默认在参数体中传递
    public BaseNetBuilder<T> setIsAppendToken(boolean isAppendToken,boolean isInHeaderOrParam){
        this.isAppendToken = isAppendToken;
        this.isInHeaderOrParam = isInHeaderOrParam;
        return this;
    }

    public BaseNetBuilder<T> setTag(Object object){
        return this;
    }




}
