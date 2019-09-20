package com.gidtech.android.mynavdraw.fragment;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gidtech.android.mynavdraw.R;
import com.gidtech.android.mynavdraw.activity.MainActivity;
import com.gidtech.android.mynavdraw.helper.Contract;
import com.gidtech.android.mynavdraw.helper.DateUtils;
//import com.gidtech.android.mynavdraw.helper.FetchWeatherTask;
import com.gidtech.android.mynavdraw.helper.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by gid on 3/24/2017.
 */

public class WeatherFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor> {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public boolean sInitialized;

    private TextView date,weather_description,high_temperature,
            low_temperature,humidityTv,pressureTv,
            wind_measurementTv,humidityLabel,pressureLabel,windLabel;
    private ImageView weather_icon;
    private static final String FORECAST_SHARE_HASHTAG = " #GidWeather";

    private Context mContext;

    public static final String[] MAIN_FORECAST_PROJECTION = {
            Contract.WeatherEntry.COLUMN_DATE,
            Contract.WeatherEntry.COLUMN_MAX_TEMP,
            Contract.WeatherEntry.COLUMN_MIN_TEMP,
            Contract.WeatherEntry.COLUMN_HUMIDITY,
            Contract.WeatherEntry.COLUMN_PRESSURE,
            Contract.WeatherEntry.COLUMN_WIND_SPEED,
            Contract.WeatherEntry.COLUMN_DEGREES,
            Contract.WeatherEntry.COLUMN_WEATHER_ID,
    };

    public static final int INDEX_WEATHER_DATE = 0;
    public static final int INDEX_WEATHER_MAX_TEMP = 1;
    public static final int INDEX_WEATHER_MIN_TEMP = 2;
    public static final int INDEX_WEATHER_HUMIDITY = 3;
    public static final int INDEX_WEATHER_PRESSURE = 4;
    public static final int INDEX_WEATHER_WIND_SPEED = 5;
    public static final int INDEX_WEATHER_DEGREES = 6;
    public static final int INDEX_WEATHER_CONDITION_ID = 7;

    private static final int ID_WEATHER_LOADER = 129;

    private String mForecastSummary;
    private Uri mUri;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public WeatherFragment() {
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
    public static WeatherFragment newInstance(String param1, String param2) {
        WeatherFragment fragment = new WeatherFragment();
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

        mContext = getContext();
        View view = inflater.inflate(R.layout.fragment_weather, container, false);

        date = (TextView)view.findViewById(R.id.date);
        weather_description =(TextView)view.findViewById(R.id.weather_description);
        high_temperature = (TextView)view.findViewById(R.id.high_temperature);
        low_temperature = (TextView)view.findViewById(R.id.low_temperature);
        humidityTv = (TextView)view.findViewById(R.id.humidity);
        pressureTv = (TextView)view.findViewById(R.id.pressure);
        wind_measurementTv = (TextView)view.findViewById(R.id.wind_measurement);
        weather_icon = (ImageView)view.findViewById(R.id.weather_icon);
        humidityLabel = (TextView)view.findViewById(R.id.humidity_label);
        pressureLabel = (TextView)view.findViewById(R.id.pressure_label);
        windLabel = (TextView)view.findViewById(R.id.wind_label);

        long todayMillis = DateUtils.getNormalizedUtcDateForToday();

        mUri = Contract.WeatherEntry.buildWeatherUriWithDate(todayMillis);
        //mUri = getIntent().getData();
        if (mUri == null) throw new NullPointerException("URI cannot be null");

        getLoaderManager().initLoader(ID_WEATHER_LOADER, null, this);
        initialize();
        return view;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {


        switch (loaderId) {

            case ID_WEATHER_LOADER:
                return new CursorLoader(
                        getActivity(),
                        mUri,
                        MAIN_FORECAST_PROJECTION,
                        null,
                        null,
                        null);

            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        boolean cursorHasValidData = false;
        if (data != null && data.moveToFirst()) {
            /* We have valid data, continue on to bind the data to the UI */
            cursorHasValidData = true;
        }

        if (!cursorHasValidData) {
            /* No data to display, simply return and do nothing */
            return;
        }


         //Weather Icon
        /* Read weather condition ID from the cursor (ID provided by Open Weather Map) */
        int weatherId = data.getInt(INDEX_WEATHER_CONDITION_ID);
        /* Use our utility method to determine the resource ID for the proper art */
        int weatherImageId = Utils.getLargeArtResourceIdForWeatherCondition(weatherId);

        /* Set the resource ID on the icon to display the art */
        weather_icon.setImageResource(weatherImageId);

         //Weather Date *

        long localDateMidnightGmt = data.getLong(INDEX_WEATHER_DATE);
        String dateText = DateUtils.getFriendlyDateString(getActivity(), localDateMidnightGmt, true);

        date.setText(dateText);


        //Weather Description *

        /* Use the weatherId to obtain the proper description */
        String description = Utils.getStringForWeatherCondition(getActivity(), weatherId);

        /* Create the accessibility (a11y) String from the weather description */
        String descriptionA11y = getString(R.string.a11y_forecast, description);

        /* Set the text and content description (for accessibility purposes) */
        weather_description.setText(description);
        weather_description.setContentDescription(descriptionA11y);

        /* Set the content description on the weather image (for accessibility purposes) */
        weather_icon.setContentDescription(descriptionA11y);

         //High (max) temperature *
        /* Read high temperature from the cursor (in degrees celsius) */
        double highInCelsius = data.getDouble(INDEX_WEATHER_MAX_TEMP);
        /*
         * If the user's preference for weather is fahrenheit, formatTemperature will convert
         * the temperature. This method will also append either °C or °F to the temperature
         * String.
         */
        String highString = Utils.formatTemperature(getActivity(), highInCelsius);

        /* Create the accessibility (a11y) String from the weather description */
        String highA11y = getString(R.string.a11y_high_temp, highString);

        /* Set the text and content description (for accessibility purposes) */
        high_temperature.setText(highString);
        high_temperature.setContentDescription(highA11y);

         //Low (min) temperature *

        /* Read low temperature from the cursor (in degrees celsius) */
        double lowInCelsius = data.getDouble(INDEX_WEATHER_MIN_TEMP);
        /*
         * If the user's preference for weather is fahrenheit, formatTemperature will convert
         * the temperature. This method will also append either °C or °F to the temperature
         * String.
         */
        String lowString = Utils.formatTemperature(getActivity(), lowInCelsius);

        String lowA11y = getString(R.string.a11y_low_temp, lowString);

        /* Set the text and content description (for accessibility purposes) */
        low_temperature.setText(lowString);
        low_temperature.setContentDescription(lowA11y);

        //Humidity

        /* Read humidity from the cursor */
        float humidity = data.getFloat(INDEX_WEATHER_HUMIDITY);
        String humidityString = getString(R.string.format_humidity, humidity);

        String humidityA11y = getString(R.string.a11y_humidity, humidityString);

        /* Set the text and content description (for accessibility purposes) */
        humidityTv.setText(humidityString);
        humidityTv.setContentDescription(humidityA11y);

        humidityLabel.setContentDescription(humidityA11y);

        //Wind speed and direction

        /* Read wind speed (in MPH) and direction (in compass degrees) from the cursor  */
        float windSpeed = data.getFloat(INDEX_WEATHER_WIND_SPEED);
        float windDirection = data.getFloat(INDEX_WEATHER_DEGREES);
        String windString = Utils.getFormattedWind(getActivity(), windSpeed, windDirection);

        String windA11y = getString(R.string.a11y_wind, windString);

        /* Set the text and content description (for accessibility purposes) */
        wind_measurementTv.setText(windString);
        wind_measurementTv.setContentDescription(windA11y);

        windLabel.setContentDescription(windA11y);

        //Pressure

        /* Read pressure from the cursor */
        float pressure = data.getFloat(INDEX_WEATHER_PRESSURE);
        String pressureString = getString(R.string.format_pressure, pressure);

        String pressureA11y = getString(R.string.a11y_pressure, pressureString);

        /* Set the text and content description (for accessibility purposes) */
        pressureTv.setText(pressureString);
        pressureTv.setContentDescription(pressureA11y);

        pressureLabel.setContentDescription(pressureA11y);

        /* Store the forecast summary String in our forecast summary field to share later */
        mForecastSummary = String.format("%s - %s - %s/%s",
                dateText, description, highString, lowString);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    public class FetchWeatherTask extends AsyncTask<String,Void,String[]> {
        private final String LOG_TAG = FetchWeatherTask.class.getSimpleName();

        /*private String getReadableDateString(long time){
            //Cos the api returns a unix timestamp
            //Date date = new Date(time * 1000);
            SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("EEE, MMM dd");
            return shortenedDateFormat.format(time);
        }*/

        private String formatHighLows(double high,double low){
            long roundedHigh = Math.round(high);
            long roundedLow = Math.round(low);

            String highLowStr = roundedHigh + "/" + roundedLow;
            return highLowStr;

        }



        private String[]  getWeatherDataFromJson(String forecastJsonStr, int numDays)
            throws JSONException {
            //Names of JSON objects that needs to be extracted.
            final String OWM_LIST = "list";
            final String OWM_WEATHER = "weather";
            final String OWM_TEMPERATURE = "temp";
            final String OWM_MAX = "max";
            final String OWM_MIN = "min";
            final String OWM_WEATHER_ID = "id";
            final String OWM_DESCRIPTION = "main";
            final String OWM_PRESSURE = "pressure";
            final String OWM_HUMIDITY = "humidity";
            final String OWM_WINDSPEED = "speed";
            final String OWM_WIND_DIRECTION = "deg";

            JSONObject forecastJson = new JSONObject(forecastJsonStr);
            JSONArray weatherArray = forecastJson.getJSONArray(OWM_LIST);

            ContentValues[] weatherContentValues = new ContentValues[weatherArray.length()];
            //Time dayTime = new Time();
            //dayTime.setToNow();

            //Start at the day returned by local time
            //int julianStartDay = Time.getJulianDay(System.currentTimeMillis(),dayTime.gmtoff);

            //working in UTC
            //dayTime = new Time();

            long normalizedUtcStartDay = DateUtils.getNormalizedUtcDateForToday();

            String[] resultStrs = new String[numDays];
            for(int i=0;i < weatherArray.length();i++){
                //String day;
                long dateTimeMillis;
                int weatherId;
                //String highAndLow;
                double pressure;
                int humidity;
                double windSpeed;
                double windDirection;

                //Get JSON Object representing day
                JSONObject dayForecast = weatherArray.getJSONObject(i);

                //date/time is returned as long
                //final long dateTime;
                //dateTime = dayTime.setJulianDay(julianStartDay+i);
                //day = getReadableDateString(dateTime);
                //day = WeatherDateUtils.getFriendlyDateString(mContext,dateTime,false);

                dateTimeMillis = normalizedUtcStartDay + DateUtils.DAY_IN_MILLIS * i;

                pressure = dayForecast.getDouble(OWM_PRESSURE);
                humidity = dayForecast.getInt(OWM_HUMIDITY);
                windSpeed = dayForecast.getDouble(OWM_WINDSPEED);
                windDirection = dayForecast.getDouble(OWM_WIND_DIRECTION);

                //
                JSONObject weatherObject = dayForecast.getJSONArray(OWM_WEATHER).getJSONObject(0);
                weatherId = weatherObject.getInt(OWM_WEATHER_ID);
                String description = weatherObject.getString(OWM_DESCRIPTION);

                //
                JSONObject temperatureObject = dayForecast.getJSONObject(OWM_TEMPERATURE);
                double high = temperatureObject.getDouble(OWM_MAX);
                double low = temperatureObject.getDouble(OWM_MIN);

                //highAndLow = formatHighLows(high,low);
                resultStrs[i] = dateTimeMillis + " - "+ description;// + " - "+ highAndLow;

                ContentValues weatherValues = new ContentValues();
                weatherValues.put(Contract.WeatherEntry.COLUMN_DATE, dateTimeMillis);
                weatherValues.put(Contract.WeatherEntry.COLUMN_HUMIDITY, humidity);
                weatherValues.put(Contract.WeatherEntry.COLUMN_PRESSURE, pressure);
                weatherValues.put(Contract.WeatherEntry.COLUMN_WIND_SPEED, windSpeed);
                weatherValues.put(Contract.WeatherEntry.COLUMN_DEGREES, windDirection);
                weatherValues.put(Contract.WeatherEntry.COLUMN_MAX_TEMP, high);
                weatherValues.put(Contract.WeatherEntry.COLUMN_MIN_TEMP, low);
                weatherValues.put(Contract.WeatherEntry.COLUMN_WEATHER_ID, weatherId);

                weatherContentValues[i] = weatherValues;

                if (weatherContentValues != null && weatherContentValues.length != 0) {
                 //Get a handle on the ContentResolver to delete and insert data
                    ContentResolver gidContentResolver = getActivity().getApplicationContext().getContentResolver();

                //Delete old weather data because we don't need to keep multiple days' data
                    gidContentResolver.delete(
                            Contract.WeatherEntry.CONTENT_URI,
                            null,
                            null);

                //Insert our new weather data into Sunshine's ContentProvider
                    gidContentResolver.bulkInsert(
                            Contract.WeatherEntry.CONTENT_URI,
                            weatherContentValues);
                }
            }

            for (String s : resultStrs){
                Log.v(LOG_TAG,"Forecast entry: "+s);
            }
            return resultStrs;
            //return weatherContentValues;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String[] result) {
            //showWeatherDataView();
            //super.onPostExecute();
        }

        @Override
        protected String[] doInBackground(String... params) {

            //showLoading();
            if(params.length == 0){
                return null;
            }
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            //Will contain the raw Json response as string
            String forecastJsonStr = null;

            String format = "json";
            String units = "metric";
            String key = "ea623ef65bf1a0d1249f1d3d4d8830dd";
            int numDays = 7;


            try{
                //Construct the URL for the OpenWeather Query
                final String FORECAST_BASE_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?";
                final String QUERY_PARAM = "q";
                final String FORMAT_PARAM = "mode";
                final String UNIT_PARAM = "units";
                final String DAYS_PARAM = "cnt";
                final String APIKEY = "appid";

                Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                        .appendQueryParameter(QUERY_PARAM,params[0])
                        .appendQueryParameter(FORMAT_PARAM,format)
                        .appendQueryParameter(UNIT_PARAM,units)
                        .appendQueryParameter(DAYS_PARAM,Integer.toString(numDays))
                        .appendQueryParameter(APIKEY,key)
                        .build();
                URL url = new URL(builtUri.toString());
                Log.v(LOG_TAG,"Built URI"+builtUri.toString());

                //Create URL request
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                //Read input stream to a string
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if(inputStream ==  null){
                    //Nothing to do
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while((line = reader.readLine()) != null){
                    //Since
                    buffer.append(line+"\n");
                }

                if(buffer.length() == 0) {
                    return null;
                }
                forecastJsonStr = buffer.toString();

                // Log.v(LOG_TAG,"Forecast JSON String:"+forecastJsonStr);
            }catch (IOException e){
                Log.e(LOG_TAG,"Error ",e);
                return null;
            }finally{
                if(urlConnection != null){
                    urlConnection.disconnect();
                }
                if(reader != null){
                    try{
                        reader.close();
                    }catch (final IOException e){
                        Log.e(LOG_TAG,"Error closing stream",e);
                    }
                }
            }
            try{
                return getWeatherDataFromJson(forecastJsonStr,numDays);
            }catch(JSONException e){
                Log.e(LOG_TAG,e.getMessage(),e);
                e.printStackTrace();
            }
            return null;
        }
    }

    public void syncWeather(){
        FetchWeatherTask weatherTask = new FetchWeatherTask();
        weatherTask.execute("Accra");
    }

    public void initialize(){
        if(sInitialized) return;
        sInitialized = true;

        Thread checkForEmpty = new Thread(new Runnable() {
            @Override
            public void run() {

                /* URI for every row of weather data in our weather table*/
                Uri forecastQueryUri = Contract.WeatherEntry.CONTENT_URI;
                String[] projectionColumns = {Contract.WeatherEntry._ID};
                String selectionStatement = Contract.WeatherEntry
                        .getSqlSelectForTodayOnwards();

                /* Here, we perform the query to check to see if we have any weather data */
                Cursor cursor = mContext.getContentResolver().query(
                        forecastQueryUri,
                        projectionColumns,
                        selectionStatement,
                        null,
                        null);

                if (null == cursor || cursor.getCount() == 0) {
                    syncWeather();
                }

                /* Close the Cursor to avoid memory leaks! */
                cursor.close();
            }
        });

        /* Finally, once the thread is prepared, fire it off to perform our checks. */
        checkForEmpty.start();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.action_refresh){
            FetchWeatherTask weatherTask = new FetchWeatherTask();
            weatherTask.execute("Accra");
            return true;
        }

        if(id == R.id.action_share){
            Intent shareIntent = createShareForecastIntent();
            startActivity(shareIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private Intent createShareForecastIntent(){
        Intent shareIntent = ShareCompat.IntentBuilder.from(this.getActivity())
                .setType("text/plain")
                .setText(mForecastSummary + FORECAST_SHARE_HASHTAG)
                .getIntent();
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        return shareIntent;
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
}
