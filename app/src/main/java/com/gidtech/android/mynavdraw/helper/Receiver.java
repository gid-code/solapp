package com.gidtech.android.mynavdraw.helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

/**
 * Created by gid on 5/26/2017.
 */

public class Receiver extends BroadcastReceiver{
    public Receiver(){
    }

    SessionManager session;
    String phone ;
    String phoneS,msg;

    String raw;


    @Override
    public void onReceive(Context context, Intent intent) {

        session = new SessionManager(context);
        phone = session.getPhone_Reg();
        raw = phone.substring(1,10);
        phoneS = "+233" + raw;
        String body,address;

        //Toast.makeText(context, , Toast.LENGTH_LONG).show();

        Bundle extras = intent.getExtras();
        if(extras != null){
            Object[] smsExtra = (Object[])extras.get("pdus");
            SmsMessage sms = SmsMessage.createFromPdu((byte[])smsExtra[0]);
            body = sms.getMessageBody();
            address = sms.getOriginatingAddress();

            if(address.equals(phoneS)){
                //NotificationUtils.remindUserCosCharging(context,body);
                if(body.contains(":")){
                    String[] msgPart = body.split(":");
                    msg = msgPart[0];
                    if(msg.contains("sF")){
                        session.setvSrc("GRID ON");
                        NotificationUtils.createNotif(context,
                                "Currently running on GRID");
                    }
                    if(msg.contains("sO")){
                        session.setvSrc("SOLAR ON");
                        NotificationUtils.createNotif(context,
                                "Currently running on SOLAR");
                    }
                }
            }
        }
    }
}
