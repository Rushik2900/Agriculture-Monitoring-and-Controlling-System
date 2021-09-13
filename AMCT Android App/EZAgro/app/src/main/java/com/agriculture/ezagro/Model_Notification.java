package com.agriculture.ezagro;

public class Model_Notification {
    public String alert_head, alert_desc;
    public String alert_time;
    public String str;

    /*
    public Model_Notification()
    {

    }
    public Model_Notification(String str)
    {
        this.str=str;
        String temp1[] = str.split(":");
        this.alert_head=temp1[0];
        this.alert_desc=temp1[1];
    }
    */
    public String getAlert_head() {
        return alert_head;
    }

    public String getAlert_time() {
        return alert_time;
    }
    public String getAlert_desc() {
        return alert_desc;
    }
}
