/**
 * Copyright (c) 2017-2018 The Elastos Developers
 * <p>
 * Distributed under the MIT software license, see the accompanying file
 * LICENSE or https://opensource.org/licenses/mit-license.php
 */
package org.elastos.controller;

import com.alibaba.fastjson.JSON;
import org.elastos.entity.ReturnMsgEntity;
import org.elastos.exception.ApiInternalException;
import org.elastos.util.JsonUtil;
import org.elastos.util.RetCodeConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;

/**
 * clark
 * <p>
 * 9/3/18
 */
@RestController
@RequestMapping("/api/1")
public class BaseController {
    @Autowired
    private RetCodeConfiguration retCodeConfiguration;
    private Logger logger = LoggerFactory.getLogger(BaseController.class);

    /**
     * call elaService method
     * @param reqBody request body
     * @param clazz Entity class name
     * @param methodName invoked method name
     * @param <T>
     * @return
     */
    protected <T> String call(String reqBody,Class<T> clazz,String methodName,Object service){
        try{

            T entity = JsonUtil.jsonStr2Entity(reqBody,clazz);

            Method m = service.getClass().getDeclaredMethod(methodName,entity.getClass());

            return (String)m.invoke(service,entity);

        }catch (Exception ex){

            ex.printStackTrace();

            logger.error("Hd {} error : {}",methodName, ex);

            long status = 0;

            if (ex instanceof ApiInternalException){
                status = retCodeConfiguration.NTERNAL_ERROR();
            }else {
                status = retCodeConfiguration.BAD_REQUEST();
            }
            return JSON.toJSONString(new ReturnMsgEntity().setResult(ex.getCause().getMessage()).setStatus(status));
        }
    }
}
