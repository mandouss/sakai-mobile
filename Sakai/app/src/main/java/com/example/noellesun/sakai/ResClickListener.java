package com.example.noellesun.sakai;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * Created by sifanw on 4/11/18.
 */

public class ResClickListener implements OnItemClickListener {
    private ListAdapterRes listAdapterRes;
    //private Context mContext;

    public ResClickListener (ListAdapterRes listAdapterRes) {
        //this.mContext = context;
        this.listAdapterRes = listAdapterRes;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        //点击的item代表的元素
        ListCellRes element = (ListCellRes) listAdapterRes.getItem(position);
        //树中的元素
        ArrayList<ListCellRes> elements = listAdapterRes.getResCells();
        //元素的数据源
        ArrayList<ListCellRes> elementsData = listAdapterRes.getResCellsData();

        //点击没有子项的item直接返回
        if (!element.isHasChildren()) {

            //return;
            Intent intent = new Intent();
            intent.setClassName("com.example.noellesun.sakai.ResClickListener","com.example.noellesun.sakai.eachResource");
            //intent.setClass(this, eachResource.class);
            //Intent intent = new Intent(ListAdapterRes.this, eachResource.class);
            //send the resource info to each Resource view
            intent.putExtra("resource info", element.getEachRes());
            //intent.putExtra("activityLabelclick", activityLabelclick);
            //intent.putExtra("resource info", resList);
            //startActivity(intent);

        }

        if (element.isExpanded()) {
            element.setExpanded(false);
            //删除节点内部对应子节点数据，包括子节点的子节点...
            ArrayList<ListCellRes> elementsToDel = new ArrayList<ListCellRes>();
            for (int i = position + 1; i < elements.size(); i++) {
                if (element.getLevel() >= elements.get(i).getLevel())
                    break;
                elementsToDel.add(elements.get(i));
            }
            elements.removeAll(elementsToDel);
            listAdapterRes.notifyDataSetChanged();
        } else {
            element.setExpanded(true);
            //从数据源中提取子节点数据添加进树，注意这里只是添加了下一级子节点，为了简化逻辑
            int i = 1;//注意这里的计数器放在for外面才能保证计数有效
            for (ListCellRes e : elementsData) {
                if (e.getPid() == element.getResId()) {
                    e.setExpanded(false);
                    elements.add(position + i, e);
                    i ++;
                }
            }
            listAdapterRes.notifyDataSetChanged();
        }
    }

}
