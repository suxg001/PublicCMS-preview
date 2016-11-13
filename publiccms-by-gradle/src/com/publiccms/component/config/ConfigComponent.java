package com.publiccms.component.config;

import static com.publiccms.common.tools.ExtendUtils.getExtendMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.spi.Cache;
import com.publiccms.common.spi.CacheEntity;
import com.publiccms.common.spi.Config;
import com.publiccms.component.cache.MemoryCacheEntity;
import com.publiccms.entities.sys.SysConfig;
import com.publiccms.service.sys.SysConfigService;
import com.publiccms.views.pojo.ConfigItem;
import com.publiccms.views.pojo.ExtendField;
import com.sanluan.common.base.Base;

/**
 * 
 * ConfigComponent 文件操作组件
 *
 */
@Component
public class ConfigComponent extends Base implements Cache {
    private static CacheEntity<String, Map<String, String>> configCache = new MemoryCacheEntity<String, Map<String, String>>();

    @Autowired
    private SysConfigService service;
    @Autowired
    private List<Config> configList;
    private Map<String, List<Config>> configMap;

    public Set<String> getConfigCodeList() {
        Set<String> configItemSet = new LinkedHashSet<String>();
        for (Config config : configList) {
            configItemSet.add(config.getCode());
        }
        return configItemSet;
    }

    public Map<String, List<Config>> getConfigMap() {
        if (empty(configMap)) {
            configMap = new HashMap<String, List<Config>>();
            for (Config config : configList) {
                List<Config> configList = configMap.get(config.getCode());
                if (empty(configList)) {
                    configList = new ArrayList<Config>();
                }
                configList.add(config);
                configMap.put(config.getCode(), configList);
            }
        }
        return configMap;
    }

    public List<ConfigItem> getConfigItemList(int siteId, String code, Locale locale) {
        List<ConfigItem> configItemList = new ArrayList<ConfigItem>();
        List<Config> configList = getConfigMap().get(code);
        if (notEmpty(configList)) {
            for (Config config : configList) {
                for (String subcode : config.getSubcode(siteId)) {
                    ConfigItem configItem = new ConfigItem(subcode, config.getSubcodeDescription(subcode, locale));
                    configItemList.add(configItem);
                }
            }
        }
        return configItemList;
    }

    public List<ExtendField> getExtendFieldList(int siteId, String code, String subcode, Locale locale) {
        List<ExtendField> extendFieldList = new ArrayList<ExtendField>();
        List<Config> configList = getConfigMap().get(code);
        if (notEmpty(configList)) {
            for (Config config : configList) {
                if (config.getSubcode(siteId).contains(subcode)) {
                    extendFieldList = config.getExtendFieldList(subcode, locale);
                    break;
                }
            }
        }
        return extendFieldList;
    }

    public Map<String, String> getConfigData(int siteId, String code, String subCode) {
        String key = getKey(siteId, code, subCode);
        Map<String, String> map = configCache.get(key);
        if (empty(map)) {
            SysConfig entity = service.getEntity(siteId, code, subCode);
            if (notEmpty(entity) && notEmpty(entity.getData())) {
                map = getExtendMap(entity.getData());
            } else {
                map = new HashMap<String, String>();
            }
            configCache.put(key, map);
        }
        return map;
    }

    public void removeCache(int siteId, String code, String subCode) {
        configCache.remove(getKey(siteId, code, subCode));
    }

    private String getKey(int siteId, String code, String subCode) {
        return siteId + code + subCode;
    }

    @Override
    public void clear() {
        configCache.clear();
    }
}
