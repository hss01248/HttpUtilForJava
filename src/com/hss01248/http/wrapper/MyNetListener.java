package com.hss01248.http.wrapper;


import com.hss01248.http.config.ConfigInfo;
import com.hss01248.http.util.MyLog;

import java.util.List;

/**
 * Created by Administrator on 2016/4/15 0015.
 */
public abstract class MyNetListener<T>  {

    public String url;
    public ConfigInfo configInfo;




    /**
     * called when the request is success bug data is empty
     */
    public  void onEmpty(){}

    public void onPreExecute() {}

    public void onNoNetwork(){
        onError("当前没有网络");
    }

    public void onTimeout(){
        onError("连接超时,请检查网络");
    }



    /** Called when response success. */
    public abstract void onSuccess(T response,String resonseStr);

    public  void onSuccessArr(List<T> response,String resonseStr){

    }

    public  void onSuccessObj(T response,String responseStr,String data,int code,String msg){
            onSuccess(response,responseStr);
    }

    public  void onSuccessArr(List<T> response, String responseStr, String data, int code, String msg){
        onSuccessArr(response,responseStr);
    }





    /**
     * Callback method that an error has been occurred with the
     * provided error code and optional user-readable message.
     */
    public void onError(String msgCanShow) {}


    /**
     * 有错误码的error

     * @param code
     */
    public void onCodeError(String msgCanShow,String hiddenMsg,int code) {
        if (msgCanShow==null || msgCanShow.equals("")){
            onError("错误码为:"+code);
        }else {
            onError(msgCanShow);
        }
    }


    public void onCancel() {}

    public void onUnFound() {
        onError("没有找到该内容");
    }
    public    void onUnlogin(){
        onError("您还没有登录");
    }



    /**
     * 都是B作为单位
     */
    public void onProgressChange(long transPortedBytes, long totalBytes) {
        //MyLog.e("transPortedBytes:"+transPortedBytes+"--totalBytes:"+totalBytes);
    }

    /**
     *
     * @param transPortedBytes
     * @param totalBytes
     * @param fileIndex
     * @param filesCount 总的上传文件数量
     */
    public void onFilesUploadProgress(long transPortedBytes, long totalBytes,int fileIndex,int filesCount) {
        MyLog.e("FilesUploadprogress:"+transPortedBytes+"--totalBytes:"+totalBytes+"--fileIndex:"+fileIndex+"-----filecount:"+filesCount);
        onProgressChange(transPortedBytes,totalBytes);
    }

    public void onFinish(){}


    public void onNetworking() {}

    /** Inform when the cache already use,
     * it means http networking won't execute. */
    public void onUsedCache() {}


    public void onRetry() {}


    /**
     * 只需要在retrofit下载时调用
     */
    /*public void registEventBus(){
        EventBus.getDefault().register(this);
    }

    public void unRegistEventBus(){
        EventBus.getDefault().unregister(this);
    }

    private long lastProgress;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void  onMessage(ProgressEvent event){
        if (event.url.equals(url)){
             if(lastProgress >= event.totalBytesRead){
                return;
            }

            lastProgress = event.totalBytesRead;
            onProgressChange(event.totalLength,event.totalBytesRead);


            //进度更新: 数据太会出现


            if (event.done){
                unRegistEventBus();
                onFinish();
            }
        }
    }*/


}
