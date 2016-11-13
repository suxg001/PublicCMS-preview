package com.publiccms.component.payment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.spi.GateWay;
import com.sanluan.common.base.Base;

@Component
public class PaymentComponent extends Base {
    private List<GateWay> gateWayList;
    private Map<String, GateWay> gateWayMap = new HashMap<String,GateWay>();

    public boolean pay(int siteId, String gateWayCode, long orderId) {
        return false;
    }

    public List<String> getAvailableGateWayList(int siteId) {
        List<String> gateWayInfoList = new ArrayList<String>();
        for (GateWay gateWay : gateWayList) {
            try {
                if (gateWay.available(siteId)) {
                    gateWayInfoList.add(gateWay.getGateWayCode());
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        return gateWayInfoList;
    }

    @Autowired
    public void setGateWayList(List<GateWay> gateWayList) {
        this.gateWayList = gateWayList;
        for (GateWay gateWay : gateWayList) {
            gateWayMap.put(gateWay.getGateWayCode(), gateWay);
        }
    }
}
