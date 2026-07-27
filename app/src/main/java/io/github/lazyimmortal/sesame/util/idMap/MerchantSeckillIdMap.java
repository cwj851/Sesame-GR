package io.github.lazyimmortal.sesame.util.idMap;

public class MerchantSeckillIdMap extends AbstractIdMap<String> {
    @Override
    protected String getFileName() {
        return "MerchantSeckill.json";
    }

    public static MerchantSeckillIdMap getInstance() {
        return MerchantSeckillIdMap.getInstance(MerchantSeckillIdMap.class);
    }
}
