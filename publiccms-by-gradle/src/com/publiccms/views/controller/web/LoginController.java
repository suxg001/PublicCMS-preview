package com.publiccms.views.controller.web;

import static com.publiccms.common.constants.CommonConstants.getCookiesUser;
import static com.publiccms.common.constants.CommonConstants.getCookiesUserSplit;
import static com.publiccms.logic.service.log.LogLoginService.CHANNEL_WEB;
import static com.sanluan.common.tools.RequestUtils.addCookie;
import static com.sanluan.common.tools.RequestUtils.getCookie;
import static com.sanluan.common.tools.RequestUtils.getIpAddress;
import static com.sanluan.common.tools.VerificationUtils.encode;
import static org.apache.commons.lang3.StringUtils.trim;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.publiccms.common.base.AbstractController;
import com.publiccms.common.spi.Configable;
import com.publiccms.entities.log.LogLogin;
import com.publiccms.entities.sys.SysDomain;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.entities.sys.SysUserToken;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.sys.SysDomainService;
import com.publiccms.logic.service.sys.SysUserService;
import com.publiccms.logic.service.sys.SysUserTokenService;
import com.publiccms.views.pojo.ExtendField;
import com.sanluan.common.tools.LanguagesUtils;

/**
 * 
 * LoginController 登陆逻辑
 *
 */
@Controller
public class LoginController extends AbstractController implements Configable {
    public final String CONFIG_CODE = "login";
    public final String CONFIG_LOGIN_PATH = "login_path";
    public final String CONFIG_REGISTER_PATH = "register_path";
    @Autowired
    private SysUserService service;
    @Autowired
    private SysUserTokenService sysUserTokenService;
    @Autowired
    private LogLoginService logLoginService;
    @Autowired
    private SysDomainService domainService;

    /**
     * @param username
     * @param password
     * @param callback
     * @param returnUrl
     * @param request
     * @param session
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "doLogin", method = RequestMethod.POST)
    public String login(String username, String password, String returnUrl, HttpServletRequest request, HttpSession session,
            HttpServletResponse response, ModelMap model) {
        SysSite site = getSite(request);
        SysDomain domain = getDomain(request);
        username = trim(username);
        password = trim(password);
        if (virifyNotEmpty("domain", domain, model) || virifyNotEmpty("loginPath", domain.getLoginPath(), model)
                || virifyNotEmpty("username", username, model) || virifyNotEmpty("password", password, model)) {
            model.addAttribute("username", username);
            model.addAttribute("returnUrl", returnUrl);
            return REDIRECT + domain.getLoginPath();
        }
        SysUser user;
        if (virifyNotEMail(username)) {
            user = service.findByName(site.getId(), username);
        } else {
            user = service.findByEmail(site.getId(), username);
        }
        String ip = getIpAddress(request);
        if (virifyNotExist("username", user, model) || virifyNotEquals("password", encode(password), user.getPassword(), model)
                || virifyNotEnablie(user, model)) {
            model.addAttribute("username", username);
            model.addAttribute("returnUrl", returnUrl);
            Integer userId = null;
            if (notEmpty(user)) {
                userId = user.getId();
            }
            logLoginService.save(new LogLogin(site.getId(), username, userId, ip, CHANNEL_WEB, false, getDate(), password));
            return REDIRECT + domain.getLoginPath();
        }
        user.setPassword(null);
        setUserToSession(session, user);
        String authToken = UUID.randomUUID().toString();
        addCookie(request.getContextPath(), response, getCookiesUser(), user.getId() + getCookiesUserSplit() + authToken,
                Integer.MAX_VALUE, null);
        sysUserTokenService.save(new SysUserToken(authToken, site.getId(), user.getId(), CHANNEL_WEB, getDate(), ip));
        service.updateLoginStatus(user.getId(), ip);
        logLoginService.save(new LogLogin(site.getId(), username, user.getId(), ip, CHANNEL_WEB, true, getDate(), null));
        return REDIRECT + returnUrl;
    }

    /**
     * @param callback
     * @param request
     * @param session
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "loginStatus")
    @ResponseBody
    public MappingJacksonValue loginStatus(String callback, HttpServletRequest request, HttpSession session,
            HttpServletResponse response, ModelMap model) {
        SysUser user = getUserFromSession(session);
        if (notEmpty(user)) {
            model.addAttribute("id", user.getId());
            model.addAttribute("name", user.getName());
            model.addAttribute("nickname", user.getNickName());
            model.addAttribute("email", user.getEmail());
            model.addAttribute("emailChecked", user.isEmailChecked());
            model.addAttribute("superuserAccess", user.isSuperuserAccess());
        }
        return getMappingJacksonValue(model, callback);
    }

    /**
     * @param entity
     * @param repassword
     * @param callback
     * @param request
     * @param session
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "doRegister", method = RequestMethod.POST)
    public String register(SysUser entity, String repassword, String returnUrl, HttpServletRequest request, HttpSession session,
            HttpServletResponse response, ModelMap model) {
        SysSite site = getSite(request);
        SysDomain domain = getDomain(request);
        entity.setName(trim(entity.getName()));
        entity.setNickName(trim(entity.getNickName()));
        entity.setPassword(trim(entity.getPassword()));
        repassword = trim(repassword);
        if (virifyNotEmpty("domain", domain, model) || virifyNotEmpty("registerPath", domain.getRegisterPath(), model)
                || virifyNotEmpty("username", entity.getName(), model) || virifyNotEmpty("nickname", entity.getNickName(), model)
                || virifyNotEmpty("password", entity.getPassword(), model)
                || virifyNotUserName("username", entity.getName(), model)
                || virifyNotNickName("nickname", entity.getNickName(), model)
                || virifyNotEquals("repassword", entity.getPassword(), repassword, model)
                || virifyHasExist("username", service.findByName(site.getId(), entity.getName()), model)
                || virifyHasExist("nickname", service.findByNickName(site.getId(), entity.getNickName()), model)) {
            return REDIRECT + domain.getRegisterPath();
        }
        String ip = getIpAddress(request);
        entity.setPassword(encode(entity.getPassword()));
        entity.setLastLoginIp(ip);
        entity.setSiteId(site.getId());
        service.save(entity);
        String authToken = UUID.randomUUID().toString();
        entity.setPassword(null);
        setUserToSession(session, entity);
        addCookie(request.getContextPath(), response, getCookiesUser(), entity.getId() + getCookiesUserSplit() + authToken,
                Integer.MAX_VALUE, null);
        sysUserTokenService.save(new SysUserToken(authToken, site.getId(), entity.getId(), CHANNEL_WEB, getDate(), ip));
        return REDIRECT + returnUrl;
    }

    /**
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "logout", method = RequestMethod.GET)
    public String logout(String returnUrl, HttpServletRequest request, HttpServletResponse response) {
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
        clearUserToSession(request.getContextPath(), request.getSession(), response);
        if (notEmpty(returnUrl)) {
            return REDIRECT + returnUrl;
        }
        SysDomain domain = getDomain(request);
        return REDIRECT + domain.getLoginPath();
    }

    protected boolean virifyNotEnablie(SysUser user, ModelMap model) {
        if (user.isDisabled()) {
            model.addAttribute(ERROR, "verify.user.notEnablie");
            return true;
        }
        return false;
    }

    @Override
    public String getCode() {
        return CONFIG_CODE;
    }

    @Override
    public void registerExtendField(String subCode, HttpServletRequest request, List<ExtendField> extendFieldList) {
        extendFieldList.add(
                new ExtendField(false, LanguagesUtils.getMessage(request, CONFIGPREFIX + CONFIG_CODE + "." + CONFIG_LOGIN_PATH),
                        null, CONFIG_LOGIN_PATH, "template", null));
        extendFieldList.add(new ExtendField(false,
                LanguagesUtils.getMessage(request, CONFIGPREFIX + CONFIG_CODE + "." + CONFIG_REGISTER_PATH), null,
                CONFIG_REGISTER_PATH, "template", null));
    }

    @Override
    public void registerSubCode(int siteId, List<String> subCodeList) {
        @SuppressWarnings("unchecked")
        List<SysDomain> list = (List<SysDomain>) domainService.getPage(siteId, null, null).getList();
        for (SysDomain entity : list) {
            subCodeList.add(entity.getId().toString());
        }
    }
}
