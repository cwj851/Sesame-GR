package io.github.lazyimmortal.sesame.rpc.request;

import java.util.Objects;

public enum RequestType {
    SESAME_REQUEST,
    UNKNOWN_TYPE,
    ENABLE_DEVELOPER_MODE,
    ADD_EXTENSION_VIEW,
    RECORD_RUNTIME_INFO,
    GET_TREE_ITEMS,
    GET_NEW_TREE_ITEMS,
    GET_UNLOCK_TREE_ITEMS,
    QUERY_AREA_TREES,
    QUERY_PROP_LIST,
    QUERY_FARM_FOOD,
    QUERY_FRIEND_ENERGY,
    DISH_IMAGE_NOW,
    QUERY_USER_COOPERATE_PLANT_LIST,
    DO_FARM_TASK,
    DO_FARM_IP_DRAW_TASK,
    DO_FARM_DRAW_TIMES_TASK,
    ADD_CUSTOM_WALK_PATH_ID,
    @Deprecated
    ADD_CUSTOM_WALK_PATH_ID_QUEUE,
    @Deprecated
    CLEAR_CUSTOM_WALK_PATH_ID_QUEUE,
    SYNC_STEP_COUNT_NOW,
    FETCH_RANKING;

    public static RequestType getRequestType(String type) {
        for (RequestType requestType : RequestType.values()) {
            if (Objects.equals(requestType.name(), type)) {
                return requestType;
            }
        }
        return UNKNOWN_TYPE;
    }
}