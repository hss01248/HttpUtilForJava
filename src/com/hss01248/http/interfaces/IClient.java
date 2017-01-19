package com.hss01248.http.interfaces;

import com.hss01248.http.config.ConfigInfo;

/**
 * Created by Administrator on 2017/1/19 0019.
 */
public abstract class IClient {
    protected abstract <E> ConfigInfo<E> getString(ConfigInfo<E> info);

    protected abstract  <E> ConfigInfo<E> postString(ConfigInfo<E> info);

    protected abstract   ConfigInfo download(ConfigInfo info);

    protected abstract   ConfigInfo upload(ConfigInfo info);

    public <E> ConfigInfo<E> start(ConfigInfo<E> info){
        return info;
    }




}
