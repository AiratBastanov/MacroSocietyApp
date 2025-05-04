package com.example.macrosocietyapp.models;

public class CommunityCreateDto {
    private String name;
    private String description;
    private String creatorId; // encrypted

    public CommunityCreateDto(String name, String description, String creatorId) {
        this.name = name;
        this.description = description;
        this.creatorId = creatorId;
    }

    // геттеры и сеттеры, если нужно
    public String getName() { return name; }

    public String getDescription() { return description; }

    public String getCreatorId() { return creatorId; }
}
