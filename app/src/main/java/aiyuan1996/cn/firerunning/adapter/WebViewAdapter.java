package aiyuan1996.cn.firerunning.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;

import java.util.List;

import aiyuan1996.cn.firerunning.R;

/**
 * Created by aiyuan on 2017/2/23.
 */

public class WebViewAdapter extends BaseAdapter{
    private static final String TAG = "WebViewAdapter";

    private Context context;
    private List<String> mlist;

    public WebViewAdapter(Context context, List<String>list) {
        this.context = context;
        this.mlist = list;
    }

    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Object getItem(int position) {
        return mlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        ViewHolder viewHolder = new ViewHolder();
        if (convertView != null) {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.webview_item, parent, false);
            viewHolder.mWebView = (WebView) view.findViewById(R.id.id_webview);
            view.setTag(viewHolder);
        }
        viewHolder.mWebView.loadUrl(mlist.get(position));
        return view;
    }

    class ViewHolder{
        WebView mWebView;
    }
}
