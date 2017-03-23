package com.hss01248.http.util;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Created by Administrator on 2017/1/7 0007.
 */
public class MyLog {

    private static Logger logger = init();

    private static Logger init() {
        Logger logger =  Logger.getLogger(MyLog.class);
       // PropertyConfigurator.configure("I:\\dev\\Spark\\lib\\log4j.properties");
        PropertyConfigurator.configure("E:\\ASprogects\\hss01248\\springMVCDemo\\config\\log4j.properties");
        return logger;
    }



    public static void d(Object obj){
        logger.debug(obj);
    }
    public static void e(Object obj){
        logger.error(obj);
    }
    public static void i(Object obj){
        logger.info(obj);
    }
    public static void json(Object obj){
        logger.info(MyJson.toJsonStr(obj));
    }

}
