package io.github.lazyimmortal.sesame.util.idMap;

public class FlashSaleIdMap extends AbstractIdMap<String> {
    @Override
    protected String getFileName() {
        return "FlashSale.json";
    }

    public static FlashSaleIdMap getInstance() {
        return FlashSaleIdMap.getInstance(FlashSaleIdMap.class);
    }
}
