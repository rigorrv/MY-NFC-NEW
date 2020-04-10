package com.jetruby.nfc.example;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jetruby.nfc.example.nfc.OutcomingNfcManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class SenderActivity extends AppCompatActivity implements OutcomingNfcManager.NfcActivity {

    //GPS Location
    private static final int REQUEST_LOCATION = 1;
    public static LocationManager locationManager;
    double latti;
    double longi;
    public static String lattitude, longitude;

    //Optimized pictures for  app loading from another Class
    private MyApp app;
    private int bgid = R.drawable.pink_line; // id of the background drawable
    private int layoutid = R.id.layout1; // id of the activity layout
    private LinearLayout layout; // the layout of the activity


    private TextView tvOutcomingMessage;
    private EditText etOutcomingMessage;
    private Button btnSetOutcomingMessage;
    private  Button backBtn;

    private NfcAdapter nfcAdapter;
    private OutcomingNfcManager outcomingNfccallback;
    ImageView img;

    //Base64
    String image64;

    //ListView
    Context actividad = this;
    Subject subject;
    ListView listView;
    ListAdapter adapter;
    List<Subject> subjectList;
    TextView nameInfo, lastInfo;



    //NFC
    String mesaggeInfo;
    String outMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sender);
        backBtn = findViewById(R.id.backBtn);
        img = findViewById(R.id.img);
        nameInfo=findViewById(R.id.nameInfo);
        lastInfo = findViewById(R.id.lastInfo);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SenderActivity.this,MainActivity.class));
                finish();
            }
        });

        app = (MyApp)getApplication();

        //pink line top
        layout = (LinearLayout) findViewById(layoutid);
        app.setBackground(layout, bgid); // PINK LINE TOP

        if (!isNfcSupported()) {
            Toast.makeText(this, "Nfc is not supported on this device", Toast.LENGTH_SHORT).show();
            finish();
        }
        if (!nfcAdapter.isEnabled()) {
            Toast.makeText(this, "NFC disabled on this device. Turn on to proceed", Toast.LENGTH_SHORT).show();
        }

        initViews();
        fullScreenWindo();


        // encapsulate sending logic in a separate class
        this.outcomingNfccallback = new OutcomingNfcManager(this);
        this.nfcAdapter.setOnNdefPushCompleteCallback(outcomingNfccallback, this);
        this.nfcAdapter.setNdefPushMessageCallback(outcomingNfccallback, this);
        setOutGoingMessage();

    }

    private void initViews() {
        this.tvOutcomingMessage = findViewById(R.id.tv_out_message);
        this.etOutcomingMessage = findViewById(R.id.et_message);
        this.btnSetOutcomingMessage = findViewById(R.id.btn_set_out_message);
        this.btnSetOutcomingMessage.setOnClickListener((v) -> setOutGoingMessage());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
    }

    private boolean isNfcSupported() {
        this.nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        return this.nfcAdapter != null;
    }

    private void setOutGoingMessage() {
        mesaggeInfo = MainActivity.registerInfo.replace("&#10;","");
        outMessage = mesaggeInfo;
        //Log.d("replace", ""+mesaggeInfo);
        this.tvOutcomingMessage.setText(outMessage);
        loadInfo();
    }

    @Override
    public String getOutcomingMessage() {
        return this.tvOutcomingMessage.getText().toString();
    }

    @Override
    public void signalResult() {
        // this will be triggered when NFC message is sent to a device.
        // should be triggered on UI thread. We specify it explicitly
        // cause onNdefPushComplete is called from the Binder thread
        runOnUiThread(() ->
                Toast.makeText(SenderActivity.this, R.string.message_beaming_complete, Toast.LENGTH_SHORT).show());
    }

    //Fullscreen
    public void fullScreenWindo(){
        this.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN|
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY|
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION|
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        );
    }

    //Load info from SharedPreferences and put into ListView
    public void loadInfo(){


        try {
            JSONArray jsonArray = new JSONArray(outMessage);
            JSONObject jsonObject;

            subjectList = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                subject = new Subject();
                jsonObject = jsonArray.getJSONObject(i);

                String infoName = jsonObject.getString("name");
                String infoLast = jsonObject.getString("last");
                nameInfo.setText(infoName);
                lastInfo.setText(infoLast);

                image64 = jsonObject.getString("base");
                loadingBase64();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //loading Base64
    public void loadingBase64(){
        byte[] bytes = image64.getBytes();
        bytes = Base64.decode(image64, Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        img.setImageBitmap(decodedImage);
    }

    public void getmyCurrentLocation() {
        MainActivity.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!MainActivity.locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

        } else if (MainActivity.locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getLocation();
        }
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(SenderActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (SenderActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SenderActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location location2 = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            if (location != null) {
                latti = location.getLatitude();
                longi = location.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);
            }
        }
        Log.d("latitud", "" + lattitude);
        Log.d("longitud", "" + longitude);

    }
}
