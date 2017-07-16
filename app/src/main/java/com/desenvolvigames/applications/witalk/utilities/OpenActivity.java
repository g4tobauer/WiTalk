package com.desenvolvigames.applications.witalk.utilities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


/**
 * Created by Joao on 16/07/2017.
 */

public class OpenActivity {
    public static <T extends AppCompatActivity> void onOpenActivity(AppCompatActivity context, Class<T> openClass, Bundle extras){
        Intent intent = new Intent(context, openClass);
        if(extras != null){
            intent.putExtras(extras);
        }
        context.startActivity(intent);
        context.finish();
    }
}
