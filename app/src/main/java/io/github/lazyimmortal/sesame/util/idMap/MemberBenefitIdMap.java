package io.github.lazyimmortal.sesame.util.idMap;

public class MemberBenefitIdMap extends AbstractIdMap<String> {
    @Override
    protected String getFileName() {
        return "memberBenefit.json";
    }

    public static MemberBenefitIdMap getInstance() {
        return MemberBenefitIdMap.getInstance(MemberBenefitIdMap.class);
    }
}
