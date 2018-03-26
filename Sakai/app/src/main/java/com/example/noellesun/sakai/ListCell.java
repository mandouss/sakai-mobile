package com.example.noellesun.sakai;

/**
 * Created by naiyuyin on 3/26/18.
 */

public class ListCell implements Comparable<ListCell>{
    private String id;
    private String titleName;
    private String category;
    private String subtitle;
    private boolean isSectionHeader;
    //private String

    //Constructor
    public ListCell(String id, String titlename, String category, String subtitle) {
        this.id = id;
        this.titleName = titlename;
        this.category = category;
        this.subtitle = subtitle;
        isSectionHeader = false;
    }

    public String getId() {
        return id;
    }

    public String getTitlename() {
        return titleName;
    }

    public String getCategory() {
        return category;
    }

    public String getSubtitle() { return subtitle; }

    public void setToSectionHeader() {
        isSectionHeader = true;
    }

    public boolean isSectionHeader() {
        return isSectionHeader;
    }

    @Override
    public int compareTo(ListCell other) {
        return this.category.compareTo(other.category);
    }
}

