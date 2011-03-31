package com.mmt.playground.vbo;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {
	
	MainView _view;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        _view = new MainView(this);
        setContentView(_view);
    }

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}
    
}