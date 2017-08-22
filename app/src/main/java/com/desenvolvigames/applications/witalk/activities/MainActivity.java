package com.desenvolvigames.applications.witalk.activities;

import android.content.Intent;
import android.os.Bundle;

import com.desenvolvigames.applications.witalk.control.WitalkService;

public class MainActivity extends AuthenticationActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        startService(new Intent(getBaseContext(), WitalkService.class));
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){super.onActivityResult(requestCode, resultCode, data);}
}
