package io.github.lazyimmortal.sesame.entity;

import java.util.Objects;

import io.github.lazyimmortal.sesame.util.StringUtil;
import lombok.Getter;

@Getter
public class LLMEntity {
    private final String baseUrl;
    private final String apiKey;
    private final String model;
    private final String name;

    public LLMEntity() {
        baseUrl = apiKey = model = name = null;
    }

    public LLMEntity(String baseUrl, String apiKey, String model, String name) {
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;
        this.model = model;
        this.name = StringUtil.isEmpty(name) ? String.valueOf(hashCode()) : name;
    }

    public String id() {
        return String.valueOf(Objects.hash(baseUrl, apiKey, model));
    }

    public String name() {
        return "兼容大模型:" + name;
    }
}
