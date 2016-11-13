package com.publiccms.views.directive.sys;

// Generated 2015-7-20 11:46:39 by com.sanluan.common.source.SourceMaker

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.entities.sys.SysRole;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.service.sys.SysRoleService;
import com.publiccms.common.base.AbstractTemplateDirective;
import com.sanluan.common.handler.RenderHandler;

@Component
public class SysRoleDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Integer id = handler.getInteger("id");
        SysSite site = getSite(handler);
        if (notEmpty(id)) {
            SysRole entity = service.getEntity(id);
            if (notEmpty(entity) && site.getId() == entity.getSiteId()) {
                handler.put("object", entity).render();
            }
        } else {
            Integer[] ids = handler.getIntegerArray("ids");
            if (notEmpty(ids)) {
                List<SysRole> entityList = service.getEntitys(ids);
                Map<String, SysRole> map = new LinkedHashMap<String, SysRole>();
                for (SysRole entity : entityList) {
                    if(site.getId() == entity.getSiteId()){
                        map.put(String.valueOf(entity.getId()), entity);
                    }
                }
                handler.put("map", map).render();
            }
        }
    }

    @Autowired
    private SysRoleService service;

}
