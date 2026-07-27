package io.github.lazyimmortal.sesame.util.idMap;

public class VitalityBenefitIdMap extends AbstractIdMap<String> {

    @Override
    protected String getFileName() {
        return "vitalityBenefit.json";
    }

    public static VitalityBenefitIdMap getInstance() {
        return VitalityBenefitIdMap.getInstance(VitalityBenefitIdMap.class);
    }
}
