package com.desenvolvigames.applications.witalk.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Joao on 25/05/2017.
 */

public abstract class BaseActivity extends AppCompatActivity {
    protected abstract void onInitControls();
    protected abstract void onInitEvents();
    protected abstract void onSincronize();
}
