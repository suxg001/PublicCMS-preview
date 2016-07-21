package com.publiccms.views.controller.web.user;

import static com.publiccms.common.constants.CommonConstants.getCookiesUser;
import static com.publiccms.common.constants.CommonConstants.getCookiesUserSplit;
import static com.publiccms.logic.component.EMailComponent.CONFIG_CODE;
import static com.sanluan.common.tools.FreeMarkerUtils.makeStringByFile;
import static com.sanluan.common.tools.FreeMarkerUtils.makeStringByString;
import static com.sanluan.common.tools.RequestUtils.getCookie;
import static com.sanluan.common.tools.RequestUtils.getIpAddress;
import static com.sanluan.common.tools.VerificationUtils.encode;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.publiccms.common.base.AbstractController;
import com.publiccms.common.spi.Configable;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysDomain;
import com.publiccms.entities.sys.SysEmailToken;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.ConfigComponent;
import com.publiccms.logic.component.EMailComponent;
import com.publiccms.logic.component.TemplateComponent;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.sys.SysEmailTokenService;
import com.publiccms.logic.service.sys.SysUserService;
import com.publiccms.logic.service.sys.SysUserTokenService;
import com.publiccms.views.pojo.ExtendField;
import com.sanluan.common.tools.LanguagesUtils;

import freemarker.template.TemplateException;

/**
 * 
 * UserController 用户逻辑处理
 *
 */
@Controller
@RequestMapping("user")
@ResponseBody
public class UserController extends AbstractController implements Configable {
    public final String CONFIG_SUB_CODE = "template";
    public final String CONFIG_EMAIL_TITLE = "email_title";
    public final String CONFIG_EMAIL_PATH = "email_path";

    @Autowired
    private SysUserService service;
    @Autowired
    private SysUserTokenService sysUserTokenService;
    @Autowired
    private EMailComponent emailComponent;
    @Autowired
    private TemplateComponent templateComponent;
    @Autowired
    private SysEmailTokenService sysEmailTokenService;
    @Autowired
    private ConfigComponent configComponent;

    /**
     * @param oldpassword
     * @param password
     * @param repassword
     * @param request
     * @param session
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "changePassword")
    public String changePassword(String oldpassword, String password, String repassword, String returnUrl,
            HttpServletRequest request, HttpSession session, HttpServletResponse response, ModelMap model) {
        SysUser user = getUserFromSession(session);
        if (!virifyNotEmpty("password", password, model) && !virifyNotEquals("repassword", password, repassword, model)) {
            if (!virifyNotEquals("password", user.getPassword(), encode(oldpassword), model)) {
                Cookie userCookie = getCookie(request.getCookies(), getCookiesUser());
                if (null != userCookie && notEmpty(userCookie.getValue())) {
                    String value = userCookie.getValue();
                    if (null != value) {
                        String[] userData = value.split(getCookiesUserSplit());
                        if (userData.length > 1) {
                            sysUserTokenService.delete(userData[1]);
                        }
                    }
                }
                clearUserToSession(request.getContextPath(), session, response);
                model.addAttribute(MESSAGE, "needReLogin");
                if (notEmpty(user)) {
                    service.updatePassword(user.getId(), encode(password));
                    logOperateService.save(new LogOperate(getSite(request).getId(), user.getId(), LogLoginService.CHANNEL_WEB,
                            "changepassword", getIpAddress(request), getDate(), user.getPassword()));
                }
            }
        }
        return REDIRECT + returnUrl;
    }

    /**
     * @param email
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "saveEmail")
    public String saveEmail(String email, String returnUrl, HttpServletRequest request, HttpSession session, ModelMap model) {
        SysSite site = getSite(request);
        if (!virifyNotEmpty("email", email, model) && !virifyNotEMail("email", email, model)
                && virifyHasExist("email", service.findByEmail(site.getId(), email), model)) {
            SysUser user = getUserFromSession(session);
            SysEmailToken sysEmailToken = new SysEmailToken();
            sysEmailToken.setUserId(user.getId());
            sysEmailToken.setAuthToken(UUID.randomUUID().toString());
            sysEmailToken.setEmail(email);
            sysEmailTokenService.save(sysEmailToken);

            try {
                SysDomain domain = getDomain(request);
                Map<String, Object> emailModel = new HashMap<String, Object>();
                emailModel.put("user", user);
                emailModel.put("email", email);
                emailModel.put("authToken", sysEmailToken.getAuthToken());
                emailComponent.sendHtml(site.getId(), email,
                        makeStringByString(domain.getName(), templateComponent.getWebConfiguration(), emailModel),
                        makeStringByFile(domain.getName(), templateComponent.getWebConfiguration(), emailModel));
            } catch (IOException | TemplateException e) {
                model.addAttribute("error", "saveEmail.email.error");
            }
            model.addAttribute(MESSAGE, "saveEmail.success");
        }
        return REDIRECT + returnUrl;
    }

    /**
     * @param code
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("verifyEmail")
    @ResponseBody
    public String verifyEmail(String authToken, String returnUrl, HttpSession session, ModelMap model) {
        SysEmailToken sysEmailToken = sysEmailTokenService.getEntity(authToken);
        if (virifyNotEmpty("verifyEmail.authToken", authToken, model)
                || virifyNotExist("verifyEmail.sysEmailToken", sysEmailToken, model)) {
            return REDIRECT + returnUrl;
        }
        sysEmailTokenService.delete(sysEmailToken.getAuthToken());
        service.checked(sysEmailToken.getUserId(), sysEmailToken.getEmail());
        clearUserTimeToSession(session);
        model.addAttribute(MESSAGE, "verifyEmail.success");
        return REDIRECT + returnUrl;
    }

    @Override
    public String getCode() {
        return CONFIG_CODE;
    }

    @Override
    public void registerExtendField(String subCode, HttpServletRequest request, List<ExtendField> extendFieldList) {
        extendFieldList.add(
                new ExtendField(false, LanguagesUtils.getMessage(request, CONFIGPREFIX + CONFIG_CODE + "." + CONFIG_EMAIL_PATH),
                        null, CONFIG_EMAIL_PATH, "template", null));
    }

    @Override
    public void registerSubCode(int siteId, List<String> subCodeList) {
        Map<String, String> config = configComponent.getConfigData(siteId, CONFIG_CODE, CONFIG_SUB_CODE);
        if (notEmpty(config)) {
            subCodeList.add(CONFIG_SUB_CODE);
        }
    }
}
