package com.publiccms.component.payment.gateway;

import static com.sanluan.common.tools.LanguagesUtils.getMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.publiccms.common.spi.Cache;
import com.publiccms.common.spi.CacheEntity;
import com.publiccms.common.spi.Config;
import com.publiccms.common.spi.GateWay;
import com.publiccms.component.cache.MemoryCacheEntity;
import com.publiccms.component.config.ConfigComponent;
import com.publiccms.views.pojo.ExtendField;
import com.sanluan.common.base.Base;

@Component
public class AliPayGateWay extends Base implements GateWay, Cache, Config {
    public static final String ALIPAY_URL = "https://openapi.alipay.com/gateway.do";
    public static final String ALIPAY_FORMAT = "json";
    public static final String CONFIG_SUBCODE = "alipay";
    public static final String CONFIG_APP_ID = "appId";
    public static final String CONFIG_PRIVATE_KEY = "privateKey";
    public static final String CONFIG_PUBLIC_KEY = "publicKey";
    public static final String CONFIG_RETURN_URL = "returnUrl";
    public static final String CONFIG_NOTIFY_URL = "notifyUrl";
    public static final String CONFIG_DESCRIPTION = CONFIGPREFIX + CONFIG_CODE + "." + CONFIG_SUBCODE;

    private static CacheEntity<Integer, AlipayClient> clientCache = new MemoryCacheEntity<Integer, AlipayClient>();

    @Autowired
    private ConfigComponent configComponent;

    public Map<String, String> getPaymentData(int siteId, long orderId) throws Exception {
        Map<String, String> result = new HashMap<String, String>();
        AlipayClient client = getClient(siteId);
        if (notEmpty(client)) {
            Map<String, String> config = configComponent.getConfigData(siteId, CONFIG_CODE, CONFIG_SUBCODE);
            if (notEmpty(config.get(CONFIG_RETURN_URL)) && notEmpty(config.get(CONFIG_NOTIFY_URL))) {
                AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();// 创建API对应的request
                alipayRequest.setReturnUrl(config.get(CONFIG_RETURN_URL));
                alipayRequest.setNotifyUrl(config.get(CONFIG_NOTIFY_URL));
                alipayRequest.setBizContent("{\"out_trade_no\":\"" + orderId
                        + "\",\"total_amount\":8.8,\"subject\":\"1111\",\"seller_id\":\"1111\",\"product_code\":\"QUICK_WAP_PAY\""
                        + "  }");
                result.put("form", client.pageExecute(alipayRequest).getBody());
            }
        } else {
            throw new Exception();
        }
        return result;
    }

    public boolean verify(int siteId, Map<String, String> paramsMap) throws Exception {
        AlipayClient client = getClient(siteId);
        if (notEmpty(client)) {
            Map<String, String> config = configComponent.getConfigData(siteId, CONFIG_CODE, CONFIG_SUBCODE);
            if (notEmpty(config.get(CONFIG_APP_ID)) && notEmpty(config.get(CONFIG_PRIVATE_KEY))
                    && notEmpty(config.get(CONFIG_PUBLIC_KEY))) {
                if (AlipaySignature.rsaCheckV1(paramsMap, config.get(CONFIG_PUBLIC_KEY), DEFAULT_CHARSET)) {
                    AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();// 创建API对应的request类
                    request.setBizContent("{\"trade_no\":\"" + paramsMap.get("trade_no") + "\"}");
                    // AlipayTradeQueryResponse response =
                    // client.execute(request);
                    return true;
                }
            }
        }
        return false;

    }

    public AlipayClient getClient(int siteId) {
        AlipayClient client = clientCache.get(siteId);
        if (empty(client)) {
            Map<String, String> config = configComponent.getConfigData(siteId, CONFIG_CODE, CONFIG_SUBCODE);
            if (notEmpty(config.get(CONFIG_APP_ID)) && notEmpty(config.get(CONFIG_PRIVATE_KEY))
                    && notEmpty(config.get(CONFIG_PUBLIC_KEY))) {
                client = new DefaultAlipayClient(ALIPAY_URL, config.get(CONFIG_APP_ID), config.get(CONFIG_PRIVATE_KEY),
                        ALIPAY_FORMAT, DEFAULT_CHARSET, config.get(CONFIG_PUBLIC_KEY));
                clientCache.put(siteId, client);
            }
        }
        return client;
    }

    @Override
    public boolean available(int siteId) {
        Map<String, String> config = configComponent.getConfigData(siteId, CONFIG_CODE, CONFIG_SUBCODE);
        if (notEmpty(config) && notEmpty(config.get(CONFIG_APP_ID))) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String getGateWayCode() {
        return CONFIG_SUBCODE;
    }

    @Override
    public String getCode() {
        return CONFIG_CODE;
    }

    @Override
    public String getSubcodeDescription(String subcode, Locale locale) {
        return getMessage(locale, CONFIG_DESCRIPTION);
    }

    @Override
    public List<String> getSubcode(int siteId) {
        List<String> subcodeList = new ArrayList<String>();
        subcodeList.add(CONFIG_SUBCODE);
        return subcodeList;
    }

    @Override
    public List<ExtendField> getExtendFieldList(String subcode, Locale locale) {
        List<ExtendField> extendFieldList = new ArrayList<ExtendField>();
        extendFieldList.add(new ExtendField(true, getMessage(locale, CONFIG_DESCRIPTION + "." + CONFIG_APP_ID), null,
                CONFIG_APP_ID, INPUTTYPE_TEXT, null));
        extendFieldList.add(new ExtendField(true, getMessage(locale, CONFIG_DESCRIPTION + "." + CONFIG_PRIVATE_KEY), null,
                CONFIG_PRIVATE_KEY, INPUTTYPE_TEXTAREA, null));
        extendFieldList.add(new ExtendField(true, getMessage(locale, CONFIG_DESCRIPTION + "." + CONFIG_PUBLIC_KEY), null,
                CONFIG_PUBLIC_KEY, INPUTTYPE_TEXTAREA, null));
        return extendFieldList;
    }

    @Override
    public void clear() {
        clientCache.clear();
    }
}
