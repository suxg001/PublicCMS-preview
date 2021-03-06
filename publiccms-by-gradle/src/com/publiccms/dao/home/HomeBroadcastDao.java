package com.publiccms.dao.home;

// Generated 2016-11-13 11:38:14 by com.sanluan.common.source.SourceMaker

import org.springframework.stereotype.Repository;

import com.publiccms.entities.home.HomeBroadcast;
import com.sanluan.common.base.BaseDao;
import com.sanluan.common.handler.PageHandler;
import com.sanluan.common.handler.QueryHandler;

@Repository
public class HomeBroadcastDao extends BaseDao<HomeBroadcast> {
    public PageHandler getPage(Integer siteId, Long userId, Boolean reposted, Long repostId, Boolean disabled, String orderField,
            String orderType, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from HomeBroadcast bean");
        if (notEmpty(siteId)) {
            queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
        }
        if (notEmpty(userId)) {
            queryHandler.condition("bean.userId = :userId").setParameter("userId", userId);
        }
        if (notEmpty(reposted)) {
            queryHandler.condition("bean.reposted = :reposted").setParameter("reposted", reposted);
        }
        if (notEmpty(repostId)) {
            queryHandler.condition("bean.repostId = :repostId").setParameter("repostId", repostId);
        }
        if (notEmpty(disabled)) {
            queryHandler.condition("bean.disabled = :disabled").setParameter("disabled", disabled);
        }
        if ("asc".equalsIgnoreCase(orderType)) {
            orderType = "asc";
        } else {
            orderType = "desc";
        }
        if (null == orderField) {
            orderField = "";
        }
        switch (orderField) {
        case "scores":
            queryHandler.order("bean.scores " + orderType);
            break;
        case "reposts":
            queryHandler.order("bean.reposts " + orderType);
            break;
        case "comments":
            queryHandler.order("bean.comments " + orderType);
            break;
        case "message":
            queryHandler.order("bean.message " + orderType);
            break;
        case "createDate":
            queryHandler.order("bean.createDate " + orderType);
            break;
        default:
            queryHandler.order("bean.id " + orderType);
        }
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected HomeBroadcast init(HomeBroadcast entity) {
        return entity;
    }

}