package com.example.noellesun.sakai;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by sifanw on 4/11/18.
 */

public class ListCellRes{

    private String itemName;
    private String numChildren;
    private String createdBy;
    private String resource_url;
    private String type;
    private String size;

    private int id;
    private int level;
    private int pid;
    private boolean hasChildren;
    private boolean isExpanded;

    public static final int NO_PARENT = -1;
    public static final int TOP_LEVEL = 0;


    private HashMap<String, String> eachRes = new HashMap<>();
    // dueDate: gradeScale
    //
    public ListCellRes(String id, String itemName, String numChildren, String createdBy, String resource_url, String type, String size, HashMap<String, String> eachRes) {
        this.id = Integer.parseInt(id);
        this.itemName = itemName;
        this.numChildren = numChildren;
        this.createdBy = createdBy;
        this.resource_url = resource_url;
        this.type = type;
        this.size = size;

        if(eachRes != null) {
            Iterator entries = eachRes.entrySet().iterator();
            while (entries.hasNext()) {
                HashMap.Entry entry = (HashMap.Entry) entries.next();
                String key = (String)entry.getKey();
                String value = (String) entry.getValue();
                this.eachRes.put(key, value);
            }
        }

        this.hasChildren = false;
        this.isExpanded = false;

        this.level = TOP_LEVEL;
        this.pid = NO_PARENT;

    }
    public int getResId() { return id; }
    public String getResitemName() { return itemName;}
    public String getResnumChildren() { return numChildren; }
    public String getRescreatedBy() { return createdBy; }
    public String getResresource_url() { return resource_url; }
    public String getRestype() { return type; }
    public String getRessize() { return size; }

    public void setlevel(int l) {
        this.level = l;
    }
    public void setpid(int p) {
        this.pid = p;
    }
    public void setExpanded(boolean e) {
        this.isExpanded = e;
    }
    public void setHasChildren(boolean h) {
        this.hasChildren = h;
    }

    public boolean isExpanded() { return isExpanded; }
    public boolean isHasChildren() { return hasChildren; }
    public int getLevel() {return level;}
    public int getPid() {return pid;}



    public HashMap<String, String> getEachRes() { return eachRes; }

//    @Override
//    public int compareTo(ListCellAssign other) {
//        if(this.category.equals(other.getAssignCategory())) {
//            return other.getAssignTitlename().compareTo(this.titleName);
//        } else {
//            return this.category.compareTo(other.getAssignCategory());
//        }
//    }
}
