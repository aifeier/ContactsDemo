package com.cwf.demo.contactsdemo;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

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

    private final int REQUEST_CONTACTS = 0x0001;
    private final int REQUEST_CALL = 0x0002;

    private ExpandableListView expandableListView;

    private ContactsAdapter contactsAdapter;

    private HashMap<Integer, List<ContactsInfo>> contactsMap;

    private String[] permissions = new String[]{Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_CONTACTS};
    private String[] permissions_call = new String[]{Manifest.permission.CALL_PHONE};
    private PermissionsChecker permissionsChecker;

    private AlertDialog.Builder builder;
    private String phone = "10086";

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
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                showClickDialog(contactsMap.get(groupPosition).get(childPosition));
                return false;
            }
        });
    }

    private void showClickDialog(final ContactsInfo contactsInfo) {
        if (builder == null) {
            builder = new AlertDialog.Builder(this);
            builder.setCancelable(true);
            builder.setTitle("拨打电话？");
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        builder.setMessage("是否拨打电话：" + contactsInfo.getPhone());
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                phone = contactsInfo.getPhone();
                if (permissionsChecker.lacksPermissions(permissions_call))
                    PermissionsActivity.startActivityForResult(MainActivity.this, REQUEST_CALL, permissions_call);
                else {
                    Utils.call(MainActivity.this, contactsInfo.getPhone());
                }
            }
        });
        builder.show();
    }

    private void getContacts() {
        contactsMap.put(0, Utils.getLocalContactsInfos(this));
        contactsMap.put(1, Utils.getSIMContactsInfos(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (permissionsChecker.lacksPermissions(permissions))
            PermissionsActivity.startActivityForResult(this, REQUEST_CONTACTS, permissions);
        else {
            getContacts();
            contactsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CALL:
                if (resultCode == PermissionsActivity.PERMISSIONS_GRANTED) {
                    Utils.call(MainActivity.this, phone);
                } else {
                    Toast.makeText(MainActivity.this, "没有拨打电话权限", Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_CONTACTS:
                if (resultCode == PermissionsActivity.PERMISSIONS_GRANTED) {
                    getContacts();
                    contactsAdapter.notifyDataSetChanged();
                } else {
                    finish();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
