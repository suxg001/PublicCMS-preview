package com.publiccms.common.spi;

import java.util.List;
import java.util.Locale;

import com.publiccms.views.pojo.ExtendField;

public interface Configable {
    public static final String CONFIGPREFIX = "config.";
    
    public static final String CONFIG_CODE_SITE = "site";

    public String getCode();

    public String getSubcodeDescription(String subcode, Locale locale);

    public List<String> getSubcode(int siteId);

    public List<ExtendField> getExtendFieldList(String subcode, Locale locale);
}