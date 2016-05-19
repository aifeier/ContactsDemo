package com.cwf.demo.contactsdemo;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.cwf.demo.contactsdemo.permission.PermissionsActivity;
import com.cwf.demo.contactsdemo.permission.PermissionsChecker;

import java.util.HashMap;
import java.util.List;

/**
 * Created by n-240 on 2016/5/19.
 *
 * @author cwf
 * @email 237142681@qq.com
 */
public class MainActivity extends BaseActivity {
    private ExpandableListView expandableListView;

    private ContactsAdapter contactsAdapter;

    private HashMap<Integer, List<ContactsInfo>> contactsMap;

    private String[] permissions = new String[]{Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_CONTACTS};
    private PermissionsChecker permissionsChecker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getToolbar().setNavigationIcon(null);
        expandableListView = (ExpandableListView) findViewById(R.id.expandable_listview);
        if (contactsMap == null)
            contactsMap = new HashMap<>();
        contactsMap.clear();
        contactsAdapter = new ContactsAdapter(contactsMap);
        expandableListView.setAdapter(contactsAdapter);
        permissionsChecker = new PermissionsChecker(this);
    }

    private void getContacts() {
        contactsMap.put(0, Utils.getLocalContactsInfos(this));
        contactsMap.put(1, Utils.getSIMContactsInfos(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (permissionsChecker.lacksPermissions(permissions))
            PermissionsActivity.startActivityForResult(this, 1, permissions);
        else {
            getContacts();
            contactsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == PermissionsActivity.PERMISSIONS_GRANTED) {
            getContacts();
            contactsAdapter.notifyDataSetChanged();
        } else {
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
