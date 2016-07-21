package com.publiccms.logic.component;

import static java.util.Collections.synchronizedList;
import static java.util.Collections.synchronizedMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.spi.Cacheable;
import com.publiccms.logic.service.cms.CmsContentRelatedService;
import com.publiccms.logic.service.cms.CmsContentService;
import com.publiccms.logic.service.cms.CmsPlaceService;
import com.publiccms.views.pojo.CmsContentRelatedStatistics;
import com.publiccms.views.pojo.CmsContentStatistics;
import com.publiccms.views.pojo.CmsPlaceStatistics;
import com.sanluan.common.base.Base;

@Component
public class StatisticsComponent extends Base implements Cacheable {
    private static List<Integer> contentCachedlist = synchronizedList(new ArrayList<Integer>());
    private static Map<Integer, CmsContentStatistics> cachedMap = synchronizedMap(new HashMap<Integer, CmsContentStatistics>());
    private static List<Integer> placeCachedlist = synchronizedList(new ArrayList<Integer>());
    private static Map<Integer, CmsPlaceStatistics> placeCachedMap = synchronizedMap(new HashMap<Integer, CmsPlaceStatistics>());
    private static List<Integer> relatedCachedlist = synchronizedList(new ArrayList<Integer>());
    private static Map<Integer, CmsContentRelatedStatistics> relatedCachedMap = synchronizedMap(
            new HashMap<Integer, CmsContentRelatedStatistics>());
    @Autowired
    private CmsContentService contentService;
    @Autowired
    private CmsContentRelatedService contentRelatedService;
    @Autowired
    private CmsPlaceService placeService;

    private void clearCache(int size) {
        if (size < contentCachedlist.size()) {
            List<CmsContentStatistics> list = new ArrayList<CmsContentStatistics>();
            for (int i = 0; i < size / 10; i++) {
                list.add(cachedMap.remove(contentCachedlist.remove(0)));
            }
            contentService.updateStatistics(list);
        }
    }

    private void clearPlaceCache(int size) {
        if (size < placeCachedlist.size()) {
            List<CmsPlaceStatistics> list = new ArrayList<CmsPlaceStatistics>();
            for (int i = 0; i < size / 10; i++) {
                list.add(placeCachedMap.remove(placeCachedlist.remove(0)));
            }
            placeService.updateStatistics(list);
        }
    }

    private void clearRelatedCache(int size) {
        if (size < relatedCachedlist.size()) {
            List<CmsContentRelatedStatistics> list = new ArrayList<CmsContentRelatedStatistics>();
            for (int i = 0; i < size / 10; i++) {
                list.add(relatedCachedMap.remove(relatedCachedlist.remove(0)));
            }
            contentRelatedService.updateStatistics(list);
        }
    }

    public CmsContentRelatedStatistics relatedClicks(Integer id) {
        if (notEmpty(id)) {
            CmsContentRelatedStatistics contentRelatedStatistics = relatedCachedMap.get(id);
            if (empty(contentRelatedStatistics)) {
                clearRelatedCache(100);
                contentRelatedStatistics = new CmsContentRelatedStatistics(id, 1, contentRelatedService.getEntity(id));
                relatedCachedlist.add(id);
            } else {
                contentRelatedStatistics.setClicks(contentRelatedStatistics.getClicks() + 1);
            }
            relatedCachedMap.put(id, contentRelatedStatistics);
            return contentRelatedStatistics;
        } else {
            return null;
        }
    }

    public CmsPlaceStatistics placeClicks(Integer id) {
        if (notEmpty(id)) {
            CmsPlaceStatistics placeStatistics = placeCachedMap.get(id);
            if (empty(placeStatistics)) {
                clearPlaceCache(100);
                placeStatistics = new CmsPlaceStatistics(id, 1, placeService.getEntity(id));
                placeCachedlist.add(id);
            } else {
                placeStatistics.setClicks(placeStatistics.getClicks() + 1);
            }
            placeCachedMap.put(id, placeStatistics);
            return placeStatistics;
        } else {
            return null;
        }
    }

    public CmsContentStatistics clicks(Integer id) {
        if (notEmpty(id)) {
            CmsContentStatistics contentStatistics = cachedMap.get(id);
            if (empty(contentStatistics)) {
                clearCache(300);
                contentStatistics = new CmsContentStatistics(id, 1, 0, 0, contentService.getEntity(id));
                contentCachedlist.add(id);
            } else {
                contentStatistics.setClicks(contentStatistics.getClicks() + 1);
            }
            cachedMap.put(id, contentStatistics);
            return contentStatistics;
        } else {
            return null;
        }
    }

    public CmsContentStatistics comments(Integer id) {
        if (notEmpty(id)) {
            CmsContentStatistics contentStatistics = cachedMap.get(id);
            if (empty(contentStatistics)) {
                clearCache(300);
                contentStatistics = new CmsContentStatistics(id, 0, 1, 0, contentService.getEntity(id));
                contentCachedlist.add(id);
            } else {
                contentStatistics.setComments(contentStatistics.getComments() + 1);
            }
            cachedMap.put(id, contentStatistics);
            return contentStatistics;
        } else {
            return null;
        }
    }

    public CmsContentStatistics scores(Integer id) {
        if (notEmpty(id)) {
            CmsContentStatistics contentStatistics = cachedMap.get(id);
            if (empty(contentStatistics)) {
                clearCache(300);
                contentStatistics = new CmsContentStatistics(id, 0, 0, 1, contentService.getEntity(id));
                contentCachedlist.add(id);
            } else {
                contentStatistics.setComments(contentStatistics.getComments() + 1);
            }
            cachedMap.put(id, contentStatistics);
            return contentStatistics;
        } else {
            return null;
        }
    }

    @Override
    public void clear() {
        placeService.updateStatistics(placeCachedMap.values());
        placeCachedlist.clear();
        placeCachedMap.clear();
        contentRelatedService.updateStatistics(relatedCachedMap.values());
        relatedCachedlist.clear();
        relatedCachedMap.clear();
        contentService.updateStatistics(cachedMap.values());
        contentCachedlist.clear();
        cachedMap.clear();
    }
}