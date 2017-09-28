package com.seoul.culture;

import com.seoul.culture.map.ActivityHostFragment;
import com.seoul.culture.map.MiniMap;

import android.app.Activity;

public class CenterMap extends ActivityHostFragment {
    
    @Override
    public Class<? extends Activity> getActivityClass() {
        return MiniMap.class;
    }
    
}
