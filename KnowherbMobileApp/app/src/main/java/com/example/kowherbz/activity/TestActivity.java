package com.example.kowherbz.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.TextView;

import com.example.kowherbz.R;
import com.example.kowherbz.holder.ParentContentHolder;
import com.example.kowherbz.utility.JsonGrabber;
import com.example.kowherbz.utility.RawResourceIDs;
import com.example.kowherbz.utility.SystemUtility;

import java.util.Map;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        //TODO make recycler view title font size adaptive based on
        // title length the current place title is considered as the longest on "activity_test.xml"

        TextView textView = findViewById(R.id.testTV);
        RawResourceIDs.init(this);
        StringBuilder stringBuilder = new StringBuilder();
        for(Map.Entry<String, Integer> item : RawResourceIDs.getRawResourceMapSakitAtLunas().entrySet()){
            stringBuilder.append(item.getKey()).append("\n");
        }
        textView.setText(stringBuilder.toString());
        //int id = RawResourceIDs.findInMap(getResources().getStringArray(R.array.sakit_lower_case)[0]);
        getResources().openRawResource(RawResourceIDs.findInMapSakitAtLunas("alipunga"));

        ParentContentHolder parentContentHolder = JsonGrabber.grabJsonDataFromRaw(this, RawResourceIDs.findInMapSakitAtLunas("alipunga"));
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        final Configuration override = new Configuration(newBase.getResources().getConfiguration());
        override.fontScale = SystemUtility.STANDARD_FONT_SCALE;
        applyOverrideConfiguration(override);
    }
}