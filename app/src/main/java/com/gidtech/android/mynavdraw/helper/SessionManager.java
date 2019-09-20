package com.gidtech.android.mynavdraw.helper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.gidtech.android.mynavdraw.activity.RegisterActivity;

import java.util.HashMap;

/**
 * Created by gid on 3/17/2017.
 */

public class SessionManager {
    //Shared Preferences
    SharedPreferences pref;

    //Editor for Shared preferences
    SharedPreferences.Editor editor;

    //Context
    Context _context;

    //Shared pref mode
    int PRIVATE_MODE = 0;

    //Sharedpref file name
    private static final String PREF_NAME = "sessionPref";

    //All Shared Preferences Keys
    private static final String IS_REGISTER = "IsRegistered";
    private static final String IS_SOLAR_ON = "solarOn";
    public  static final String phone_Reg = "phoneReg";
    public static final String V_SRC = "srcV";
    public static final String S_VOLT = "voltS";
    public static final String G_VOLT = "voltG";
    public static final String POW_V = "vPow";
    public static final String ENG_V = "vEng";

    //Constructor
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create register session
     */
    public void createRegisterSession(String Reg_phone){
        //Storing register value as true
        editor.putBoolean(IS_REGISTER,true);

        //Storing phone number in pref
        editor.putString(phone_Reg,Reg_phone);

        //commit changes
        editor.commit();
    }

    public void setPhone_Reg(String Reg_phone)
    {
        editor.putString(phone_Reg,Reg_phone);
        editor.commit();
    }

    public String getPhone_Reg()
    {
        return pref.getString(phone_Reg,"0549693420");
    }
    /**
     * Get stored session data
     */
    public HashMap<String,String> getRegInfo(){
        HashMap<String,String> user = new HashMap<String,String>();
        //user phone number
        user.put(phone_Reg,pref.getString(phone_Reg,"0549693420"));

        return user;
    }

    /**
     * Check registration method
     */
    public void checkRegister(){
        //Check registration status
        if(!this.isRegisterd()){
            //user is not logged in redirect
            Intent i = new Intent(_context,RegisterActivity.class);
            //Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            //Starting activity
            _context.startActivity(i);
        }
    }

    /**
     * Quick check for registered
     */
    public boolean isRegisterd(){
        return pref.getBoolean(IS_REGISTER,false);
    }

    public void setSolarOn(boolean kk ){
        editor.putBoolean(IS_SOLAR_ON,kk);
        editor.commit();
    }

    public boolean isSolarOn(){
        return pref.getBoolean(IS_SOLAR_ON,true);
    }

    public void setvSrc(String kk){
        editor.putString(V_SRC,kk);
        editor.commit();
    }

    public String getvSrc(){
        return pref.getString(V_SRC,"GRID ON");
    }

    public String getsVolt() {
        return pref.getString(S_VOLT,"");
    }

    public void setsVolt(String kk){
        editor.putString(S_VOLT,kk);
        editor.commit();
    }

    public void setgVolt(String kk){
        editor.putString(G_VOLT,kk);
        editor.commit();
    }

    public String getgVolt() {
        return pref.getString(G_VOLT,"");
    }

    public void setPowV(String kk){
        editor.putString(POW_V,kk);
        editor.commit();
    }

    public String getPowV() {
        return pref.getString(POW_V,"");
    }

    public void setEngV(String kk){
        editor.putString(ENG_V,kk);
        editor.commit();
    }

    public String getEngV() {
        return pref.getString(ENG_V,"");
    }


}
