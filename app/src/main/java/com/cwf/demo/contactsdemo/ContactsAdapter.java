package com.cwf.demo.contactsdemo;

import android.provider.ContactsContract;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by n-240 on 2016/5/19.
 * 列表适配
 *
 * @author cwf
 * @email 237142681@qq.com
 */
public class ContactsAdapter extends BaseExpandableListAdapter {
    private HashMap<Integer, List<ContactsInfo>> contactsMap;

    public ContactsAdapter(HashMap<Integer, List<ContactsInfo>> contactsMap) {
        this.contactsMap = contactsMap;
    }

    @Override
    public int getGroupCount() {
        return contactsMap.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return contactsMap.get(groupPosition).size();
    }

    @Override
    public CharSequence getGroup(int groupPosition) {
        switch (groupPosition) {
            case 0:
                return "手机";
            case 1:
                return "SIM卡";
            default:
                return "未知";
        }
    }

    @Override
    public ContactsInfo getChild(int groupPosition, int childPosition) {
        return contactsMap.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupHolder groupHolder;
        if (convertView == null) {
            groupHolder = new GroupHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group, null);
            groupHolder.title = (AppCompatTextView) convertView.findViewById(R.id.group_title);
            groupHolder.num = (AppCompatTextView) convertView.findViewById(R.id.group_num);
            convertView.setTag(groupHolder);
        } else {
            groupHolder = (GroupHolder) convertView.getTag();
        }
        groupHolder.title.setText(getGroup(groupPosition));
        groupHolder.num.setText(getChildrenCount(groupPosition) + "");
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        GroupHolder groupHolder;
        if (convertView == null) {
            groupHolder = new GroupHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_children, null);
            groupHolder.title = (AppCompatTextView) convertView.findViewById(R.id.children_name);
            groupHolder.num = (AppCompatTextView) convertView.findViewById(R.id.children_num);
            convertView.setTag(groupHolder);
        } else {
            groupHolder = (GroupHolder) convertView.getTag();
        }
        groupHolder.title.setText(getChild(groupPosition, childPosition).getName());
        groupHolder.num.setText(getChild(groupPosition, childPosition).getPhone());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public class GroupHolder {
        AppCompatTextView title;
        AppCompatTextView num;
    }
}
