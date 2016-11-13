package com.publiccms.service.home;

import java.io.Serializable;

// Generated 2016-11-13 11:38:14 by com.sanluan.common.source.SourceMaker

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.entities.home.HomeArticle;
import com.publiccms.dao.home.HomeArticleDao;
import com.sanluan.common.base.BaseService;
import com.sanluan.common.handler.PageHandler;

@Service
@Transactional
public class HomeArticleService extends BaseService<HomeArticle> {

    @Transactional(readOnly = true)
    public PageHandler getPage(Integer siteId, Long directoryId, Long userId, Boolean disabled, String orderField,
            String orderType, Integer pageIndex, Integer pageSize) {
        return dao.getPage(siteId, directoryId, userId, disabled, orderField, orderType, pageIndex, pageSize);
    }

    public HomeArticle updateStatus(Serializable id, boolean status) {
        HomeArticle entity = dao.getEntity(id);
        if (notEmpty(entity)) {
            entity.setDisabled(status);
        }
        return entity;
    }

    @Autowired
    private HomeArticleDao dao;
}