package com.gidtech.android.mynavdraw.fragment;

import android.Manifest;
import android.app.Activity;
//import android.app.LoaderManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsMessage;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.LoaderManager;

import com.gidtech.android.mynavdraw.R;
import com.gidtech.android.mynavdraw.activity.MainActivity;
import com.gidtech.android.mynavdraw.helper.SMSData;
import com.gidtech.android.mynavdraw.helper.SessionManager;

import org.w3c.dom.Text;

import java.util.HashMap;

import static com.gidtech.android.mynavdraw.R.id.energyV;
//import static com.gidtech.android.mynavdraw.R.mipmap.ic_launcher1;

/**
 * Created by gid on 3/17/2017.
 */

public class HomeFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private static final int MY_PERMISSIONS_REQUEST_READ_SMS = 0;
    SessionManager session ;
    //session class instance
    //session = new SessionManager(getActivity());

    //get data from session
    //HashMap<String,String> user = session.getRegInfo();

    //phone number
    //String phone = user.get(SessionManager.phone_Reg);
    String phone,vSource;
    String phoneS;

    TextView date,solarText,solarVal,gridText,gridVal,source;
    TextView powerText,powerV,energyText,energyV;
    ImageView img_src;

    BroadcastReceiver receiver = null;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home1, container, false);

        session = new SessionManager(getActivity());
        //get data from session
        //HashMap<String,String> user = session.getRegInfo();

        //phone number
        //phone = user.get(SessionManager.phone_Reg);
        phone = session.getPhone_Reg();

        //date = (TextView)view.findViewById(R.id.home_date);
        solarText = (TextView)view.findViewById(R.id.solarV);
        solarVal = (TextView)view.findViewById(R.id.solarVV);
        gridText = (TextView)view.findViewById(R.id.gridV);
        gridVal = (TextView)view.findViewById(R.id.gridVV);
        source = (TextView)view.findViewById(R.id.source);
        powerText = (TextView)view.findViewById(R.id.power);
        powerV = (TextView)view.findViewById(R.id.powerV);
        energyText = (TextView)view.findViewById(R.id.energy);
        energyV = (TextView)view.findViewById(R.id.energyV);
        img_src = (ImageView)view.findViewById(R.id.img_src);

        //Context mContext = getActivity().getApplicationContext();

        /*vSource = session.getvSrc();
        source.setText(vSource);
        if(vSource.contains("GRID ON")){
            img_src.setImageResource(R.drawable.grid_src);
            session.setSolarOn(true);
        }
        if(vSource.contains("SOLAR ON")){
            img_src.setImageResource(R.drawable.solar);
            session.setSolarOn(false);
        }*/
        String raw = phone.substring(1,10);
        phoneS = "+233"+raw;
        //call contentResolver for sms
        Uri uri = Uri.parse("content://sms/inbox");
        String[] reCol = new String[]{"_id","address","body"};
        Cursor cc = getActivity().getApplicationContext().getContentResolver().query(uri,
                reCol,
                "address=?",
                new String[]{phoneS},
                null);
        //startManagingCursor(cc);
        //cc.setNotificationUri(getContext().getContentResolver(),uri);

        getSMS(cc);
        //cc.close();

        IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //Toast.makeText(context,"CD tin hmaj",Toast.LENGTH_LONG).show();
                //this.getSMS();
                //(getParentFragment().getActivity()context.getApplicationContext()).getSms();
                //(getActivity()context.getApplicationContext())


                //((HomeFragment.this))context.getApplicationContext()).getSMS();
                //Activity act = (HomeFragment.this).getActivity();
                //context.getApplicationContext()).getSms();

            }
        };

        getActivity().registerReceiver(receiver,filter);

        getLoaderManager().initLoader(0,null,this);


        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void getSMS(Cursor c){
        //phone number
        //String phone = user.get(SessionManager.phone_Reg);

        //read sms permission
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED){

            if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.READ_SMS)){

            }else{
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_SMS},
                        MY_PERMISSIONS_REQUEST_READ_SMS);
            }
        }

        //call contentResolver for sms
        Uri uri = Uri.parse("content://sms/inbox");
        String[] reCol = new String[]{"_id","address","body"};
        c = getActivity().getApplicationContext().getContentResolver().query(uri,reCol,"address=?",new String[]{phoneS},null);
        //startManagingCursor(c);

        //Read the sms data and store it in the list
        if(c.moveToFirst()){
            for(int i=0; i < c.getCount();i++){
                SMSData sms = new SMSData();
                sms.setBody(c.getString(c.getColumnIndexOrThrow("body")).toString());
                sms.setNumber(c.getString(c.getColumnIndexOrThrow("address")).toString());
                if(sms.getBody().contains(":")){
                    //smsList.add(sms);
                    String [] msgPart = sms.getBody().split(":");
                    String src = msgPart[0];
                    if(src.contains("sO")){
                        source.setText(R.string.solar_on);
                        img_src.setImageResource(R.drawable.solar);
                        session.setSolarOn(true);
                    }else if(src.contains("sF"))
                    {
                        source.setText(R.string.grid_on);
                        img_src.setImageResource(R.drawable.grid_src);
                        session.setSolarOn(false);
                    }
                    String sVt = msgPart[1];
                    solarVal.setText(sVt+"V");
                    String gVt = msgPart[2];
                    gridVal.setText(gVt+"V");
                    String poW = msgPart[3];
                    powerV.setText(poW+"kW");
                    String enG = msgPart[4];
                    energyV.setText(enG+"kWh");
                }else{


                    c.moveToNext();}

                break;
            }
        }
        c.close();
    }

    public void onDestroy(){
        super.onDestroy();
        //getActivity().unregisterReceiver(receiver);
    }

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //Toast.makeText(getContext(),phoneS, Toast.LENGTH_LONG).show();
        return new android.support.v4.content.CursorLoader(getActivity(),
                Uri.parse("content://sms/inbox"),
                new String[]{"_id","address","body"},
                "address=?",
                new String[]{phoneS},
                null);
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        getSMS(data);
        //data.close();

    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {

    }

}
