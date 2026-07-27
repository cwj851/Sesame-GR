package io.github.lazyimmortal.sesame.util.idMap;

public class NeverLandBenefitIdMap extends AbstractIdMap<String> {

    @Override
    protected String getFileName() {
        return "neverLandBenefit.json";
    }

    public static NeverLandBenefitIdMap getInstance() {
        return NeverLandBenefitIdMap.getInstance(NeverLandBenefitIdMap.class);
    }
}
