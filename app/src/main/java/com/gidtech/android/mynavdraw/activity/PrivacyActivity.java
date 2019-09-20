package com.gidtech.android.mynavdraw.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gidtech.android.mynavdraw.R;
import com.gidtech.android.mynavdraw.helper.SessionManager;

import java.util.Random;

/**
 * Created by gid on 3/24/2017.
 */

public class PrivacyActivity extends Activity {


    public TextView tipsView;
    public Button next;
    public int i;
    public int j;
    public String[] ContentArray;

    SessionManager session;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tips_layout);
        session = new SessionManager(getApplicationContext());

        //setupSharedPreferences(;);

        tipsView = (TextView) findViewById(R.id.tipsTv);
        next = (Button)findViewById(R.id.nxt);
        ContentArray = new String[]{"Turn things off when you are not in the " +
        "room such as lights, TVs, entertainment " +
                "systems, and your computer and monitor",
                "Plug home electronics, such as TVs and " +
                "DVD players, into power strips; turn the " +
                "power strips off when the equipment " +
                "is not in use—TVs and DVDs in standby " +
                "mode still use several watts of power.","Buy energy efficient refrigerators. Let the " +
                "yellow label and stars be your guide. The more the stars, the more efficient the fridge. Efficient fridges consume less electricity",
        "Do not position the fridge at where there are direct sun rays, or near heaters such as stoves",
        "Avoid frequent opening of the fridge’s door since every opening introduces warm air into the fridge and the fridge will require more energy to overcome the warm air",
        "Use brighter colours for rooms since they do reflect light and ensure better illumination than dull colours which tend to absorb light.",
        "The (ballast) choke in the fluorescent light should be removed if the lamp is gone dead. The choke consumes up to 11 watt if it remains in the system.",
        "Turn off lights that are not in use","Turn off TV and video sets that are not being watched. Children especially are likely to leave a set on when called outside to play.",
        "Buy efficient air-conditioners. Look out for the yellow label and the black stars. The more the stars, the more efficient the application, no label no good!",
        "If you are going out for more than 30 minutes, the air conditioners should be turned off.","Avoid the use of incandescent lamps as they produce heat. The work of the fan will not be fully felt as the air in the room will be warmed by the incandescent lamp.",
        "Switch off the mains if you are not charging the mobile phone. A mobile phone charger plugged in but not charging consumes 0.048kWh/day which amount to 7.20kWh/year"};
        tipsView.setText(ContentArray[0]);

        next.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        onButtonClicked(v);
                        //session.setSolarOn(true);
                    }
                }
        );



    }

    public void onButtonClicked(View v){
        Random rand = new Random();
        i = rand.nextInt(9);


        tipsView.setText(ContentArray[i]);
    }

    /*private void setupSharedPreferences() {
        // Get all of the values from shared preferences to set it up
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        /*mVisualizerView.setShowBass(sharedPreferences.getBoolean(getString(R.string.pref_show_bass_key),
                getResources().getBoolean(R.bool.pref_show_bass_default)));
        mVisualizerView.setShowMid(sharedPreferences.getBoolean(getString(R.string.pref_show_mid_range_key),
                getResources().getBoolean(R.bool.pref_show_mid_range_default)));
        mVisualizerView.setShowTreble(sharedPreferences.getBoolean(getString(R.string.pref_show_treble_key),
                getResources().getBoolean(R.bool.pref_show_treble_default)));
        loadColorFromPreferences(sharedPreferences);
        loadSizeFromSharedPreferences(sharedPreferences);

        // Register the listener
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_phone_key))){
            String newNum = sharedPreferences.getString(getString(R.string.pref_phone_key),"0502233666");
            session.setPhone_Reg(newNum);
        }
    }*/
}
