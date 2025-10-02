package org.example.backend.f1.crawling;

public class DriverBasicInfo {
    private final String name;
    private final String team;
    private final String url;

    public DriverBasicInfo(String name, String team, String url) {
        this.name = name;
        this.team = team;
        this.url = url;
    }
    public String getName() { return name; }
    public String getTeam() { return team; }
    public String getUrl() { return url; }
}
