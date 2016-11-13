package com.publiccms.views.directive.sys;

// Generated 2016-3-2 13:39:54 by com.sanluan.common.source.SourceMaker

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.service.sys.SysExtendFieldService;
import com.publiccms.common.base.AbstractTemplateDirective;
import com.sanluan.common.handler.RenderHandler;
import com.sanluan.common.handler.PageHandler;

@Component
public class SysExtendFieldListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Integer extendId = handler.getInteger("extendId");
        PageHandler page;
        if (notEmpty(extendId)) {
            page = service.getPage(extendId, handler.getInteger("pageIndex", 1), handler.getInteger("count", 30));
        } else {
            page = new PageHandler(0, 0, 0, 0);
        }
        handler.put("page", page).render();
    }

    @Autowired
    private SysExtendFieldService service;

}