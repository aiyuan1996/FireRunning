package aiyuan1996.cn.firerunning.entity;

import org.litepal.crud.DataSupport;

/**
 * Created by aiyuan on 2017/2/23.
 */

public class HistoryUrl extends DataSupport{
    private int notificationId;
    private String alert;
    private String urlPath;

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public void setAlert(String alert) {
        this.alert = alert;
    }

    public void setUrlPath(String urlPath) {
        this.urlPath = urlPath;
    }

    public int getNotificationId() {

        return notificationId;
    }

    public String getAlert() {
        return alert;
    }

    public String getUrlPath() {
        return urlPath;
    }
}
