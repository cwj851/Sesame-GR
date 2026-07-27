package io.github.lazyimmortal.sesame.entity.idAndName;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.github.lazyimmortal.sesame.util.idMap.FlashSaleIdMap;

public class FlashSale extends IdAndName {

    public FlashSale(String id, String name) {
        super(id, name);
    }

    public static List<FlashSale> getList() {
        List<FlashSale> list = new ArrayList<>();
        Set<Map.Entry<String, String>> idSet = FlashSaleIdMap.getInstance().getMap().entrySet();
        for (Map.Entry<String, String> entry : idSet) {
            list.add(new FlashSale(entry.getKey(), entry.getValue()));
        }
        return list;
    }
}
