package com.jetruby.nfc.example;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.renderscript.Sampler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;


public class MainActivity extends Activity {

    //GPS Location
    private static final int REQUEST_LOCATION = 1;
    public static LocationManager locationManager;
    double latti;
    double longi;
    public static String lattitude, longitude;
    public static String lattitudeCont, longitudeCont;

    //Optimized pictures for  app loading from another Class
    private ImageView img, selectPic, editoBtn;
    private MyApp app;
    private int navid = R.id.imageView1; // id of the imageView
    private int editor = R.id.editorBtn; // id of the button
    private int pictureSelected = R.id.imageView2;
    private int navdid = R.drawable.pic; // id of the image drawable
    private int botonEditor = R.drawable.pincel_btn; //id of imagen editor drawable
    private int bgid = R.drawable.pink_line; // id of the background drawable
    private int layoutid = R.id.layout1; // id of the activity layout
    private LinearLayout layout; // the layout of the activity



    //Layouts
    RelativeLayout Welcome, Register, layerButtonMenu, contactInfoBox, menuVisible, warningContacts;
    Button registerBtn, sendEmail;
    LinearLayout checkInfo, listContacts, listSetting, settingsButton, contactListTXT, ClearContactS, pinkLineSettings;
    LinearLayout lista;
    Boolean checkInfoList = true;
    Button button1, PhoneCall, dontRemoveBTn, RemoveBTN, backBtn;
    TextView textShowList, contactoName, contactLast, settingsTxt, welcomeTXT, contactText, ClearContactSTxt;
    Button menuBtn;
    LinearLayout menuLayout;
    Boolean menu = false;
    Boolean showSettings = false;
    ImageView pictureContact;
    Boolean listViewCOntacts = false;
    LinearLayout [] buttonsSettings;
    Boolean inactiveButtons = false;

    //ListView Personal Information
    Context actividad = this;
    Subject subject;
    ListView listView;
    ListAdapter adapter;
    List<Subject> subjectList;


    //ListView Contact Information
    SubjectContact subjectContact;
    ListView listViewContact;
    ListContacts adapterContact;
    List<SubjectContact> subjectListContact;

    //ListView Contact Information inside Box
    ListView contactListView;
    InfoContact adapterContactIfon;

    //Form for loading information from user
    public static String registerInfo;
    String infoName, infoLast, infoEmail, infoPhone, infoAge, infoEducation, infoJoin, infoOut, infoKnowledge, infoGoals;
    EditText name, last, email, phone, age, education, join, out, software, goals;
    String nameTxt, lastTxt, emailTxt, phoneTxt, ageTxt, educationTxt, joinTxt, outTxt,softwareTxt, goalsTxt , image64Txt;
    TextView nameInfo, lastInfo;
    String listViewButton, callPhone, senEmail;
    int numberListViewBtn;
    String cityName;

    //code for loading picture from Gallery
    private static final int PICK_IMAGE_REQUEST = 0;
    private final String TAG = "Main Activity";
    private ImageView mImage;
    private Uri mImageUri;

    //sharedPreferences
    SharedPreferences SharedPreferencesInfo;
    SharedPreferences SharedMyPicture;
    SharedPreferences SharedMyPicture64;
    SharedPreferences SharedPreferencesContact;
    String MY_INFO = "my_info";
    String MY_PICTURE = "my_picture";
    String MY_PICTURE64 = "my_picture64";
    String MY_CONTACTS = "my_contacts";
    Set<String> getMyPicture64;
    Set<String> getMyPicture;
    Set<String> getListValue;
    Set<String> getContactList;
    Boolean informationFilled = false; // check if the Form is filled
    String mImageUriPic;
    String contactList;

    //Base64
    byte[] bytes;
    public static String picture;
    public static String imageString;
    public static String Base64Image;
    public static String checkingBase64;
    public static String newStringBase;
    String loadImage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        app = (MyApp)getApplication();
        menuBtn = findViewById(R.id.menuBtn);
        menuBtn.setVisibility(View.VISIBLE);
        menuLayout = findViewById(R.id.menuLayout);
        menuLayout.setVisibility(View.GONE);
        listViewContact = findViewById(R.id.listViewContact);
        listViewContact.setVisibility(View.VISIBLE);
        contactListView = findViewById(R.id.contactListView);

        //instance Layouts and butons inside layouts and Hide or Show Layouts
        Welcome = findViewById(R.id.Welcome);
        Welcome.setVisibility(View.GONE);
        Register = findViewById(R.id.Register);
        Register.setVisibility(View.GONE);
        registerBtn=findViewById(R.id.registerBtn);
        checkInfo = findViewById(R.id.checkInfo);
        listView = findViewById(R.id.lisView);
        lista = findViewById(R.id.lista);
        lista.setVisibility(View.GONE);
        button1 = (Button) findViewById(R.id.button1);
        textShowList =findViewById(R.id.textShowList);
        layerButtonMenu = findViewById(R.id.layerButtonMenu);
        layerButtonMenu.setVisibility(View.VISIBLE);
        contactInfoBox = findViewById(R.id.contactInfoBox);
        contactInfoBox.setVisibility(View.GONE);
        pictureContact = findViewById(R.id.pictureContact);
        PhoneCall = findViewById(R.id.PhoneCall);
        sendEmail = findViewById(R.id.sendEmail);
        contactoName = findViewById(R.id.contactoName);
        contactLast = findViewById(R.id.contactoLast);
        menuVisible = findViewById(R.id.menuVisible);
        menuVisible.setVisibility(View.VISIBLE);
        listContacts= findViewById(R.id.listContacts);
        listContacts.setVisibility(View.VISIBLE);
        listSetting=findViewById(R.id.listSetting);
        listSetting.setVisibility(View.GONE);
        settingsButton = findViewById(R.id.settingsButton);
        warningContacts = findViewById(R.id.warningContacts);
        warningContacts.setVisibility(View.GONE);
        ClearContactS = findViewById(R.id.ClearContactS);
        dontRemoveBTn = findViewById(R.id.dontRemoveBTn);
        RemoveBTN = findViewById(R.id.RemoveBTN);
        contactListTXT = findViewById(R.id.contactListTXT);
        backBtn =findViewById(R.id.backBtn);
        settingsTxt = findViewById(R.id.settingsTxt);
        pinkLineSettings = findViewById(R.id.pinkLineSettings);
        pinkLineSettings.setVisibility(View.GONE);
        welcomeTXT = findViewById(R.id.welcomeTXT);
        contactText = findViewById(R.id.contactText);
        contactText.setVisibility(View.GONE);
        ClearContactSTxt = findViewById(R.id.ClearContactSTxt);

        //CallMethod Fullscreen
        fullScreenWindo();

        //Instance EditText for create The form to put info into SharedFoleders
        nameInfo = findViewById(R.id.nameInfo);
        lastInfo = findViewById(R.id.lastInfo);
        name=findViewById(R.id.name);
        last=findViewById(R.id.last);
        email=findViewById(R.id.email);
        phone=findViewById(R.id.phone);
        age=findViewById(R.id.age);
        education=findViewById(R.id.education);
        join=findViewById(R.id.join);
        out=findViewById(R.id.out);
        software=findViewById(R.id.software);
        goals=findViewById(R.id.goals);

        //Loading Images and Background with code to optimece the app
        mImage = (ImageView) findViewById(R.id.imageView1);
        //User Picture
        img = (ImageView)findViewById(navid);
        app.setImage(img, navdid); // User Picture default

        //selectedPicture Register Layout
        selectPic = (ImageView) findViewById(pictureSelected);
        app.setImageProfile(selectPic, navdid); // Editor Button


        //boton editor
        editoBtn = (ImageView) findViewById(editor);
        app.setImageEdit(editoBtn, botonEditor); // Editor Button


        //pink line top
        layout = (LinearLayout) findViewById(layoutid);
        app.setBackground(layout, bgid); // PINK LINE TOP


        //sharedPreferences
        SharedPreferencesInfo = getSharedPreferences(MY_INFO, Context.MODE_PRIVATE); // shared preferences for Info
        SharedMyPicture = getSharedPreferences(MY_PICTURE, Context.MODE_PRIVATE);// shared preferences for Picture
        SharedMyPicture64 = getSharedPreferences(MY_PICTURE64, Context.MODE_PRIVATE);// shared preferences for Picture
        getListValue = SharedPreferencesInfo.getStringSet("myFields", new HashSet<String>()); // shared preferences for Info
        getMyPicture = SharedMyPicture.getStringSet("myFields", new HashSet<String>());// shared preferences for Picture
        getMyPicture64 = SharedMyPicture64.getStringSet("myFields", new HashSet<String>());// shared preferences for Picture
        checkingBase64 = SharedMyPicture64.getString("Json", null);

        contactInfoShared();


        //Check if my_picture64.xm exist in Shared Preferences if not Load Picture from Drawable
        File fileBase64Img = new File("/data/data/com.jetruby.nfc.example/shared_prefs/my_picture64.xml");
        String mImageUri = SharedMyPicture.getString("image", null);
        if(fileBase64Img.exists()){
            loadingBase64();
        } else {
            //app.setImageProfile(selectPic,pictureSelected);
            app.setImage(img, navdid); // free last image, and store new one
        }

        //Check if the files in Shared Preferences Exist if not Create it
        File fileXInfo = new File("/data/data/com.jetruby.nfc.example/shared_prefs/my_info.xml");
        if(fileXInfo.exists()){
            Welcome.setVisibility(View.VISIBLE); // if exist register Show me Welcome
            Register.setVisibility(View.GONE);// if exist register option GONE
            informationFilled =true;
        }else {
            Welcome.setVisibility(View.GONE); // if exist Welcome GONE
            Register.setVisibility(View.VISIBLE); // if exist register Show me Registration
            SharedPreferences.Editor editor = SharedPreferencesInfo.edit();
            editor.clear();
            editor.commit();
        }

        //Check if the files in Shared Preferences Exist if not Create it
        File fileContact = new File("/data/data/com.jetruby.nfc.example/shared_prefs/my_contacts.xml");
        if(fileContact.exists()){
            //nothing
        }else {
            SharedPreferences.Editor editor = SharedPreferencesContact.edit();
            editor.clear();
            editor.commit();
        }

            //BUTTONS
            //listView Contact Info
            listViewContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    listViewButton = String.valueOf(position);
                    contactInfoBox.setVisibility(View.VISIBLE);
                    numberListViewBtn = position;
                    loadContactInfo();
                    menuLayout.setVisibility(View.GONE);
                    backBtn.setVisibility(View.VISIBLE);
                    menuBtn.setVisibility(View.GONE);

                }
            });
            registerBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fullScreenWindo();
                    imageBase64();
                    informationFilled = true;
                    loadInfo();
                }
            });
            selectPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageSelect();
                }
            });
            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkingBase64 = SharedMyPicture64.getString("Json", null);
                    newStringBase = registerInfo.replace("&#10;", "");
                    startActivity(new Intent(v.getContext(), SenderActivity.class));
                    finish();
                }
            });
            checkInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fullScreenWindo();
                    if (checkInfoList) {
                        lista.setVisibility(View.VISIBLE);
                        textShowList.setText("Click here to Hide your info");
                    } else {
                        lista.setVisibility(View.GONE);
                        textShowList.setText("Click here to See your info");
                    }
                    checkInfoList = !checkInfoList;
                }
            });

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    fullScreenWindo();
                    if (checkInfoList) {
                        lista.setVisibility(View.VISIBLE);
                        textShowList.setText("Click here to Hide your info");
                    } else {
                        lista.setVisibility(View.GONE);
                        textShowList.setText("Click here to See your info");
                    }
                    checkInfoList = !checkInfoList;
                }
            });
            editoBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fullScreenWindo();
                    //Log.d("Base64Image",""+Base64Image);
                    //Check if my_picture64.xm exist in Shared Preferences if not Load Picture from Drawable
                    File fileBase64Img = new File("/data/data/com.jetruby.nfc.example/shared_prefs/my_picture64.xml");
                    if (fileBase64Img.exists()) {
                        loadingBase64();
                    } else {
                        app.setImage(img, navdid); // free last image, and store new one
                    }
                    Welcome.setVisibility(View.GONE); // if exist Welcome GONE
                    Register.setVisibility(View.VISIBLE); // if exist register Show me Registration
                    loadInfo();
                }
            });

            menuBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (inactiveButtons == false) {
                        menu = !menu;
                        showSettings = false;
                        fullScreenWindo();
                        checkInfoList = true;
                        textShowList.setText("Click here to See your info");
                        if (menu == true) {
                            loadContactList();
                            getmyCurrentLocation();
                            menuLayout.setVisibility(View.VISIBLE);
                            editoBtn.setVisibility(View.GONE);
                            Welcome.setVisibility(View.GONE);
                            menuBtn.setText("BACK");
                            welcomeTXT.setText("");
                            contactText.setVisibility(View.VISIBLE);
                            lista.setVisibility(View.GONE);

                        } else {
                            Welcome.setVisibility(View.VISIBLE);
                            menuLayout.setVisibility(View.GONE);
                            contactInfoBox.setVisibility(View.GONE);
                            listSetting.setVisibility(View.GONE);
                            pinkLineSettings.setVisibility(View.GONE);
                            listContacts.setVisibility(View.VISIBLE);
                            editoBtn.setVisibility(View.VISIBLE);
                            Welcome.setVisibility(View.VISIBLE);
                            menuBtn.setText("MENU");
                            welcomeTXT.setText("WELCOME");
                            contactText.setVisibility(View.GONE);
                            settingsTxt.setTextColor(Color.parseColor("#FFFFFF"));
                        }
                    }
                }
            });

            backBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadContactList();
                    menuLayout.setVisibility(View.VISIBLE);
                    backBtn.setVisibility(View.GONE);
                    menuBtn.setVisibility(View.VISIBLE);
                    contactInfoBox.setVisibility(View.GONE);
                }
            });

            PhoneCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CallPhone();
                }
            });
            sendEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SendEmail();
                }
            });


            settingsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (inactiveButtons == false) {
                        settingsTxt.setTextColor(Color.parseColor("#ff3366"));
                        showSettings = !showSettings;
                        if (showSettings == true) {
                            listSetting.setVisibility(View.VISIBLE);
                            listContacts.setVisibility(View.GONE);
                            contactListTXT.setVisibility(View.GONE);
                            pinkLineSettings.setVisibility(View.VISIBLE);
                        } else {
                            settingsTxt.setTextColor(Color.parseColor("#FFFFFF"));
                            listSetting.setVisibility(View.GONE);
                            listContacts.setVisibility(View.VISIBLE);
                            contactListTXT.setVisibility(View.VISIBLE);
                            pinkLineSettings.setVisibility(View.GONE);
                        }

                    }
                }
            });

        //Clear Contacts Button desactivate all the others buttons
        ClearContactS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                warningContacts.setVisibility(View.VISIBLE);
                inactiveButtons = true;
                Log.d("inactiveButtons", ""+inactiveButtons);
                ClearContactSTxt.setTextColor(Color.parseColor("#ff3366"));
            }
        });
        dontRemoveBTn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                warningContacts.setVisibility(View.GONE);
                inactiveButtons = false;
                ClearContactSTxt.setTextColor(Color.parseColor("#FFFFFF"));
            }
        });
        RemoveBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                warningContacts.setVisibility(View.GONE);
                clearContacts();
                inactiveButtons = false;
                ClearContactSTxt.setTextColor(Color.parseColor("#FFFFFF"));
            }
        });


        //Load info into TextView
        loadInfo();
        Base64Image = SharedMyPicture64.getString("Json", null);


        //GPS Location
        getmyCurrentLocation();
    }

    //Shared Preferences for Contacts
    public void contactInfoShared(){
        SharedPreferencesContact = getSharedPreferences(MY_CONTACTS, Context.MODE_PRIVATE);// shared preferences for Contatcs
        getContactList = SharedPreferencesContact.getStringSet("myFields", new HashSet<String>());// shared preferences for Picture

    }

    //select picture from phone gallery
    public void imageSelect() {
        permissionsCheck();
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
        } else {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
        }
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    //check permision to load image from Phone Gallery
    public void permissionsCheck() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            return;
        }
    }

    //put the picture loaded from phone gallery into ImageView
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE_REQUEST) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    mImageUri = data.getData();
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                    SharedPreferences.Editor editor = SharedMyPicture.edit();
                    editor.putString("image", String.valueOf(mImageUri));
                    editor.commit();

                    // Sets the ImageView with the Image URI
                    selectPic.setImageURI(mImageUri);
                    mImage.setImageURI(mImageUri);
                    mImage.invalidate();
                    fullScreenWindo();
                }
            }
        }
    }

    //Register Info and Save as JSON Format
    public void registerINfo(){
        nameTxt = name.getText().toString();
        lastTxt = last.getText().toString();
        emailTxt = email.getText().toString();
        phoneTxt = phone.getText().toString();
        ageTxt = age.getText().toString();
        educationTxt = education.getText().toString();
        joinTxt = join.getText().toString();
        outTxt = out.getText().toString();
        softwareTxt = software.getText().toString();
        goalsTxt = goals.getText().toString();
        image64Txt = checkingBase64;
        registerInfo = "[{"+"'name':'"+nameTxt+"', "+"last:'"+lastTxt+"', "+"email:'"+emailTxt+"', "+"phone:'"+phoneTxt+"', "+"age:"+ageTxt+", "+"education:'"+educationTxt+"', "+"join:"+joinTxt+", "+"out:"+outTxt+", "+"software:'"+softwareTxt+"', "+"goals:'"+goalsTxt+"', "+"base:'"+imageString+"'}]";
        SharedPreferences.Editor editor = SharedPreferencesInfo.edit();
        editor.putString("Json", registerInfo);// change this to registerInfo if doesn't works
        editor.commit();
        Register.setVisibility(View.GONE);
        Welcome.setVisibility(View.VISIBLE);
        checkingBase64 = SharedMyPicture64.getString("Json", null);
    }

    //Load info from SharedPreferences and put into ListView User
    public void loadInfo(){
        registerInfo = SharedPreferencesInfo.getString("Json", null);
        if(informationFilled) {

            if(registerInfo == null){
                //nothing
            }else {
                try {
                    JSONArray jsonArray = new JSONArray(registerInfo.toString());
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
                        subjectList.add(subject);
                        adapter = new ListAdapter(subjectList, actividad);
                        listView.setAdapter(adapter);
                        infoName = subject.subject_name;
                        infoLast = subject.subject_last;
                        infoEmail = subject.subject_email;
                        infoPhone = subject.subject_phone;
                        infoAge = subject.subject_age;
                        infoEducation = subject.subject_education;
                        infoJoin = subject.subject_join;
                        infoOut = subject.subject_out;
                        infoKnowledge = subject.subject_software;
                        infoGoals = subject.subject_goals;

                        //filling the Form again
                        nameInfo.setText(infoName);
                        lastInfo.setText(infoLast);
                        name.setText(infoName);
                        last.setText(infoLast);
                        email.setText(infoEmail);
                        phone.setText(infoPhone);
                        age.setText(infoAge);
                        education.setText(infoEducation);
                        join.setText(infoJoin);
                        out.setText(infoOut);
                        software.setText(infoKnowledge);
                        goals.setText(infoGoals);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }else{
            //Nothing
        }
    }

    //Load info from SharedPreferences and put into ListView User
    public void loadContactList(){
        fullScreenWindo();
        contactList = SharedPreferencesContact.getString("contact", null);
        Map<String, ?> prefsMap = SharedPreferencesContact.getAll();
        List<Sampler.Value> list = new ArrayList<Sampler.Value>((Collection<? extends Sampler.Value>) prefsMap.values());
        Collections.sort(list);
        String clearContactString = list.toString().replace("[", "").replace("]","");

        //Log.d("contactList", ""+clearContactString);

        if(clearContactString.contains(",")){
            ClearContactS.setVisibility(View.VISIBLE);
            //Log.d("contactList", "SHOW");
            contactListTXT.setVisibility(View.VISIBLE);

        }else {
            ClearContactS.setVisibility(View.GONE);
            //Log.d("contactList", "HIDE");
            contactListTXT.setVisibility(View.GONE);
        }

        if (clearContactString == null) {
            //nothing
        }else{
            try {
                JSONArray jsonArray = new JSONArray("[" + clearContactString + "]");
                JSONObject jsonObject;
                subjectList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    subject = new Subject();
                    jsonObject = jsonArray.getJSONObject(i);
                    subject.subject_name = jsonObject.getString("name");
                    subject.subject_last = jsonObject.getString("last");
                    subject.subject_phone = jsonObject.getString("phone");
                    subjectList.add(subject);

                    adapterContact = new ListContacts(subjectList, actividad);
                    listViewContact.setAdapter(adapterContact);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //Load info from SharedPreferences and put into ListView User
    public void loadContactInfo(){
        fullScreenWindo();
        contactList = SharedPreferencesContact.getString("contact", null);
        Map<String, ?> prefsMap = SharedPreferencesContact.getAll();
        List<Sampler.Value> list = new ArrayList<Sampler.Value>((Collection<? extends Sampler.Value>) prefsMap.values());
        Collections.sort(list);
        String clearContactString = list.toString().replace("[", "").replace("]","");
        if (clearContactString == null) {
            //notthing
        }else{
            try {
                JSONArray jsonArray = new JSONArray("[" + clearContactString + "]");
                JSONObject jsonObject;

                subjectList = new ArrayList<>();
                for (int i = 0; i == 0; i++) {
                    subject = new Subject();
                    jsonObject = jsonArray.getJSONObject(numberListViewBtn);
                    subject.subject_name = jsonObject.getString("name");
                    subject.subject_last = jsonObject.getString("last");
                    subject.subject_phone = jsonObject.getString("phone");

                    subject.subject_email = jsonObject.getString("email");
                    subject.subject_phone = jsonObject.getString("phone");
                    subject.subject_age = jsonObject.getString("age");
                    subject.subject_education = jsonObject.getString("education");
                    subject.subject_join = jsonObject.getString("join");
                    subject.subject_out = jsonObject.getString("out");
                    subject.subject_software = jsonObject.getString("software");
                    subject.subject_goals = jsonObject.getString("goals");

                    //Log.d("Goals", "" + jsonObject.getString("goals"));
                    //var for GPS Location
                    String checkLat = jsonObject.getString("lat");
                    String checkLng = jsonObject.getString("lng");
                    Double LatInfo = Double.valueOf(checkLat);
                    Double LngInfo = Double.valueOf(checkLng);

                    //vars for Phone and Email
                    callPhone = jsonObject.getString("phone");
                    senEmail = jsonObject.getString("email");

                    //Conver Lattitud And Longitud to Address
                    if (LatInfo.equals(0.000000) && LngInfo.equals(0.000000)) {
                        //nothing

                    } else{
                        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                    List<Address> addresses = geocoder.getFromLocation(LatInfo, LngInfo, 1);
                    cityName = addresses.get(0).getAddressLine(0);
                    String stateName = addresses.get(0).getAddressLine(1);
                    String countryName = addresses.get(0).getAddressLine(2);
                    subject.subject_city = cityName;
                    }

                    subjectList.add(subject);
                    adapterContactIfon = new InfoContact(subjectList, actividad);
                    contactListView.setAdapter(adapterContactIfon);


                    //Loading Image Base 64 to COntact Picture
                    String ImageContact64 = jsonObject.getString("base");
                    if(ImageContact64 == null){
                        //nothing
                    }else {
                        byte[] bytes = ImageContact64.getBytes();
                        bytes = Base64.decode(ImageContact64, Base64.DEFAULT);
                        Bitmap decodedImage = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        pictureContact.setImageBitmap(decodedImage);

                        contactoName.setText(jsonObject.getString("name"));
                        contactLast.setText(jsonObject.getString("last"));
                    }


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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

    //loading a decoding BAse64 in shared preferences
    public void imageBase64(){
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                BitmapDrawable drawable = (BitmapDrawable) selectPic.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
                byte[] imageBytes = baos.toByteArray();
                imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                //Log.d("Base64Picture", ""+imageString);
                SharedPreferences.Editor editor = SharedMyPicture64.edit();
                editor.putString("Json", imageString);
                editor.commit();
                registerINfo();
    }

    //loading encodign and decoding Bitmap with Base64
    public void loadingBase64(){
        Base64Image = SharedMyPicture64.getString("Json", null);
        byte[] bytes = Base64Image.getBytes();
        bytes = Base64.decode(Base64Image, Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        img.setImageBitmap(decodedImage);
        selectPic.setImageBitmap(decodedImage);
    }

    //GPS Get Curretn Location
    public void getmyCurrentLocation() {
        MainActivity.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!MainActivity.locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

        } else if (MainActivity.locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getLocation();
        }
    }

    //GPS Current Location
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
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
        if(lattitude == null && longitude == null){
            lattitudeCont = "0.000000";
            longitudeCont = "0.000000";
        }else {

            lattitudeCont = lattitude;
            longitudeCont = longitude;
        }
    }

    //make a call
    public void CallPhone(){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+callPhone));
        startActivity(intent);
    }

    //send and email
    public void SendEmail(){
        //Send an Email
        Intent mailIntent = new Intent(Intent.ACTION_VIEW);
        Uri data = Uri.parse("mailto:?subject=" + "Hi I'm "+infoName+" "+infoLast+" and I want to contact with you"+ "&body=" + "Hi I want to contact with you. \n My info is: \n Name: "+infoName+" "+infoLast+" \n"+" my Phone is : "+infoPhone+" and we meet us in "+cityName+ "&to=" + senEmail);
        mailIntent.setData(data);
        startActivity(Intent.createChooser(mailIntent, "Send mail..."));

    }

    //Delet Shared Preferences Contact
    public void clearContacts(){
        //remove warning mesage
        String filePath = getApplicationContext().getFilesDir().getParent()+"/shared_prefs/my_contacts.xml";
        File deletePrefFile = new File(filePath );
        deletePrefFile.delete();
        //remove Favorites List
        Toast.makeText(MainActivity.this, "You Removed your Data", Toast.LENGTH_SHORT).show();
        SharedPreferences.Editor editor = SharedPreferencesContact.edit();
        editor.clear();
        editor.commit();
        loadContactList();
        listViewContact.setAdapter(null);
    }

    @Override
    public void onResume(){
        super.onResume();
        fullScreenWindo();
    }
}