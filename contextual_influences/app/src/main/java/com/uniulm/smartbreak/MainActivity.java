package com.uniulm.smartbreak;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.uniulm.social_media_interventions.R;

public class MainActivity extends AppCompatActivity{

    //TextView tv_start = (TextView) findViewById(R.id.tv_start);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      //  Button btn_start = (Button) findViewById(R.id.btn_start);
       /* Button btn_start = (Button) findViewById(R.id.quitButton);

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!fgServiceRunning()){
                    clickStart();
                }
            }
        });*/
    }

    public boolean fgServiceRunning(){
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for(ActivityManager.RunningServiceInfo serviceInfo: activityManager.getRunningServices(Integer.MAX_VALUE)){
            if(FGService.class.getName().equals(serviceInfo.service.getClassName())){
                return true;
            }
        }
        return false;
    }

    private void clickStart() {
                Intent serviceIntent = new Intent(this, FGService.class);
                startService(serviceIntent);
    }


}