package com.example.macrosocietyapp.models;

public class Community {
    private int id;
    private String name;
    private String description;
    private int creatorId;
    private boolean isMember;

    public Community() {}

    public Community(String name, String description, int creatorId) {
        this.name = name;
        this.description = description;
        this.creatorId = creatorId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getCreatorId() { return creatorId; }
    public void setCreatorId(int creatorId) { this.creatorId = creatorId; }

    public boolean isMember() { return isMember; }
    public void setMember(boolean member) { isMember = member; }
}


