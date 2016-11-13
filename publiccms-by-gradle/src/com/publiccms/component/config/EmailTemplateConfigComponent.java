package com.publiccms.component.config;

import static com.publiccms.component.EmailComponent.CONFIG_CODE;
import static com.sanluan.common.tools.LanguagesUtils.getMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.spi.Config;
import com.publiccms.component.EmailComponent;
import com.publiccms.views.pojo.ExtendField;
import com.sanluan.common.base.Base;

@Component
public class EmailTemplateConfigComponent extends Base implements Config {
    public static final String CONFIG_SUBCODE = "template";
    public static final String CONFIG_EMAIL_TITLE = "email_title";
    public static final String CONFIG_EMAIL_PATH = "email_path";
    public static final String CONFIG_DESCRIPTION = CONFIGPREFIX + CONFIG_CODE + "." + CONFIG_SUBCODE;

    @Autowired
    private ConfigComponent configComponent;

    @Override
    public String getCode() {
        return CONFIG_CODE;
    }

    @Override
    public List<ExtendField> getExtendFieldList(String subcode, Locale locale) {
        List<ExtendField> extendFieldList = new ArrayList<ExtendField>();
        if (CONFIG_SUBCODE.equals(subcode)) {
            extendFieldList.add(new ExtendField(false, getMessage(locale, CONFIG_DESCRIPTION + "." + CONFIG_EMAIL_TITLE), null,
                    CONFIG_EMAIL_TITLE, INPUTTYPE_TEXT, null));
            extendFieldList.add(new ExtendField(false, getMessage(locale, CONFIG_DESCRIPTION + "." + CONFIG_EMAIL_PATH), null,
                    CONFIG_EMAIL_PATH, INPUTTYPE_TEMPLATE, null));
        }
        return extendFieldList;
    }

    @Override
    public List<String> getSubcode(int siteId) {
        List<String> subcodeList = new ArrayList<String>();
        Map<String, String> config = configComponent.getConfigData(siteId, CONFIG_CODE, EmailComponent.CONFIG_SUBCODE);
        if (notEmpty(config)) {
            subcodeList.add(CONFIG_SUBCODE);
        }
        return subcodeList;
    }

    @Override
    public String getSubcodeDescription(String subcode, Locale locale) {
        return getMessage(locale, CONFIG_DESCRIPTION);
    }
}
