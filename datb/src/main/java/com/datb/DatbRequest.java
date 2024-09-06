package com.datb;

import java.io.Serializable;

public class DatbRequest implements Serializable {
    private String url;
    private String widgetId;
    private int idx;
    public DatbRequest()
   {
       this(null, null);
   }
    public DatbRequest(String url, String widgetId) {
        this(url, 0, widgetId);
    }

    public DatbRequest(String url, int pageViewIndex, String widgetId) {
        this.url = url;
        this.idx = pageViewIndex;
        this.widgetId = widgetId;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
    public void setWidgetIndex(int idx) {
        this.idx = idx;
    }
    public int getIdx() {
        return idx;
    }
    public void setWidgetId(String widgetId) {
        this.widgetId = widgetId;
    }
    public String getWidgetId() {
        return widgetId;
    }

}
