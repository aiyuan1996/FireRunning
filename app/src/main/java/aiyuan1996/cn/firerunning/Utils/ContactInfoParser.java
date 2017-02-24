package aiyuan1996.cn.firerunning.Utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import aiyuan1996.cn.firerunning.entity.ContactInfo;

/**
 * 联系人信息都存储在sqlite数据库中，因此需要先获取到联系人的id，根据id在data表中查询联系人名字以及电话号码，并封装在contactinfo，然后存入list集合
 * @author aiyuan
 *
 */
public class ContactInfoParser {
    private static String TAG = "ContactInfoParser";
    public static List<ContactInfo> getSystemContact(Context context){
        ContentResolver resolver = context.getContentResolver();
        //1.查询raw_contacts表，把联系人的id取出来
        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
        Uri dataUri = Uri.parse("content://com.android.contacts/data");
        List<ContactInfo> infos = new ArrayList<ContactInfo>();
        Cursor cursor = resolver.query(uri, new String[]{"contact_id"}, null, null, null);
        while(cursor.moveToNext()){
            String id = cursor.getString(0);
            if(id != null){
                Log.d(TAG,"联系人id：" + id);
                ContactInfo info = new ContactInfo();
                info.setId(id);
                //根据联系人的id查询data表，把这个id‘的数据取出来
                //系统api查询data表的时候，不是真正查询data表，而是查询data表的视图
                Cursor dataCursor = resolver.query(dataUri, new String[]{"data1","mimetype"} ,"raw_contact_id=?" ,new String[]{id}, null);
                while(dataCursor.moveToNext()){
                    String data1 = dataCursor.getString(0);
                    String mimetype = dataCursor.getString(1);
                    if("vnd.android.cursor.item/name".equals(mimetype)){
                        Log.d(TAG,"姓名="+ data1);
                        info.setName(data1);
                    }else if("vnd.android.cursor.item/phone_v2".equals(mimetype)){
                        Log.d(TAG,"电话="+ data1);
                        info.setPhone(data1);
                    }
                }
                //如果姓名和手机都为空，则跳过该条数据
                if(TextUtils.isEmpty(info.getName()) && TextUtils.isEmpty(info.getPhone()))
                    continue;
                infos.add(info);
                dataCursor.close();
            }
        }
        cursor.close();
        return infos;
    }

    /**
     * 得到sim卡的所有联系人信息
     * @param context
     * @return contactinfo的list集合
     */
    public static  List<ContactInfo> getSimContacts( Context context){
        Uri uri = Uri.parse("content://icc/adn");
        List<ContactInfo> infos = new ArrayList<ContactInfo>();
        Cursor mCursor = context.getContentResolver().query(uri, null, null, null, null);
        if (mCursor != null) {
            while (mCursor.moveToNext()) {
                ContactInfo info = new ContactInfo();
                // 取得联系人名字
                int nameFieldColumnIndex = mCursor.getColumnIndex("name");
                info.setName(mCursor.getString(nameFieldColumnIndex));
                // 取得电话号码
                int numberFieldColumnIndex = mCursor.getColumnIndex("number");
                info.setPhone(mCursor.getString(numberFieldColumnIndex));
                infos.add(info);
            }
        }
        mCursor.close();
        return infos;
    }
}
