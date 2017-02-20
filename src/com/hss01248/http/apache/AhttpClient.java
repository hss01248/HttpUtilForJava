package com.hss01248.http.apache;

import com.hss01248.http.config.ConfigInfo;
import com.hss01248.http.interfaces.IClient;
import com.hss01248.http.util.MyLog;
import com.hss01248.http.util.Tool;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/24 0024.
 */
public class AhttpClient extends IClient{
    private static PoolingHttpClientConnectionManager cm;
    private static String EMPTY_STR = "";
    private static String UTF_8 = "UTF-8";

    private static void init() {
        if (cm == null) {
            cm = new PoolingHttpClientConnectionManager();
            cm.setMaxTotal(50);// 整个连接池最大连接数
            cm.setDefaultMaxPerRoute(5);// 每路由最大连接数，默认值是2
        }
    }

    /**
     * 通过连接池获取HttpClient
     *
     * @return
     */
    private static CloseableHttpClient getHttpClient() {

        return HttpClients.custom().setConnectionManager(cm).build();
    }





    private static AhttpClient instance;

    private AhttpClient(){
        init();
    }

    public static AhttpClient getInstance(){
        if(instance==null){
            instance =new AhttpClient();
           // client= new DefaultHttpClient();

        }
        return instance;
    }

    protected <E> ConfigInfo<E> getString(ConfigInfo<E> info) {
        HttpGet request=new HttpGet(Tool.generateUrlOfGET(info));
        try {

            HttpResponse response= getHttpClient().execute(request);
            //打印服务器返回的状态
            MyLog.i(response.getStatusLine());

            if (handleHttpCodeError(info, response)){
                request.releaseConnection();
                return info;
            }

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            response.getEntity().writeTo(byteArrayOutputStream);
           String str =  byteArrayOutputStream.toString(info.responseCharset);
            MyLog.i(str);
            info.listener.onSuccess((E) str,str);
            request.releaseConnection();

            //打印返回的信息
            // System.out.println(response.getEntity().);
            //response.getEntity().writeTo(new FileOutputStream(new File("E:\\1.txt")));
        } catch (IOException e) {
            e.printStackTrace();
            info.listener.onError(e.getMessage());
            request.releaseConnection();
        }
        return info;
    }

    private <E> boolean handleHttpCodeError(ConfigInfo<E> info, HttpResponse response) {
        int code = response.getStatusLine().getStatusCode();
        if(code !=200){
            info.listener.onError("http错误码："+code);
            return true;
        }
        return false;
    }

    protected <E> ConfigInfo<E> postString(ConfigInfo<E> info) {

        try {

        HttpPost  httpPost = new HttpPost(info.url);



        //todo 参数为json，参数已经是key=value形式的
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        Iterator<Map.Entry<String, String>> iterator = info.params.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<String,String> elem =  iterator.next();
            list.add(new BasicNameValuePair(Tool.urlEncode(elem.getKey()),Tool.urlEncode(elem.getValue())));
        }
        if(list.size() > 0){
            UrlEncodedFormEntity entity = null;
            entity = new UrlEncodedFormEntity(list,"utf-8");
            httpPost.setEntity(entity);
        }

        HttpResponse response = getHttpClient().execute(httpPost);
        if (handleHttpCodeError(info, response)) return info;
        HttpEntity resEntity = response.getEntity();
        if(resEntity != null){
            String result = EntityUtils.toString(resEntity,info.responseCharset);
            Tool.parseStringByType(result,info);
        }
        return info;
    }catch(Exception ex){
        ex.printStackTrace();
        info.listener.onError(ex.getMessage());
        return info;
    }
}

    protected ConfigInfo download(ConfigInfo info) {
        return null;
    }

    protected ConfigInfo upload(ConfigInfo info) {
        return null;
    }
}
