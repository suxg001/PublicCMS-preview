package com.publiccms.service.cms;

// Generated 2015-5-8 16:50:23 by com.sanluan.common.source.SourceMaker

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.entities.cms.CmsModel;
import com.publiccms.dao.cms.CmsModelDao;
import com.sanluan.common.base.BaseService;
import com.sanluan.common.handler.PageHandler;

@Service
@Transactional
public class CmsModelService extends BaseService<CmsModel> {

    @Transactional(readOnly = true)
    public PageHandler getPage(Integer siteId, Integer parentId, Boolean hasChild, Boolean onlyUrl, Boolean hasImages,
            Boolean hasFiles, Boolean disabled, Integer pageIndex, Integer pageSize) {
        return dao.getPage(siteId, parentId, hasChild, onlyUrl, hasImages, hasFiles, disabled, pageIndex, pageSize);
    }

    public CmsModel updateExtendId(Serializable id, Integer extendId) {
        CmsModel entity = getEntity(id);
        if (notEmpty(entity)) {
            entity.setExtendId(extendId);
        }
        return entity;
    }

    @Override
    public void delete(Serializable id) {
        CmsModel entity = getEntity(id);
        if (notEmpty(entity)) {
            entity.setDisabled(true);
        }
    }

    @Autowired
    private CmsModelDao dao;
}