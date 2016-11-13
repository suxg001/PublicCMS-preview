package com.publiccms.views.directive.task;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTaskDirective;
import com.publiccms.component.template.TemplateComponent;
import com.publiccms.entities.cms.CmsContent;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.service.cms.CmsContentService;
import com.sanluan.common.handler.RenderHandler;

@Component
public class PublishContentDirective extends AbstractTaskDirective {
    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Long id = handler.getLong("id");
        SysSite site = getSite(handler);
        Map<String, Boolean> map = new LinkedHashMap<String, Boolean>();
        if (notEmpty(id)) {
            map.put(id.toString(), templateComponent.createContentFile(site, service.getEntity(id), null, null));
        } else {
            Long[] ids = handler.getLongArray("ids");
            if (notEmpty(ids)) {
                List<CmsContent> entityList = service.getEntitys(ids);
                for (CmsContent entity : entityList) {
                    map.put(entity.getId().toString(), templateComponent.createContentFile(site, entity, null, null));
                }
            }
        }
        handler.put("map", map).render();
    }

    @Autowired
    private TemplateComponent templateComponent;
    @Autowired
    private CmsContentService service;
}
