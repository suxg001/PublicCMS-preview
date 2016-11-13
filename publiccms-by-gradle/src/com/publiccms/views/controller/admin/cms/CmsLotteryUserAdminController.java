package com.publiccms.views.controller.admin.cms;

// Generated 2016-11-13 10:56:11 by com.sanluan.common.source.SourceMaker

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.publiccms.common.base.AbstractController;
import com.publiccms.entities.cms.CmsLotteryUser;
import com.publiccms.service.cms.CmsLotteryUserService;
@Controller
@RequestMapping("cmsLotteryUser")
public class CmsLotteryUserAdminController extends AbstractController {

	private String[] ignoreProperties = new String[]{"id"};

    @RequestMapping("save")
    public String save(CmsLotteryUser entity, HttpServletRequest request) {
        if (notEmpty(entity.getId())) {
            entity = service.update(entity.getId(), entity, ignoreProperties);
        } else {
            service.save(entity);
        }
        return TEMPLATE_DONE;
    }

    @RequestMapping("delete")
    public String delete(Integer id) {
        service.delete(id);
        return TEMPLATE_DONE;
    }
    
    @Autowired
    private CmsLotteryUserService service;
}