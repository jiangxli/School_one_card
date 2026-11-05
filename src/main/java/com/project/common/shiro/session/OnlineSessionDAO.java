package com.project.common.shiro.session;

import java.io.Serializable;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.springframework.beans.factory.annotation.Value;

/**
 * 针对自定义的ShiroSession的db操作
 * 
 * @author
 */
public class OnlineSessionDAO extends EnterpriseCacheSessionDAO
{

    public OnlineSessionDAO()
    {
        super();
    }


    /**
     * 根据会话ID获取会话
     *
     * @param sessionId 会话ID
     * @return ShiroSession
     */
    @Override
    protected Session doReadSession(Serializable sessionId)
    {
       return null;
    }





}
