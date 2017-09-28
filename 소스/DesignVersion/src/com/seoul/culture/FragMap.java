package com.seoul.culture;

import com.seoul.culture.map.ActivityHostFragment;
import com.seoul.culture.map.MyMap;

import android.app.Activity;

public class FragMap extends ActivityHostFragment {
    
    @Override
    protected Class<? extends Activity> getActivityClass() {
        return MyMap.class;
    }
    
}