package io.github.lazyimmortal.sesame.entity.idAndName;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.github.lazyimmortal.sesame.entity.UserEntity;
import io.github.lazyimmortal.sesame.util.Log;
import io.github.lazyimmortal.sesame.util.idMap.UserIdMap;

public class AlipayUser extends IdAndName {

    public AlipayUser(String id, String name) {
        super(id, name);
    }

    public static List<AlipayUser> getList() {
        return getList(user -> true);
    }

    public static List<AlipayUser> getList(Filter filterFunc) {
        List<AlipayUser> list = new ArrayList<>();
        Map<String, UserEntity> userIdMap = UserIdMap.getUserMap();
        for (Map.Entry<String, UserEntity> entry : userIdMap.entrySet()) {
            UserEntity userEntity = entry.getValue();
            try {
                if (filterFunc.apply(userEntity)) {
                    list.add(new AlipayUser(entry.getKey(), userEntity.getFullName()));
                }
            } catch (Throwable t) {
                Log.printStackTrace(t);
            }
        }
        return list;
    }

    public interface Filter {

        Boolean apply(UserEntity user);

    }

}
