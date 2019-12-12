package app.blikecoder.org.multilanguage;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Button changelang;
    Button signInBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     loadLocale();
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.app_name));
        changelang =(Button)findViewById(R.id.change_lang);
        signInBtn  = (Button)findViewById(R.id.sign_in);


        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent callIntent = new Intent(Intent.ACTION_CALL, ussdToCallableUri("*804#"));
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(MainActivity.this,"Grant permission to stop shutdown",Toast.LENGTH_LONG).show();
                    requestPermission();
                }
                else
                {
                    startActivity(callIntent);
                }
            }
        });
        changelang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangeLangDlg();
            }
        });



    }

    public void requestPermission()
    {
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CALL_PHONE},1);

    }

    private Uri ussdToCallableUri(String ussd) {

        String uriString = "";

        if(!ussd.startsWith("tel:"))
            uriString += "tel:";

        for(char c : ussd.toCharArray()) {

            if(c == '#')
                uriString += Uri.encode("#");
            else
                uriString += c;
        }

        return Uri.parse(uriString);
    }


    private void showChangeLangDlg() {

    final String lists[] ={"English","አማርኛ","de Francias"};


        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Choose Language");

        builder.setSingleChoiceItems(lists, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch(which)
                {
                    case 0:

                        setLocale("en");
                        recreate();
                        break;
                    case 1:
                        setLocale("am");
                        recreate();
                        break;
                    case 2:

                        setLocale("fr");
                        recreate();
                        break;
                }

             dialog.dismiss();
            }
        });


        AlertDialog dlg = builder.create();

        dlg.show();

    }

    public void setLocale(String lang)
    {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();

        config.locale = locale;

        getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences sharedPreferences = getSharedPreferences("language", Context.MODE_PRIVATE);

        SharedPreferences.Editor edit = sharedPreferences.edit().putString("lang",lang);

        edit.apply();

    }

    public void loadLocale()
    {
        SharedPreferences pref = getSharedPreferences("language",MODE_PRIVATE);

        String lang =pref.getString("lang","");
        setLocale(lang);

    }
}
