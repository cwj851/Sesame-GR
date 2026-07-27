package io.github.lazyimmortal.sesame.entity.idAndName;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.github.lazyimmortal.sesame.util.idMap.MemberBenefitIdMap;
import io.github.lazyimmortal.sesame.util.idMap.MerchantSeckillIdMap;

public class MerchantSeckill extends IdAndName {

    public MerchantSeckill(String id, String name) {
        super(id, name);
    }

    public static List<MerchantSeckill> getList() {
        List<MerchantSeckill> list = new ArrayList<>();
        Set<Map.Entry<String, String>> idSet = MerchantSeckillIdMap.getInstance().getMap().entrySet();
        for (Map.Entry<String, String> entry : idSet) {
            list.add(new MerchantSeckill(entry.getKey(), entry.getValue()));
        }
        return list;
    }
}
