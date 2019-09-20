package com.gidtech.android.mynavdraw.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gidtech.android.mynavdraw.R;
import com.gidtech.android.mynavdraw.helper.SessionManager;

/**
 * Created by gid on 3/17/2017.
 */

public class RegisterActivity extends AppCompatActivity {
    TextView introTxt;
    EditText regNum;
    Button regBtn;
    String phoneNo;
    SessionManager session;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_register);

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

        //Session Manager
        session = new SessionManager(getApplicationContext());

        //Intro text,Registration number ,registration btn
        introTxt = (TextView)findViewById(R.id.textView);
        regNum = (EditText)findViewById(R.id.regNum);
        regBtn = (Button)findViewById(R.id.regBtn);
    }

    public void regMethod(View view) {
        //get phone number
        phoneNo = regNum.getText().toString();

        //check if phone number is valid
        if(phoneNo.trim().length() == 10){
            //start registration session
            session.createRegisterSession(phoneNo);
            session.setPhone_Reg(phoneNo);

            SmsManager sms_manager = SmsManager.getDefault();
            sms_manager.sendTextMessage(phoneNo, null, "CS", null, null);
            Toast.makeText(getApplicationContext(), "SMS SENT", Toast.LENGTH_LONG).show();

            //starting mainactivity
            Intent i = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(i);
            finish();
        }else {
            //clear textfield
            regNum.setText("");
            Toast.makeText(getApplicationContext(),"NUMBER INVALID.ENTER AGAIN!",Toast.LENGTH_LONG).show();
        }
        /**SharedPreferences sharedPreferences = getSharedPreferences("regInfo", Context.MODE_PRIVATE);
         SharedPreferences.Editor editor = sharedPreferences.edit();
         editor.putString("phoneReg",userInfo);
         editor.apply();

         regNum.setText("");
         Toast.makeText(getApplicationContext(),"Registration Successful",Toast.LENGTH_LONG).show();
         Intent i = new Intent(this, Main2Activity.class);
         startActivity(i);
         finish();**/
    }

}
