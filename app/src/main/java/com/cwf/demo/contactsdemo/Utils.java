package com.cwf.demo.contactsdemo;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;

import com.cwf.demo.contactsdemo.permission.PermissionsActivity;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by n-240 on 2016/5/19.
 *
 * @author cwf
 * @email 237142681@qq.com
 */
public class Utils {

    /*拨打电话*/
    public static void call(Context context, String phoneNumber) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        context.startActivity(intent);
    }

    // ----------------得到本地联系人信息-------------------------------------
    public static List<ContactsInfo> getLocalContactsInfos(Context context) {
        List<ContactsInfo> localList = new ArrayList<>();
        try {
            ContentResolver cr = context.getContentResolver();
            String str[] = {ContactsContract.CommonDataKinds.Phone.CONTACT_ID, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER,
                    ContactsContract.CommonDataKinds.Phone.PHOTO_ID, ContactsContract.CommonDataKinds.Phone.SORT_KEY_PRIMARY};
            Cursor cur = cr.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, str, null,
                    null, ContactsContract.CommonDataKinds.Phone.SORT_KEY_PRIMARY);
            ContactsInfo contactsInfo;
            if (cur != null) {
                while (cur.moveToNext()) {
                    contactsInfo = new ContactsInfo();
                    contactsInfo.setPhone(cur.getString(cur
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));// 得到手机号码
                    contactsInfo.setName(cur.getString(cur
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
                    // contactsInfo.setContactsPhotoId(cur.getLong(cur.getColumnIndex(Phone.PHOTO_ID)));
                    long contactid = cur.getLong(cur
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                    contactsInfo.setId(contactid);
                    contactsInfo.setSortKey(cur.getString(cur
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.SORT_KEY_PRIMARY)));
                    long photoid = cur.getLong(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_ID));
                    // 如果photoid 大于0 表示联系人有头像 ，如果没有给此人设置头像则给他一个默认的
                    if (photoid > 0) {
                        Uri uri = ContentUris.withAppendedId(
                                ContactsContract.Contacts.CONTENT_URI, contactid);
                        InputStream input = ContactsContract.Contacts
                                .openContactPhotoInputStream(cr, uri);
                        contactsInfo.setBitmap(BitmapFactory.decodeStream(input));
                    } else {
                        contactsInfo.setBitmap(BitmapFactory.decodeResource(
                                context.getResources(), R.mipmap.ic_launcher));
                    }

                    System.out.println("---------联系人电话--"
                            + contactsInfo.getPhone() + ":" + contactsInfo.getSortKey());
                    localList.add(contactsInfo);

                }
            }
            cur.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return localList;

    }

    /*获得sim卡上的联系人*/
    public static List<ContactsInfo> getSIMContactsInfos(Context context) {

        List<ContactsInfo> SIMList = new ArrayList<>();
        try {
            TelephonyManager mTelephonyManager = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            System.out.println("---------SIM--------");
            ContentResolver cr = context.getContentResolver();
            final String SIM_URI_ADN = "content://icc/adn";// SIM卡


            Uri uri = Uri.parse(SIM_URI_ADN);
            Cursor cursor = cr.query(uri, null, null, null, null);
            ContactsInfo SIMContactsInfo;
            while (cursor.moveToFirst()) {
                SIMContactsInfo = new ContactsInfo();
                SIMContactsInfo.setName(cursor.getString(cursor
                        .getColumnIndex("name")));
                SIMContactsInfo
                        .setPhone(cursor.getString(cursor
                                .getColumnIndex("number")));
                SIMContactsInfo
                        .setBitmap(BitmapFactory.decodeResource(
                                context.getResources(),
                                R.mipmap.ic_launcher));
                SIMList.add(SIMContactsInfo);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return SIMList;
    }
}
