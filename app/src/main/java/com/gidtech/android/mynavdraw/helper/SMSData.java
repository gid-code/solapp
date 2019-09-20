package com.gidtech.android.mynavdraw.helper;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

/**
 * Created by gid on 3/26/2017.
 */

public class SMSData {
    //Number from which the sms was sent
    private String number;

    //SMS text body
    private String body;

    public String getNumber(){
        return number;
    }

    public void setNumber(String number){
        this.number = number;
    }

    public String getBody(){
        return body;
    }

    public void setBody(String body){
        this.body = body;
    }

    public void getData(Context context){
        Cursor c = context.getContentResolver().query(Uri.parse("content://sms/inbox"),
                new String[]{"address","body"},
                null,
                null,
                null);
    }
}
