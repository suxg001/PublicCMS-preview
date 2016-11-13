package com.publiccms.component.config;

import static com.sanluan.common.tools.LanguagesUtils.getMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Component;

import com.publiccms.common.spi.Config;
import com.publiccms.views.pojo.ExtendField;
import com.sanluan.common.base.Base;

@Component
public class LoginConfigComponent extends Base implements Config {
    public static final String CONFIG_SUBCODE = "login";
    public static final String CONFIG_LOGIN_PATH = "login_path";
    public static final String CONFIG_REGISTER_PATH = "register_path";
    public static final String CONFIG_DESCRIPTION = CONFIGPREFIX + CONFIG_CODE_SITE + "." + CONFIG_SUBCODE;

    @Override
    public String getCode() {
        return CONFIG_CODE_SITE;
    }

    @Override
    public List<ExtendField> getExtendFieldList(String subcode, Locale locale) {
        List<ExtendField> extendFieldList = new ArrayList<ExtendField>();
        extendFieldList.add(new ExtendField(false, getMessage(locale, CONFIG_DESCRIPTION + "." + CONFIG_LOGIN_PATH), null,
                CONFIG_LOGIN_PATH, INPUTTYPE_TEMPLATE, null));
        extendFieldList.add(new ExtendField(false, getMessage(locale, CONFIG_DESCRIPTION + "." + CONFIG_REGISTER_PATH), null,
                CONFIG_REGISTER_PATH, INPUTTYPE_TEMPLATE, null));
        return extendFieldList;
    }

    @Override
    public List<String> getSubcode(int siteId) {
        List<String> subcodeList = new ArrayList<String>();
        subcodeList.add(CONFIG_SUBCODE);
        return subcodeList;
    }

    @Override
    public String getSubcodeDescription(String subcode, Locale locale) {
        return getMessage(locale, CONFIG_DESCRIPTION);
    }
}
