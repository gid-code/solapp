package com.gidtech.android.mynavdraw.activity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.VideoView;

import com.gidtech.android.mynavdraw.R;
import com.gidtech.android.mynavdraw.helper.SessionManager;

import java.util.HashMap;

import static android.support.v7.appcompat.R.id.time;
import static java.security.AccessController.getContext;

/**
 * Created by gid on 3/21/2017.
 */

public class fabActivity extends Activity {
    public RadioGroup radioGroup;
    public RadioButton sBtn;
    public RadioButton gBtn;
    public String phone;
    public String msg;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fab_layout);

        radioGroup = (RadioGroup)findViewById(R.id.radioG);
        sBtn = (RadioButton)findViewById(R.id.solarBtn);
        gBtn = (RadioButton)findViewById(R.id.gridBtn);

        //session class instance
        session = new SessionManager(getApplicationContext());

        //check user registration
        //session.checkRegister();

        //get data from session
        //HashMap<String,String> user = session.getRegInfo();

        //phone number
        //phone = user.get(SessionManager.phone_Reg);
        phone = session.getPhone_Reg();

        if(session.isSolarOn()){
            sBtn.setChecked(true);
            gBtn.setChecked(false);
        }else{
            sBtn.setChecked(false);
            gBtn.setChecked(true);
        }

        sBtn.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        onRadioButtonClicked(v);
                        //session.setSolarOn(true);
                    }
                }
        );

        gBtn.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        onRadioButtonClicked(v);
                        //session.setSolarOn(false);
                    }
                }
        );

    }

    public void onRadioButtonClicked(View v){
        boolean checked = ((RadioButton) v).isChecked();

        switch (v.getId()){
            case R.id.solarBtn:
                if (checked)
                    sendMsg("solar on");
                break;
            case R.id.gridBtn:
                if(checked)
                    sendMsg("grid on");
                break;
        }
    }

    public void sendMsg(String m) {
        String mssg = m;

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

        SmsManager sms_manager = SmsManager.getDefault();
        sms_manager.sendTextMessage(phone, null, mssg, null, null);
        Toast.makeText(getApplicationContext(), "SMS SENT", Toast.LENGTH_LONG).show();

    }
}
