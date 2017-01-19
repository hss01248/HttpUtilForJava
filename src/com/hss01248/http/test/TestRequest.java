package com.hss01248.http.test;

import com.hss01248.http.test.bean.*;
import com.hss01248.http.util.MyJson;
import com.hss01248.http.util.MyLog;
import com.hss01248.http.wrapper.MyNetApi2;
import com.hss01248.http.wrapper.MyNetListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/19 0019.
 */
public class TestRequest {

    public static void main(String[] args){
        MyNetApi2.init("http://www.qxinli.com:9001/api/");


        testDownload();

    }

    public static void testGetString(){
        MyNetApi2.buildStringRequest("https://kyfw.12306.cn/otn/regist/init")
                .callback(new MyNetListener<String>() {
                    @Override
                    public void onSuccess(String response, String resonseStr) {
                        MyLog.e(response);

                    }

                    @Override
                    public void onError(String error) {
                        super.onError(error);
                        MyLog.e(error);
                    }
                })
                .setIgnoreCer().get();
    }
    public static void testPostString(){
        MyNetApi2.buildStringRequest("article/getArticleCommentList/v1.json")
                .addParams("pageSize","30")
                .addParams("articleId","1738")
                .addParams("pageIndex","1")
                .callback(new MyNetListener<String>() {
                    @Override
                    public void onSuccess(String response, String resonseStr) {
                        MyLog.e(response);
                    }
                }).post();
    }
    public static void testGetJson(){
        MyNetApi2.buildJsonRequest("version/latestVersion/v1.json",GetCommonJsonBean.class)
                               .callback(new MyNetListener<GetCommonJsonBean>() {
                    @Override
                    public void onSuccess(GetCommonJsonBean response, String resonseStr) {
                        MyLog.json(MyJson.toJsonStr(response));
                    }
                })
                .get();

    }
    public static void testPostJson(){
        MyNetApi2.buildJsonRequest("article/getArticleCommentList/v1.json",PostCommonJsonBean.class)
                .addParams("pageSize","30")
                .addParams("articleId","1738")
                .addParams("pageIndex","1")
                .callback(new MyNetListener<PostCommonJsonBean>() {
                    @Override
                    public void onSuccess(PostCommonJsonBean response, String resonseStr) {
                        MyLog.json(MyJson.toJsonStr(response));
                    }
                }).post();
    }

    public static void testGetStandardJson(){
/*	聚合api:笑话大全
                    sort	string	是	类型，desc:指定时间之前发布的，asc:指定时间之后发布的
                    page	int	否	当前页数,默认1
                    pagesize	int	否	每次返回条数,默认1,最大20
                    time	string	是	时间戳（10位），如：1418816972
                    key 	string  您申请的key*/
        Map<String,String> map4 = new HashMap<String,String>();
        map4.put("sort","desc");
        map4.put("page","1");
        map4.put("pagesize","4");
        map4.put("time",System.currentTimeMillis()/1000+"");
        map4.put("key","fuck you");


        MyNetApi2.buildStandardJsonRequest("http://japi.juhe.cn/joke/content/list.from",GetStandardJsonBean.class)
                .addParams("sort","desc")
                .addParams("page","1")
                .addParams("pagesize","4")
                .addParams("time",System.currentTimeMillis()/1000+"")
                .addParams("key","fuck you")
                .setStandardJsonKey("result","error_code","reason")
                .setCustomCodeValue(0,2,-1)

                .callback(new MyNetListener<GetStandardJsonBean>() {
                    @Override
                    public void onSuccess(GetStandardJsonBean response, String resonseStr) {
                        MyLog.json(MyJson.toJsonStr(response));
                    }
                    @Override
                    public void onError(String error) {
                        super.onError(error);
                        MyLog.e(error);
                    }
                })
                .get();
    }
    public static void testPostStandardJson(){
        MyNetApi2.buildStandardJsonRequest("article/getArticleCommentList/v1.json",PostStandardJsonArray.class)
                .addParams("pageSize","30")
                .addParams("articleId","1738")
                .addParams("pageIndex","1")
                .setResponseJsonArray()
                .callback(new MyNetListener<PostStandardJsonArray>() {
                    @Override
                    public void onSuccess(PostStandardJsonArray response, String resonseStr) {
                        //MyLog.json(MyJson.toJsonStr(response));
                    }

                    @Override
                    public void onSuccessArr(List<PostStandardJsonArray> response, String responseStr, String data, int code, String msg) {
                        super.onSuccessArr(response, responseStr, data, code, msg);
                        MyLog.json(MyJson.toJsonStr(response));
                    }
                })
                .post();
    }
    public static void testParamsAsJson(){
        MyNetApi2.buildStandardJsonRequest("http://app.cimc.com:9090/app/appVersion/getLatestVersion",VersionInfo.class)
                .addParams("versionName","1.0.0")
                .addParams("appType","0")
                .setParamsAsJson()
                .setCustomCodeValue(1,2,3)
                .callback(new MyNetListener<VersionInfo>() {


                    @Override
                    public void onSuccess(VersionInfo response, String resonseStr) {
                        MyLog.e(resonseStr);
                    }

                    @Override
                    public void onEmpty() {
                        super.onEmpty();
                    }

                    @Override
                    public void onError(String msgCanShow) {
                        super.onError(msgCanShow);
                        MyLog.e(msgCanShow);
                    }
                })
                .post();
    }
    public static void testDownload(){
        String url = "https://travel.12306.cn/imgs/resources/uploadfiles/images/fed7d5b4-37d3-4f32-bacc-e9b942cb721d_product_W572_H370.jpg";
        String url2 = "http://www.qxinli.com/download/qxinli.apk";
        MyNetApi2.buildDownloadRequest(url2)
                .setIgnoreCer()
                .callback(new MyNetListener() {
                    @Override
                    public void onSuccess(Object response, String onSuccess) {
                        MyLog.e("onSuccess:"+onSuccess);
                    }

                    @Override
                    public void onProgressChange(long fileSize, long downloadedSize) {
                        super.onProgressChange(fileSize, downloadedSize);
                        //MyLog.e("progress:"+downloadedSize+"--filesize:"+fileSize);
                    }

                    @Override
                    public void onError(String msgCanShow) {
                        super.onError(msgCanShow);
                        MyLog.e(msgCanShow);
                    }
                })
                .get();
    }
    public static void testUpload(){
        MyNetApi2.buildUpLoadRequest("http://192.168.1.100:8080/gm/file/q_uploadAndroidApk.do","uploadFile","/storage/emulated/0/qxinli.apk")
                .addFile("uploadFile","/storage/emulated/0/Download/retrofit/qxinli.apk")
                .addParams("uploadFile555","1474363536041.jpg")
                .addParams("api_secret777","898767hjk")
                .callback(new MyNetListener<String>() {
                    @Override
                    public void onSuccess(String response, String resonseStr) {
                        MyLog.e(resonseStr);
                    }

                    @Override
                    public void onError(String error) {
                        super.onError(error);
                        MyLog.e("error:"+error);
                    }

                    @Override
                    public void onProgressChange(long fileSize, long downloadedSize) {
                        super.onProgressChange(fileSize, downloadedSize);
                        MyLog.e("upload onProgressChange:"+downloadedSize + "  total:"+ fileSize +"  progress:"+downloadedSize*100/fileSize);
                    }
                }).post();
    }
}
