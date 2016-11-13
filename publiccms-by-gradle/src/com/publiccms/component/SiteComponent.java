package com.publiccms.component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.publiccms.common.spi.Cache;
import com.publiccms.common.spi.CacheEntity;
import com.publiccms.common.view.MultiSiteImportDirective;
import com.publiccms.common.view.MultiSiteIncludeDirective;
import com.publiccms.component.cache.MemoryCacheEntity;
import com.publiccms.entities.sys.SysDomain;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.service.sys.SysDomainService;
import com.publiccms.service.sys.SysSiteService;
import com.sanluan.common.base.Base;

public class SiteComponent extends Base implements Cache {
    public static final String TEMPLATE_PATH = "template";
    public static final String TASK_FILE_PATH = "task";

    public static final String STATIC_FILE_PATH_RESOURCE = "resource";
    public static final String STATIC_FILE_PATH_WEB = "web";

    private static CacheEntity<String, SysSite> siteCache = new MemoryCacheEntity<String, SysSite>();
    private static CacheEntity<String, SysDomain> domainCache = new MemoryCacheEntity<String, SysDomain>();
    public static final String SITE_PATH_PREFIX = "/site_";
    public static final String CONTEXT_SITE = "site";
    public static final String CONTEXT_INCLUDE = "include";
    public static final String CONTEXT_IMPORT = "import";
    protected static final String CONTEXT_BASE = "base";

    private String rootPath;
    private String resourceFilePath;
    private String staticFilePath;
    private String taskTemplateFilePath;
    private String webTemplateFilePath;

    private int defaultSiteId;
    private Set<Integer> idSet = new HashSet<Integer>();
    @Autowired
    private SysDomainService sysDomainService;
    @Autowired
    private SysSiteService sysSiteService;

    /**
     * @param path
     * @return
     */
    public static String getFullFileName(SysSite site, String path) {
        if (path.startsWith(SEPARATOR) || path.startsWith("\\")) {
            return SITE_PATH_PREFIX + site.getId() + path;
        }
        return SITE_PATH_PREFIX + site.getId() + SEPARATOR + path;
    }

    public static void expose(Map<String, Object> map, SysSite site, String scheme, String serverName, int serverPort,
            String contextPath) {
        map.put(CONTEXT_BASE, getBasePath(scheme, serverName, serverPort, contextPath));
        expose(map, site);
    }

    public static void expose(Map<String, Object> map, SysSite site) {
        map.put(CONTEXT_SITE, site);
        map.put(CONTEXT_INCLUDE, new MultiSiteIncludeDirective(site));
        map.put(CONTEXT_IMPORT, new MultiSiteImportDirective(site));
    }

    private static String getBasePath(String scheme, String serverName, int serverPort, String contextPath) {
        return scheme + "://" + (80 == serverPort ? serverName : serverName + ":" + serverPort) + contextPath;
    }

    public String getViewNamePreffix(String serverName) {
        SysSite site = getSite(serverName);
        SysDomain sysDomain = getDomain(serverName);
        return getViewNamePreffix(site, sysDomain);
    }

    public String getViewNamePreffix(SysSite site, SysDomain sysDomain) {
        return getFullFileName(site, null == sysDomain || empty(sysDomain.getPath()) ? BLANK : sysDomain.getPath() + SEPARATOR);
    }

    public SysDomain getDomain(String serverName) {
        SysDomain sysDomain = domainCache.get(serverName);
        if (empty(sysDomain)) {
            sysDomain = sysDomainService.getEntity(serverName);
            if (empty(sysDomain)) {
                sysDomain = new SysDomain();
            } else {
                domainCache.put(serverName, sysDomain);
            }
        }
        return sysDomain;
    }

    public SysSite getSite(String serverName) {
        SysSite site = siteCache.get(serverName);
        if (empty(site)) {
            SysDomain sysDomain = getDomain(serverName);
            if (notEmpty(sysDomain.getId())) {
                site = sysSiteService.getEntity(sysDomain.getSiteId());
            } else {
                site = sysSiteService.getEntity(defaultSiteId);
            }
            siteCache.put(serverName, site);
        }
        return site;
    }

    public boolean isMaster(int siteId) {
        return notEmpty(idSet) && idSet.contains(siteId);
    }

    /**
     * @param filePath
     * @return
     */
    public String getResourceFilePath(SysSite site, String filePath) {
        return resourceFilePath + getFullFileName(site, filePath);
    }

    /**
     * @param filePath
     * @return
     */
    public String getStaticFilePath(SysSite site, String filePath) {
        return staticFilePath + getFullFileName(site, filePath);
    }

    /**
     * @param templatePath
     * @return
     */
    public String getTaskTemplateFilePath(SysSite site, String templatePath) {
        return getTaskTemplateFilePath() + getFullFileName(site, templatePath);
    }

    /**
     * @param templatePath
     * @return
     */
    public String getWebTemplateFilePath(SysSite site, String templatePath) {
        return getWebTemplateFilePath() + getFullFileName(site, templatePath);
    }

    public void setDefaultSiteId(int defaultSiteId) {
        this.defaultSiteId = defaultSiteId;
    }

    public void setSiteMasters(String siteMasters) {
        String[] masters = siteMasters.split(COMMA_DELIMITED);
        for (String master : masters) {
            Integer id;
            try {
                id = Integer.parseInt(master);
            } catch (NumberFormatException e) {
                id = null;
            }
            if (notEmpty(id)) {
                idSet.add(id);
            }
        }
    }

    /**
     * @param rootPath
     */
    public void setRootPath(String rootPath) {
        if (notEmpty(rootPath)) {
            if (!(rootPath.endsWith(SEPARATOR) || rootPath.endsWith("\\"))) {
                rootPath += SEPARATOR;
            }
        }
        this.rootPath = rootPath;
        this.staticFilePath = rootPath + STATIC_FILE_PATH_WEB;
        this.taskTemplateFilePath = rootPath + TASK_FILE_PATH;
        this.webTemplateFilePath = rootPath + TEMPLATE_PATH;
        this.resourceFilePath = rootPath + STATIC_FILE_PATH_RESOURCE;
    }

    @Override
    public void clear() {
        siteCache.clear();
        domainCache.clear();
    }

    public String getRootPath() {
        return rootPath;
    }

    public void setTaskTemplateFilePath(String taskTemplateFilePath) {
        this.taskTemplateFilePath = taskTemplateFilePath;
    }

    public void setWebTemplateFilePath(String webTemplateFilePath) {
        this.webTemplateFilePath = webTemplateFilePath;
    }

    public String getTaskTemplateFilePath() {
        return taskTemplateFilePath;
    }

    public String getWebTemplateFilePath() {
        return webTemplateFilePath;
    }
}