package com.publiccms.dao.home;

// Generated 2016-11-13 11:38:14 by com.sanluan.common.source.SourceMaker

import org.springframework.stereotype.Repository;

import com.publiccms.entities.home.HomeArticleContent;
import com.sanluan.common.base.BaseDao;

@Repository
public class HomeArticleContentDao extends BaseDao<HomeArticleContent> {

    @Override
    protected HomeArticleContent init(HomeArticleContent entity) {
        return entity;
    }

}