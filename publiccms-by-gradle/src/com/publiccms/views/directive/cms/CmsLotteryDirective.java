package com.publiccms.views.directive.cms;

// Generated 2016-3-1 17:24:24 by com.sanluan.common.source.SourceMaker

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.entities.cms.CmsLottery;
import com.publiccms.service.cms.CmsLotteryService;
import com.sanluan.common.handler.RenderHandler;

@Component
public class CmsLotteryDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Integer id = handler.getInteger("id");
        if (notEmpty(id)) {
            handler.put("object", service.getEntity(id)).render();
        } else {
            Integer[] ids = handler.getIntegerArray("ids");
            if (notEmpty(ids)) {
                List<CmsLottery> entityList = service.getEntitys(ids);
                Map<String, CmsLottery> map = new LinkedHashMap<String, CmsLottery>();
                for (CmsLottery entity : entityList) {
                    map.put(String.valueOf(entity.getId()), entity);
                }
                handler.put("map", map).render();
            }
        }
    }

    @Autowired
    private CmsLotteryService service;

}
