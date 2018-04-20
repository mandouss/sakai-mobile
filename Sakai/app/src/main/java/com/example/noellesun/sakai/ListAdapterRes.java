package com.example.noellesun.sakai;
import java.util.ArrayList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Created by sifanw on 4/11/18.
 */

public class ListAdapterRes extends BaseAdapter {
    private ArrayList<ListCellRes> ResCellsData;

    private ArrayList<ListCellRes> ResCells;

    private LayoutInflater inflater;

    private int indentionBase;

    public ListAdapterRes(ArrayList<ListCellRes> elements, ArrayList<ListCellRes> elementsData, LayoutInflater inflater) {
        this.ResCellsData = elementsData;
        this.ResCells = elements;
        this.inflater = inflater;
        indentionBase = 20;

    }

    public ArrayList<ListCellRes> getResCells() {
        return ResCells;
    }

    public ArrayList<ListCellRes> getResCellsData() {
        return ResCellsData;
    }

    @Override
    public int getCount() {
        return ResCells.size();
    }

    @Override
    public Object getItem(int position) {
        return ResCells.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.resource_listitem, null);
            holder.disclosureImg = (ImageView) convertView.findViewById(R.id.disclosureImg);
            holder.itemName = (TextView) convertView.findViewById(R.id.itemName);
            holder.createdBy = (TextView) convertView.findViewById(R.id.createdBy);
            holder.size = (TextView) convertView.findViewById(R.id.size);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ListCellRes element = ResCells.get(position);
        int level = element.getLevel();
        holder.disclosureImg.setPadding(
                indentionBase * (level + 1),
                holder.disclosureImg.getPaddingTop(),
                holder.disclosureImg.getPaddingRight(),
                holder.disclosureImg.getPaddingBottom());
        holder.itemName.setText(element.getResitemName());
        holder.createdBy.setText(element.getRescreatedBy());
        holder.size.setText(element.getRessize());
        if (element.isHasChildren() && !element.isExpanded()) {
            holder.disclosureImg.setImageResource(R.mipmap.ic_res);
            //这里要主动设置一下icon可见，因为convertView有可能是重用了"设置了不可见"的view，下同。
            holder.disclosureImg.setVisibility(View.VISIBLE);
        } else if (element.isHasChildren() && element.isExpanded()) {
            holder.disclosureImg.setImageResource(R.mipmap.ic_res);
            holder.disclosureImg.setVisibility(View.VISIBLE);
        } else if (!element.isHasChildren()) {
            holder.disclosureImg.setImageResource(R.mipmap.ic_res);
            holder.disclosureImg.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }

    /**
     * 优化Holder
     * @author carrey
     *
     */
    static class ViewHolder{
        ImageView disclosureImg;
        TextView itemName;
        TextView createdBy;
        TextView size;
    }



}
