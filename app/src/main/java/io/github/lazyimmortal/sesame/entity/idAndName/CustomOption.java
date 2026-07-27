package io.github.lazyimmortal.sesame.entity.idAndName;

import java.util.ArrayList;
import java.util.List;

public interface CustomOption {
    String nickName();

    static String nickName(Enum<? extends CustomOption> option) {
        try {
            return (String) option.getClass().getMethod("nickName").invoke(option);
        } catch (Exception e) {
            return null;
        }
    }

    static List<CustomIdAndName> getList(Class<? extends Enum<? extends CustomOption>> enumClass) {
        return getList(enumClass.getEnumConstants());
    }

    static List<CustomIdAndName> getList(Enum<? extends CustomOption>[] enumConstants) {
        if (enumConstants == null) {
            return new ArrayList<>();
        }
        List<CustomIdAndName> list = new ArrayList<>();
        for (Enum<? extends CustomOption> enumConstant : enumConstants) {
            list.add(new CustomIdAndName(enumConstant.name(), nickName(enumConstant)));
        }
        return list;
    }

    class CustomIdAndName extends IdAndName {
        public CustomIdAndName(String id, String name) {
            super(id, name);
        }
    }
}
