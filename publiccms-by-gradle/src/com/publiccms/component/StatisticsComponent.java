package com.publiccms.component;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.spi.Cache;
import com.publiccms.common.spi.CacheEntity;
import com.publiccms.component.cache.MemoryCacheEntity;
import com.publiccms.entities.cms.CmsWord;
import com.publiccms.service.cms.CmsContentRelatedService;
import com.publiccms.service.cms.CmsContentService;
import com.publiccms.service.cms.CmsPlaceService;
import com.publiccms.service.cms.CmsTagService;
import com.publiccms.service.cms.CmsWordService;
import com.publiccms.views.pojo.CmsContentRelatedStatistics;
import com.publiccms.views.pojo.CmsContentStatistics;
import com.publiccms.views.pojo.CmsPlaceStatistics;
import com.publiccms.views.pojo.CmsTagStatistics;
import com.publiccms.views.pojo.CmsWordStatistics;
import com.sanluan.common.base.Base;

@Component
public class StatisticsComponent extends Base implements Cache {
    private static CacheEntity<Long, CmsContentStatistics> contentCache = new MemoryCacheEntity<Long, CmsContentStatistics>();
    private static CacheEntity<Long, CmsPlaceStatistics> placeCache = new MemoryCacheEntity<Long, CmsPlaceStatistics>();
    private static CacheEntity<Long, CmsContentRelatedStatistics> relatedCache = new MemoryCacheEntity<Long, CmsContentRelatedStatistics>();
    private static CacheEntity<Long, CmsWordStatistics> wordCache = new MemoryCacheEntity<Long, CmsWordStatistics>();
    private static CacheEntity<Long, CmsTagStatistics> tagCache = new MemoryCacheEntity<Long, CmsTagStatistics>();
    @Autowired
    private CmsContentService contentService;
    @Autowired
    private CmsContentRelatedService contentRelatedService;
    @Autowired
    private CmsPlaceService placeService;
    @Autowired
    private CmsWordService wordService;
    @Autowired
    private CmsTagService tagService;

    public CmsContentRelatedStatistics relatedClicks(Long id) {
        if (notEmpty(id)) {
            CmsContentRelatedStatistics contentRelatedStatistics = relatedCache.get(id);
            if (empty(contentRelatedStatistics)) {
                contentRelatedStatistics = new CmsContentRelatedStatistics(id, 1, contentRelatedService.getEntity(id));
            } else {
                contentRelatedStatistics.setClicks(contentRelatedStatistics.getClicks() + 1);
            }
            List<CmsContentRelatedStatistics> list = relatedCache.put(id, contentRelatedStatistics);
            if (notEmpty(list)) {
                contentRelatedService.updateStatistics(list);
            }
            return contentRelatedStatistics;
        } else {
            return null;
        }
    }

    public CmsTagStatistics searchTag(Long id) {
        if (notEmpty(id)) {
            CmsTagStatistics tagStatistics = tagCache.get(id);
            if (empty(tagStatistics)) {
                tagStatistics = new CmsTagStatistics(id, 1, tagService.getEntity(id));
            } else {
                tagStatistics.setSearchCounts(tagStatistics.getSearchCounts() + 1);
            }
            List<CmsTagStatistics> list = tagCache.put(id, tagStatistics);
            if (notEmpty(list)) {
                tagService.updateStatistics(list);
            }
            return tagStatistics;
        } else {
            return null;
        }
    }

    public CmsWordStatistics search(int siteId, String word) {
        if (notEmpty(word)) {
            CmsWord entity = wordService.getEntity(siteId, word);
            if (empty(entity)) {
                entity = new CmsWord();
                entity.setName(word);
                entity.setSiteId(siteId);
                entity.setHidden(true);
                wordService.save(entity);
            }
            long id = entity.getId();
            CmsWordStatistics wordStatistics = wordCache.get(id);
            if (empty(wordStatistics)) {
                wordStatistics = new CmsWordStatistics(entity.getId(), 1, entity);
            } else {
                wordStatistics.setSearchCounts(wordStatistics.getSearchCounts() + 1);
            }
            List<CmsWordStatistics> list = wordCache.put(id, wordStatistics);
            if (notEmpty(list)) {
                wordService.updateStatistics(list);
            }
            return wordStatistics;
        } else {
            return null;
        }
    }

    public CmsPlaceStatistics placeClicks(Long id) {
        if (notEmpty(id)) {
            CmsPlaceStatistics placeStatistics = placeCache.get(id);
            if (empty(placeStatistics)) {
                placeStatistics = new CmsPlaceStatistics(id, 1, placeService.getEntity(id));
            } else {
                placeStatistics.setClicks(placeStatistics.getClicks() + 1);
            }
            List<CmsPlaceStatistics> list = placeCache.put(id, placeStatistics);
            if (notEmpty(list)) {
                placeService.updateStatistics(list);
            }
            return placeStatistics;
        } else {
            return null;
        }
    }

    public CmsContentStatistics clicks(Long id) {
        if (notEmpty(id)) {
            CmsContentStatistics contentStatistics = contentCache.get(id);
            if (empty(contentStatistics)) {
                contentStatistics = new CmsContentStatistics(id, 1, 0, 0, contentService.getEntity(id));
            } else {
                contentStatistics.setClicks(contentStatistics.getClicks() + 1);
            }
            List<CmsContentStatistics> list = contentCache.put(id, contentStatistics);
            if (notEmpty(list)) {
                contentService.updateStatistics(list);
            }
            return contentStatistics;
        } else {
            return null;
        }
    }

    public CmsContentStatistics comments(Long id) {
        if (notEmpty(id)) {
            CmsContentStatistics contentStatistics = contentCache.get(id);
            if (empty(contentStatistics)) {
                contentStatistics = new CmsContentStatistics(id, 0, 1, 0, contentService.getEntity(id));
            } else {
                contentStatistics.setComments(contentStatistics.getComments() + 1);
            }
            List<CmsContentStatistics> list = contentCache.put(id, contentStatistics);
            if (notEmpty(list)) {
                contentService.updateStatistics(list);
            }
            return contentStatistics;
        } else {
            return null;
        }
    }

    public CmsContentStatistics scores(Long id) {
        if (notEmpty(id)) {
            CmsContentStatistics contentStatistics = contentCache.get(id);
            if (empty(contentStatistics)) {
                contentStatistics = new CmsContentStatistics(id, 0, 0, 1, contentService.getEntity(id));
            } else {
                contentStatistics.setComments(contentStatistics.getComments() + 1);
            }
            List<CmsContentStatistics> list = contentCache.put(id, contentStatistics);
            if (notEmpty(list)) {
                contentService.updateStatistics(list);
            }
            return contentStatistics;
        } else {
            return null;
        }
    }

    @Override
    public void clear() {
        placeService.updateStatistics(placeCache.clear());
        contentRelatedService.updateStatistics(relatedCache.clear());
        wordService.updateStatistics(wordCache.clear());
        tagService.updateStatistics(tagCache.clear());
        contentService.updateStatistics(contentCache.clear());
    }
}