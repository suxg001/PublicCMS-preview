package com.publiccms.logic.component;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.AbstractCachingViewResolver;

import com.publiccms.common.spi.Cacheable;
import com.publiccms.logic.service.tools.HqlService;
import com.sanluan.common.base.Base;

@Component
public class CacheComponent extends Base {
    @Autowired
    private List<Cacheable> cacheableList;
    private List<AbstractCachingViewResolver> cachingViewResolverList = new ArrayList<AbstractCachingViewResolver>();

    @Autowired
    private HqlService hqlService;

    public void clear() {
        for (Cacheable cache : cacheableList) {
            cache.clear();
        }
        clearViewCache();
        hqlService.clear();
    }
    
    public void clearViewCache() {
        for (AbstractCachingViewResolver cachingViewResolver : cachingViewResolverList) {
            cachingViewResolver.clearCache();
        }
    }

    public void registerCachingViewResolverList(AbstractCachingViewResolver cachingViewResolver) {
        cachingViewResolverList.add(cachingViewResolver);
    }
}
