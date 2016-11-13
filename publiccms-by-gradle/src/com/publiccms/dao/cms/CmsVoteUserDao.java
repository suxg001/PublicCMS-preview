package com.publiccms.dao.cms;

// Generated 2016-3-3 17:46:06 by com.sanluan.common.source.SourceMaker

import java.util.Date;

import org.springframework.stereotype.Repository;

import com.publiccms.entities.cms.CmsVoteUser;
import com.sanluan.common.base.BaseDao;
import com.sanluan.common.handler.PageHandler;
import com.sanluan.common.handler.QueryHandler;

@Repository
public class CmsVoteUserDao extends BaseDao<CmsVoteUser> {
    public PageHandler getPage(Integer lotteryId, Long userId, 
                String ip, Date startCreateDate, Date endCreateDate, 
                String orderType, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from CmsVoteUser bean");
        if (notEmpty(lotteryId)) {
            queryHandler.condition("bean.lotteryId = :lotteryId").setParameter("lotteryId", lotteryId);
        }
        if (notEmpty(userId)) {
            queryHandler.condition("bean.userId = :userId").setParameter("userId", userId);
        }
        if (notEmpty(ip)) {
            queryHandler.condition("bean.ip = :ip").setParameter("ip", ip);
        }
        if (notEmpty(startCreateDate)) {
            queryHandler.condition("bean.createDate > :startCreateDate").setParameter("startCreateDate", startCreateDate);
        }
        if (notEmpty(endCreateDate)) {
            queryHandler.condition("bean.createDate <= :endCreateDate").setParameter("endCreateDate", endCreateDate);
        }
        if("asc".equalsIgnoreCase(orderType)){
            orderType = "asc";
        }else{
            orderType = "desc";
        }
        queryHandler.order("bean.createDate " + orderType);
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected CmsVoteUser init(CmsVoteUser entity) {
        return entity;
    }

}