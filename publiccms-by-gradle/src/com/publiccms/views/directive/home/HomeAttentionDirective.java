package com.publiccms.views.directive.home;

// Generated 2016-11-13 11:38:14 by com.sanluan.common.source.SourceMaker

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.entities.home.HomeAttention;
import com.publiccms.entities.home.HomeAttentionId;
import com.publiccms.service.home.HomeAttentionService;
import com.sanluan.common.handler.RenderHandler;

@Component
public class HomeAttentionDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Long userId = handler.getLong("userId");
        Long attentionId = handler.getLong("attentionId");
        if (notEmpty(userId) && notEmpty(attentionId)) {
            handler.put("object", service.getEntity(new HomeAttentionId(userId, attentionId))).render();
        } else {
            Long[] userIds = handler.getLongArray("userIds");
            Long[] attentionIds = handler.getLongArray("attentionIds");
            if (notEmpty(userId) && notEmpty(attentionIds)) {
                HomeAttentionId[] ids = new HomeAttentionId[attentionIds.length];
                for (int i = 0; i < attentionIds.length; i++) {
                    ids[i] = new HomeAttentionId(userId, attentionIds[i]);
                }
                List<HomeAttention> entityList = service.getEntitys(ids);
                Map<String, HomeAttention> map = new LinkedHashMap<String, HomeAttention>();
                for (HomeAttention entity : entityList) {
                    map.put(String.valueOf(entity.getId()), entity);
                }
                handler.put("map", map).render();
            } else if (notEmpty(attentionId) && notEmpty(userIds)) {
                HomeAttentionId[] ids = new HomeAttentionId[userIds.length];
                for (int i = 0; i < userIds.length; i++) {
                    ids[i] = new HomeAttentionId(userIds[i], attentionId);
                }
                List<HomeAttention> entityList = service.getEntitys(ids);
                Map<String, HomeAttention> map = new LinkedHashMap<String, HomeAttention>();
                for (HomeAttention entity : entityList) {
                    map.put(String.valueOf(entity.getId()), entity);
                }
                handler.put("map", map).render();
            }
        }
    }

    @Autowired
    private HomeAttentionService service;

}
