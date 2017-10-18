package com.elefante;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressWarnings("unused")
public class ProfileActivity extends Activity {

    TextView profileage, profileid, profilephoto, profilename, profilesex, profilebirthyear, profilechipno, profileworkplace, profilelocation, profilebroughtfrom, profileowner, profiledie;

    Elephant elephant;
    Date a, b;
    String age, birthyear, dieyear;
    int elephantIndex, projectIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        starttwo();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void starttwo() {


        projectIndex = getIntent().getIntExtra("project_index", 0);
        elephantIndex = getIntent().getIntExtra("elephant_index", 0);

        profileid = (TextView) findViewById(R.id.textViewid1);
        profilephoto = (TextView) findViewById(R.id.textViewpic1);
        profilename = (TextView) findViewById(R.id.textViewname1);
        profilesex = (TextView) findViewById(R.id.textViewsextype1);
        //	profilebirthyear=(TextView)findViewById(R.id.textViewbirth1);
        profilechipno = (TextView) findViewById(R.id.textViewchip1);
        profileworkplace = (TextView) findViewById(R.id.textViewwork1);
        profilelocation = (TextView) findViewById(R.id.textViewlocation1);
        profilebroughtfrom = (TextView) findViewById(R.id.textViewbrought1);
        profileowner = (TextView) findViewById(R.id.textViewowner1);
        //	profiledie=(TextView)findViewById(R.id.textViewdie1);
        profileage = (TextView) findViewById(R.id.textViewage1);

        elephant = GlobalJSONData.getInstance().getUser().getProject().get(projectIndex).getElephant().get(elephantIndex);
        birthyear = elephant.getBirthyear();    //take birth as die date here
        dieyear = elephant.getDiedate();        //take die as birthdate here
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy");
        try {
            a = sdf1.parse(birthyear);
            b = sdf2.parse(dieyear);
        } catch (ParseException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        String c = sdf1.format(a);
        String d = sdf2.format(b);
        int e = Integer.parseInt(c) - Integer.parseInt(d);
        age = Integer.toString(e);

        profileid.setText(elephant.getElephantid());
        profilename.setText(elephant.getName());
        profilesex.setText(elephant.getSex());
        //	profilebirthyear.setText(elephant.getDiedate());
        profileage.setText(age);
        profilechipno.setText(elephant.getChipno());
        profileworkplace.setText(elephant.getWorkplace());
        profilelocation.setText(elephant.getLocation());
        profilebroughtfrom.setText(elephant.getBringfrom());
        profileowner.setText(elephant.getOwner());
        //	profiledie.setText(elephant.getBirthyear());


    }


}
