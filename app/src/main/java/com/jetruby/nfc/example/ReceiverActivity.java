package com.jetruby.nfc.example;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.renderscript.Sampler;
import android.support.annotation.Nullable;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class ReceiverActivity extends AppCompatActivity {

    //GPS Location
    private static final int REQUEST_LOCATION = 1;
    public static LocationManager locationManager;
    double latti;
    double longi;
    public String lattitude, longitude;


    //Optimized pictures for  app loading from another Class
    private MyApp app;
    private int bgid = R.drawable.pink_line; // id of the background drawable
    private int layoutid = R.id.layout1; // id of the activity layout
    private LinearLayout layout; // the layout of the activity

    public static final String MIME_TEXT_PLAIN = "text/plain";

    private TextView tvIncomingMessage;
    private NfcAdapter nfcAdapter;
    String inMessage;

    //Layout
    ImageView img;

    //ListView
    Context actividad = this;
    Subject subject;
    ListView listView;
    ListAdapter adapter;
    List<Subject> subjectList;
    Button backBtn;

    //Base64
    String image64;

    //Form for loading information from user
    public static String registerInfo;
    String infoName, infoLast, infoEmail, infoPhone, infoAge, infoEducation, infoJoin, infoOut, infoKnowledge, infoGoals;
    EditText name, last, email, phone, age, education, join, out, software, goals;
    String nameTxt, lastTxt, emailTxt, phoneTxt, ageTxt, educationTxt, joinTxt, outTxt,softwareTxt, goalsTxt , image64Txt;
    TextView nameInfo, lastInfo;

    //sharedPreferences
    SharedPreferences SharedPreferencesContact;
    String MY_CONTACTS = "my_contacts";
    Set<String> getContactList;
    String CheckingName;
    String CheckingLast;
    String Contactname;
    String Contactlast;
    String Contactemail;
    String Contactphone;
    String Contactage;
    String Contacteducation;
    String Contactjoin;
    String Contactout;
    String Contactsoftware;
    String Contactgoals;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver);
        listView = findViewById(R.id.lisView);
        img = findViewById(R.id.img);
        nameInfo=findViewById(R.id.nameInfo);
        lastInfo = findViewById(R.id.lastInfo);
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ReceiverActivity.this,MainActivity.class));
                finish();
            }
        });


        app = (MyApp)getApplication();


        //sharedPreferences Saving Contacts
        SharedPreferencesContact = getSharedPreferences(MY_CONTACTS, Context.MODE_PRIVATE);// shared preferences for Contatcs
        getContactList = SharedPreferencesContact.getStringSet("myFields", new HashSet<String>());// shared preferences for Picture

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

        fullScreenWindo();
        initViews();
    }

    // need to check NfcAdapter for nullability. Null means no NFC support on the device
    private boolean isNfcSupported() {
        this.nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        return this.nfcAdapter != null;
    }

    private void initViews() {
        this.tvIncomingMessage = findViewById(R.id.tv_in_message);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // also reading NFC message from here in case this activity is already started in order
        // not to start another instance of this activity
        receiveMessageFromDevice(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // foreground dispatch should be enabled here, as onResume is the guaranteed place where app
        // is in the foreground
        enableForegroundDispatch(this, this.nfcAdapter);
        receiveMessageFromDevice(getIntent());
    }

    @Override
    protected void onPause() {
        super.onPause();
        disableForegroundDispatch(this, this.nfcAdapter);
    }

    private void receiveMessageFromDevice(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable[] parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

            NdefMessage inNdefMessage = (NdefMessage) parcelables[0];
            NdefRecord[] inNdefRecords = inNdefMessage.getRecords();
            NdefRecord ndefRecord_0 = inNdefRecords[0];

            inMessage = new String(ndefRecord_0.getPayload());
            this.tvIncomingMessage.setText(inMessage);
            //Log.d("getMesagge",""+inMessage);
            loadInfo();
        }
    }

    // Foreground dispatch holds the highest priority for capturing NFC intents
    // then go activities with these intent filters:
    // 1) ACTION_NDEF_DISCOVERED
    // 2) ACTION_TECH_DISCOVERED
    // 3) ACTION_TAG_DISCOVERED

    // always try to match the one with the highest priority, cause ACTION_TAG_DISCOVERED is the most
    // general case and might be intercepted by some other apps installed on your device as well

    // When several apps can match the same intent Android OS will bring up an app chooser dialog
    // which is undesirable, because user will most likely have to move his device from the tag or another
    // NFC device thus breaking a connection, as it's a short range

    public void enableForegroundDispatch(AppCompatActivity activity, NfcAdapter adapter) {

        // here we are setting up receiving activity for a foreground dispatch
        // thus if activity is already started it will take precedence over any other activity or app
        // with the same intent filters


        final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        //
        final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);

        IntentFilter[] filters = new IntentFilter[1];
        String[][] techList = new String[][]{};

        filters[0] = new IntentFilter();
        filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filters[0].addCategory(Intent.CATEGORY_DEFAULT);
        try {
            filters[0].addDataType(MIME_TEXT_PLAIN);
        } catch (IntentFilter.MalformedMimeTypeException ex) {
            throw new RuntimeException("Check your MIME type");
        }

        adapter.enableForegroundDispatch(activity, pendingIntent, filters, techList);
    }

    public void disableForegroundDispatch(final AppCompatActivity activity, NfcAdapter adapter) {
        adapter.disableForegroundDispatch(activity);
    }


    //Load info from SharedPreferences and put into ListView
    public void loadInfo(){


                try {
                    JSONArray jsonArray = new JSONArray(inMessage);
                    JSONObject jsonObject;

                    subjectList = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        subject = new Subject();
                        jsonObject = jsonArray.getJSONObject(i);
                        subject.subject_name = jsonObject.getString("name");
                        subject.subject_last = jsonObject.getString("last");
                        subject.subject_email = jsonObject.getString("email");
                        subject.subject_phone = jsonObject.getString("phone");
                        subject.subject_age = jsonObject.getString("age");
                        subject.subject_education = jsonObject.getString("education");
                        subject.subject_join = jsonObject.getString("join");
                        subject.subject_out = jsonObject.getString("out");
                        subject.subject_software = jsonObject.getString("software");
                        subject.subject_goals = jsonObject.getString("goals");
                        image64 = jsonObject.getString("base");//getting base 64 image
                        String infoName = jsonObject.getString("name");
                        String infoLast = jsonObject.getString("last");
                        nameInfo.setText(infoName);
                        lastInfo.setText(infoLast);

                        Contactname = jsonObject.getString("name");
                        Contactlast = jsonObject.getString("last");
                        Contactemail = jsonObject.getString("email");
                        Contactphone = jsonObject.getString("phone");
                        Contactage = jsonObject.getString("age");
                        Contacteducation = jsonObject.getString("education");
                        Contactjoin = jsonObject.getString("join");
                        Contactout = jsonObject.getString("out");
                        Contactsoftware = jsonObject.getString("software");
                        Contactgoals = jsonObject.getString("goals");

                        subjectList.add(subject);
                        adapter = new ListAdapter(subjectList, actividad);
                        listView.setAdapter(adapter);
                        loadingBase64();
                        checkingInfoContact();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
    }

    //Checking info From Shared Preferences
    public void checkingInfoContact(){
        Map<String, ?> prefsMap = SharedPreferencesContact.getAll();
        List<Sampler.Value> list = new ArrayList<Sampler.Value>((Collection<? extends Sampler.Value>) prefsMap.values());
        String clearContactString = list.toString().replace("[", "").replace("]","");
        //Log.d("clearContactString", ""+clearContactString);

        if (clearContactString == null) {
            //notthing
        }else{
            try {
                JSONArray jsonArray = new JSONArray("[" + clearContactString + "]");
                JSONObject jsonObject;

                subjectList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    subject = new Subject();
                    jsonObject = jsonArray.getJSONObject(i);
                    CheckingName = jsonObject.getString("name");
                    CheckingLast = jsonObject.getString("last");
                }

                //Saving new Contacs into Shared Preferences
                if(CheckingName == null) {// checking if the info is NULL and put the name in the string
                    String contact = "{" + "'name':" + "'" + Contactname + "'" + ", " + "'last':" + "'" + Contactlast + "'" +", " + "'email':" + "'" + Contactemail + "'" +", " + "'phone':" + "'" + Contactphone + "'" +", " + "'age':" + "'" + Contactage + "'" +", " + "'education':" + "'" + Contacteducation + "'" +", " + "'join':" + "'" + Contactjoin + "'" +", " + "'out':" + "'" + Contactout + "'" +", " + "'software':" + "'" + Contactsoftware + "'" +", " + "'goals':" + "'" + Contactgoals + "'" +", " + "'lat':" + "'" + MainActivity.lattitudeCont + "'" +", " + "'lng':" + "'" + MainActivity.longitudeCont + "'" +", " + "'base':" + "'" + image64 + "'" + "}";
                    getContactList.add(contact);
                    SharedPreferences.Editor editorContact = SharedPreferencesContact.edit();
                    editorContact.putString(Contactname, String.valueOf(getContactList));
                    editorContact.apply();
                }else {
                if (CheckingName.equals(Contactname)) {// checking if the info is Contains the same name and put the Latsname in the string
                    //Log.d("Contacto", "existe uno parecido");
                    String contact = "{" + "'name':" + "'" + Contactname + "'" + ", " + "'last':" + "'" + Contactlast + "'" +", " + "'email':" + "'" + Contactemail + "'" +", " + "'phone':" + "'" + Contactphone + "'" +", " + "'age':" + "'" + Contactage + "'" +", " + "'education':" + "'" + Contacteducation + "'" +", " + "'join':" + "'" + Contactjoin + "'" +", " + "'out':" + "'" + Contactout + "'" +", " + "'software':" + "'" + Contactsoftware + "'" +", " + "'goals':" + "'" + Contactgoals + "'" +", " + "'lat':" + "'" + MainActivity.lattitudeCont + "'" +", " + "'lng':" + "'" + MainActivity.longitudeCont + "'" +", " + "'base':" + "'" + image64 + "'" + "}";
                    getContactList.add(contact);
                    SharedPreferences.Editor editorContact = SharedPreferencesContact.edit();
                    editorContact.putString(Contactlast, String.valueOf(getContactList));
                    editorContact.apply();
                } else {// checking if the Name doesnt't exist in the list and put in the list
                    String contact = "{" + "'name':" + "'" + Contactname + "'" + ", " + "'last':" + "'" + Contactlast + "'" +", " + "'email':" + "'" + Contactemail + "'" +", " + "'phone':" + "'" + Contactphone + "'" +", " + "'age':" + "'" + Contactage + "'" +", " + "'education':" + "'" + Contacteducation + "'" +", " + "'join':" + "'" + Contactjoin + "'" +", " + "'out':" + "'" + Contactout + "'" +", " + "'software':" + "'" + Contactsoftware + "'" +", " + "'goals':" + "'" + Contactgoals + "'" +", " + "'lat':" + "'" + MainActivity.lattitudeCont + "'" +", " + "'lng':" + "'" + MainActivity.longitudeCont + "'" +", " + "'base':" + "'" + image64 + "'" + "}";
                    getContactList.add(contact);
                    SharedPreferences.Editor editorContact = SharedPreferencesContact.edit();
                    editorContact.putString(Contactname, String.valueOf(getContactList));
                    editorContact.apply();
                }
            }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }


    //loading Base64
    public void loadingBase64(){
        byte[] bytes = image64.getBytes();
        bytes = Base64.decode(image64, Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        img.setImageBitmap(decodedImage);
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
}
