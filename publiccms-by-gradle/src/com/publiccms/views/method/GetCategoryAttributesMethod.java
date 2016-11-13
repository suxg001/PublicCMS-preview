package com.publiccms.views.method;

import static com.publiccms.common.tools.ExtendUtils.getExtendMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.entities.cms.CmsCategoryAttribute;
import com.publiccms.service.cms.CmsCategoryAttributeService;
import com.sanluan.common.base.BaseMethod;

import freemarker.template.TemplateModelException;

@Component
public class GetCategoryAttributesMethod extends BaseMethod {

    /*
     * (non-Javadoc)
     * 
     * @see freemarker.template.TemplateMethodModelEx#exec(java.util.List)
     */
    @SuppressWarnings("unchecked")
    @Override
    public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
        Long[] ids = getLongArray(0, arguments);
        if (notEmpty(ids)) {
            Map<String, Map<String, String>> resultMap = new HashMap<String, Map<String, String>>();
            for (CmsCategoryAttribute entity : service.getEntitys(ids)) {
                Map<String, String> map = getExtendMap(entity.getData());
                map.put("title", entity.getTitle());
                map.put("keywords", entity.getKeywords());
                map.put("description", entity.getDescription());
                resultMap.put(String.valueOf(entity.getCategoryId()), map);
            }
            return resultMap;
        }
        return null;
    }

    @Autowired
    private CmsCategoryAttributeService service;
}
