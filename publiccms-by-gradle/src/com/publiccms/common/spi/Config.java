package com.publiccms.common.spi;

import java.util.List;
import java.util.Locale;

import com.publiccms.views.pojo.ExtendField;

public interface Config {
    public static final String CONFIGPREFIX = "config.";

    public static final String CONFIG_CODE_SITE = "site";

    public static final String INPUTTYPE_TEMPLATE = "template";

    public static final String INPUTTYPE_TEXT = "text";

    public static final String INPUTTYPE_FILE = "file";

    public static final String INPUTTYPE_IMAGE = "image";
    
    public static final String INPUTTYPE_EDITOR = "editor";

    public static final String INPUTTYPE_TEXTAREA = "textarea";

    public static final String INPUTTYPE_DATE = "date";

    public static final String INPUTTYPE_DATETIME = "datetime";

    public static final String INPUTTYPE_USER = "user";

    public static final String INPUTTYPE_CONTENT = "content";

    public static final String INPUTTYPE_NUMBER = "number";

    public static final String INPUTTYPE_PASSWORD = "password";

    public static final String INPUTTYPE_BOOLEAN = "boolean";

    public static final String INPUTTYPE_EMAIL = "email";

    public String getCode();

    public String getSubcodeDescription(String subcode, Locale locale);

    public List<String> getSubcode(int siteId);

    public List<ExtendField> getExtendFieldList(String subcode, Locale locale);
}