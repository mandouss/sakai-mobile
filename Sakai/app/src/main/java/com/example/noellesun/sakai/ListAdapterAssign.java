package com.example.noellesun.sakai;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ss on 3/31/18.
 */

public class ListAdapterAssign extends ArrayAdapter {

    LayoutInflater inflater;
    public ListAdapterAssign(Context context, ArrayList<ListCellAssign> items) {
        super(context, 0, items);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ListCellAssign cell = (ListCellAssign) getItem(position);

        //If the cell is a section header we inflate the header layout
        if(cell.isAssignHeader())
        {
            v = inflater.inflate(R.layout.section_header_assign, null);

            v.setClickable(false);

            TextView header = (TextView) v.findViewById(R.id.section_header_assign);
            header.setText(cell.getAssignId());
        }
        else
        {
            v = inflater.inflate(R.layout.list_item_assign, null);
            TextView titleName = (TextView) v.findViewById(R.id.assigntitle);
            TextView subtitle = (TextView) v.findViewById(R.id.assignsubtitle);
            TextView dueDate = (TextView) v.findViewById(R.id.dueTime);

            titleName.setText(cell.getAssignTitlename());
            subtitle.setText("Grade Scale: " + cell.getAssignSubtitle());
            dueDate.setText("Due Time: "+ cell.getDueTime());
        }
        return v;
    }
}
