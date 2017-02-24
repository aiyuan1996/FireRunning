package aiyuan1996.cn.firerunning.ui.PushActivity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import aiyuan1996.cn.firerunning.R;
import aiyuan1996.cn.firerunning.adapter.WebViewAdapter;
import aiyuan1996.cn.firerunning.entity.HistoryUrl;

/**
 * Created by aiyuan on 2017/2/20
 */
public class webViewActivity extends AppCompatActivity {
    private static final String TAG = "webViewActivity";
    private SwipeRefreshLayout swipeReflush;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        swipeReflush = (SwipeRefreshLayout)findViewById(R.id.swipe_reflush);
        swipeReflush.setColorSchemeResources(R.color.colorPrimary);
        swipeReflush.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showHistory();
            }
        });
        showHistory();
    }

    private void showHistory() {
        ListView listView = (ListView)findViewById(R.id.list_view);
        List<String> webViews = new ArrayList<>();
        List<HistoryUrl> historyUrls = DataSupport.findAll(HistoryUrl.class);
        for(HistoryUrl historyUrl : historyUrls){
            webViews.add(historyUrl.getUrlPath());
        }
        WebViewAdapter webViewAdapter = new WebViewAdapter(this, webViews);
        listView.setAdapter(webViewAdapter);
        swipeReflush.setRefreshing(false);
    }

}
