package aiyuan1996.cn.firerunning.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import aiyuan1996.cn.firerunning.R;
import aiyuan1996.cn.firerunning.entity.ContactInfo;
import aiyuan1996.cn.firerunning.ui.view.ViewHolder;

/**
 * Created by aiyuan on 2017/2/24.
 */

public class ContactAdapter extends BaseAdapter {
    // 用来控制CheckBox的选中状况
    private static HashMap<Integer,Boolean> isSelected;
    private List<ContactInfo> contactInfos;
    private Context context;

    public ContactAdapter(List<ContactInfo> contactInfos, Context context) {
        super();
        this.contactInfos = contactInfos;
        this.context = context;
        isSelected = new HashMap<Integer, Boolean>();
        initDate();
    }

    private void initDate() {
        for(int i=0; i<contactInfos.size();i++) {
            getIsSelected().put(i,false);
        }
    }

    public static HashMap<Integer,Boolean> getIsSelected() {
        return isSelected;
    }

    public static void setIsSelected(HashMap<Integer,Boolean> isSelected) {
        ContactAdapter.isSelected = isSelected;
    }

    @Override
    public int getCount() {
        return contactInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return contactInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = View.inflate(context, R.layout.item_list_contact_select, null);
            holder = new ViewHolder();
            holder.mNameTV = (TextView) convertView.findViewById(R.id.tv_name);
            holder.mPhoneTV = (TextView)convertView.findViewById(R.id.tv_phone);
            holder.checkBox = (CheckBox)convertView.findViewById(R.id.check_box);
            convertView.setTag(holder);

        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mNameTV.setText(contactInfos.get(position).getName());
        holder.mPhoneTV.setText(contactInfos.get(position).getPhone());
        holder.checkBox.setChecked(getIsSelected().get(position));
        return convertView;
    }

}
