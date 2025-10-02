package org.example.backend.f1.crawling;

import lombok.Getter;

@Getter
public class F1TeamBasicInfo {
    private final String name;
    private final String url;

    public F1TeamBasicInfo(String name, String url) {
        this.name = name;
        this.url = url;
    }
}
