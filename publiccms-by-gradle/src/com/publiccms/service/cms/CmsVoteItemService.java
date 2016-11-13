package com.publiccms.service.cms;

// Generated 2016-3-3 17:43:34 by com.sanluan.common.source.SourceMaker


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.entities.cms.CmsVoteItem;
import com.publiccms.dao.cms.CmsVoteItemDao;
import com.sanluan.common.base.BaseService;
import com.sanluan.common.handler.PageHandler;

@Service
@Transactional
public class CmsVoteItemService extends BaseService<CmsVoteItem> {

    @Transactional(readOnly = true)
    public PageHandler getPage(Integer voteId, 
                String orderField, String orderType, Integer pageIndex, Integer pageSize) {
        return dao.getPage(voteId, 
                orderField, orderType, pageIndex, pageSize);
    }
    
    @Autowired
    private CmsVoteItemDao dao;
}