package com.publiccms.service.cms;

// Generated 2016-1-25 10:53:21 by com.sanluan.common.source.SourceMaker

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.entities.cms.CmsContentRelated;
import com.publiccms.dao.cms.CmsContentRelatedDao;
import com.publiccms.views.pojo.CmsContentRelatedStatistics;
import com.sanluan.common.base.BaseService;
import com.sanluan.common.handler.PageHandler;

@Service
@Transactional
public class CmsContentRelatedService extends BaseService<CmsContentRelated> {
    private String[] ignoreProperties = new String[] { "id", "contentId", "userId" };
    
    @Transactional(readOnly = true)
    public PageHandler getPage(Long contentId, Long relatedContentId, Long userId, String orderField, String orderType,
            Integer pageIndex, Integer pageSize) {
        return dao.getPage(contentId, relatedContentId, userId, orderField, orderType, pageIndex, pageSize);
    }

    public void updateStatistics(Collection<CmsContentRelatedStatistics> entitys) {
        for (CmsContentRelatedStatistics entityStatistics : entitys) {
            CmsContentRelated entity = getEntity(entityStatistics.getId());
            if (notEmpty(entity)) {
                entity.setClicks(entity.getClicks() + entityStatistics.getClicks());
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void update(long contentId, long userId, List<CmsContentRelated> entitys) {
        Set<Long> idList = new HashSet<Long>();
        if (notEmpty(entitys)) {
            for (CmsContentRelated entity : entitys) {
                if (notEmpty(entity.getId())) {
                    update(entity.getId(), entity, ignoreProperties);
                } else {
                    entity.setContentId(contentId);
                    entity.setUserId(userId);
                    save(entity);
                }
                idList.add(entity.getId());
            }
        }
        for (CmsContentRelated extend : (List<CmsContentRelated>) getPage(contentId, null, null, null, null, null, null)
                .getList()) {
            if (!idList.contains(extend.getId())) {
                delete(extend.getId());
            }
        }
    }

    @Autowired
    private CmsContentRelatedDao dao;
}