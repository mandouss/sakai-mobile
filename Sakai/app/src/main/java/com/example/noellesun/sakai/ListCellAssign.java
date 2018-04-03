package com.example.noellesun.sakai;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by ss on 3/31/18.
 */

public class ListCellAssign implements Comparable<ListCellAssign>{
    private String id;
    private String titleName;
    private String category;
    private String subtitle;
    private String dueTime;
    private HashMap<String, String> eachAssign = new HashMap<>();
    private boolean isAssignHeader;
    // dueDate: gradeScale
    //
    public ListCellAssign(String id, String titlename, String status, String gradeScale, String dueDate, HashMap<String, String> eachAssign) {
        this.id = id;
        this.titleName = titlename;
        this.category = status;
        this.subtitle =  gradeScale;
        this.dueTime= dueDate;
        if(eachAssign != null) {
            Iterator entries = eachAssign.entrySet().iterator();
            while (entries.hasNext()) {
                HashMap.Entry entry = (HashMap.Entry) entries.next();
                String key = (String)entry.getKey();
                String value = (String) entry.getValue();
                this.eachAssign.put(key, value);
            }
        }

        isAssignHeader = false;
    }
    public String getAssignId() { return id; }
    public String getAssignTitlename() { return titleName;}

    public String getAssignCategory() { return category; }

    public String getAssignSubtitle() { return subtitle; }

    public void setToAssignHeader() {
        isAssignHeader = true;
    }

    public boolean isAssignHeader() { return isAssignHeader; }


    public String getDueTime() { return dueTime; }
    public HashMap<String, String> getEachAssign() { return eachAssign; }

    @Override
    public int compareTo(ListCellAssign other) {
        if(this.category.equals(other.getAssignCategory())) {
            return other.getAssignTitlename().compareTo(this.titleName);
        } else {
            return this.category.compareTo(other.getAssignCategory());
        }
    }
}
