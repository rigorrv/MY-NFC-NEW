package com.jetruby.nfc.example;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class NextWindow extends Activity {

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
    RelativeLayout Welcome, Register;
    Button registerBtn;
    LinearLayout checkInfo;
    LinearLayout lista;
    Boolean checkInfoList = true;
    Button button1;
    TextView textShowList;

    //ListView
    Context actividad = this;
    Subject subject;
    ListView listView;
    ListAdapter adapter;
    List<Subject> subjectList;

    //Form for loading information from user
    public static String registerInfo;
    String infoName, infoLast, infoEmail, infoPhone, infoAge, infoEducation, infoJoin, infoOut, infoKnowledge, infoGoals;
    EditText name, last, email, phone, age, education, join, out, software, goals;
    String nameTxt, lastTxt, emailTxt, phoneTxt, ageTxt, educationTxt, joinTxt, outTxt,softwareTxt, goalsTxt , path;
    TextView nameInfo, lastInfo;


    //code for loading picture from Gallery
    private static final int PICK_IMAGE_REQUEST = 0;
    private final String TAG = "Main Activity";
    private ImageView mImage;
    private Uri mImageUri;

    //sharedPreferences
    SharedPreferences SharedPreferencesInfo;
    SharedPreferences SharedMyPicture;
    String MY_INFO = "my_info";
    String MY_PICTURE = "my_picture";
    Set<String> getMyPicture;
    Set<String> getListValue;
    Boolean informationFilled = false; // check if the Form is filled
    String mImageUriPic;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_window);
        app = (MyApp)getApplication();

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
        getListValue = SharedPreferencesInfo.getStringSet("myFields", new HashSet<String>()); // shared preferences for Info
        getMyPicture = SharedMyPicture.getStringSet("myFields", new HashSet<String>());// shared preferences for Picture
        String mImageUri = SharedMyPicture.getString("image", null);
        if (mImageUri != null) {
            mImage.setImageURI(Uri.parse(mImageUri));
        } else {
            //app.setImageProfile(selectPic,pictureSelected);
            app.setImage(img, navdid); // free last image, and store new one
        }

        //Check if the files in Shared Preferences Exist if not Create it
        File fileXInfo = new File("/data/data/com.example.myimagetest/shared_prefs/my_info.xml");
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
        //BUTTONS
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullScreenWindo();
                registerINfo();
                informationFilled= true;
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
                startActivity(new Intent(v.getContext(), MainActivity.class));
                finish();
            }
        });
        checkInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullScreenWindo();
                if(checkInfoList){
                    lista.setVisibility(View.VISIBLE);
                    textShowList.setText("Click here to Hide your info");
                }else{
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
                if(checkInfoList){
                    lista.setVisibility(View.VISIBLE);
                    textShowList.setText("Click here to Hide your info");
                }else{
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
                String mImageUri = SharedMyPicture.getString("image", null);
                if (mImageUri != null) {
                    selectPic.setImageURI(Uri.parse(mImageUri));
                } else {
                    //app.setImageProfile(selectPic,pictureSelected);
                    app.setImage(img, navdid); // free last image, and store new one
                }
                Welcome.setVisibility(View.GONE); // if exist Welcome GONE
                Register.setVisibility(View.VISIBLE); // if exist register Show me Registration
                loadInfo();
            }
        });
        //Load info into TextView
        loadInfo();
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
        // Check which request we're responding to
        if (requestCode == PICK_IMAGE_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // The user picked a image.
                // The Intent's data Uri identifies which item was selected.
                if (data != null) {
                    // This is the key line item, URI specifies the name of the data
                    mImageUri = data.getData();
                    //Log.d("miPic",""+mImageUri);
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
        registerInfo = "[{"+"'name':'"+nameTxt+"', "+"last:'"+lastTxt+"', "+"email:'"+emailTxt+"', "+"phone:'"+phoneTxt+"', "+"age:"+ageTxt+", "+"education:'"+educationTxt+"', "+"join:"+joinTxt+", "+"out:"+outTxt+", "+"software:'"+softwareTxt+"', "+"goals:'"+goalsTxt+"'}]";
        SharedPreferences.Editor editor = SharedPreferencesInfo.edit();
        editor.putString("Json", registerInfo);
        editor.commit();
        Register.setVisibility(View.GONE);
        Welcome.setVisibility(View.VISIBLE);
    }

    //Load info from SharedPreferences and put into ListView
    public void loadInfo(){
        registerInfo = SharedPreferencesInfo.getString("Json", null);
        //Log.d("JSON", ""+registerInfo);
        if(informationFilled) {

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
                    //Log.d("infoName", "cargando informacion a welcome");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            //Nothing
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



}