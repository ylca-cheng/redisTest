/*
 * ==============================================
 * (C)2018 Shanghai KingstarWinning Corporation. All rights reserved.
 * 项目名称： mmap-task-clinical-3.2-H_MAIN
 * 系统名称： ENGINE3.0
 * 文件名称： SerializeUtil.java
 * 注意事项：
 * Id: 
 * ==============================================
 */
package com.cheng.utils;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/** 
 * <p>Class类功能定义的说明性内容。（请以句号“。”结尾、段中换行请使用“<br/>”符号）</p> 
 * <p>说明内容可以任意书写。 </p> 
 * <p>备注内容可以任意书写 </p> 
 * 
 * @version 1.0
 * @author 公司名 : 上海金仕达卫宁软件科技有限公司（Shanghai KingStar WinningSoft LTD.） <br />
 * 变更履历 <br />
 *  2018-2-27 :  author1: 代码作成<br />
 */
public class SerializeUtil {

    private static Logger logger = LoggerFactory.getLogger(SerializeUtil.class);
    
    /**
     * 序列化对象
     * */
    public static byte[] serializeObject(Object o){
        byte[] result = null;
        ByteArrayOutputStream bos = null;
        ObjectOutputStream oos = null;
        try{
            bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(o);
            result = bos.toByteArray();
        }catch(Exception e){
            logger.error("jedis序列化对象失败："+e.getMessage());
        }finally{
            IOUtils.closeQuietly(bos);
            IOUtils.closeQuietly(oos);
        }
        //logger.info("序列化对象成功");
        return result;
    }
    
    /**
     * 反序列化对象
     * */
    public static Object deserializeObject(byte[] source){
        Object result = null;
        ByteArrayInputStream bis = null;
        ObjectInputStream ois = null;
        try{
            bis = new ByteArrayInputStream(source);
            ois = new ObjectInputStream(bis);
            result = ois.readObject();
        }catch(Exception e){
            logger.error("jedis反序列化对象失败："+e.getMessage());
        }finally{
            IOUtils.closeQuietly(bis);
            IOUtils.closeQuietly(ois);
        }
        return result;
    }
    
}

/* Copyright (C) 2018, 上海金仕达卫宁软件科技有限公司 Project, All Rights Reserved. */