package com.mmt.playground.vbo;

import android.content.Context;
import android.opengl.GLSurfaceView;

public class MainView extends GLSurfaceView {

	private final Scene _renderer; 
	
	public MainView(Context context) {
		super(context);
		
		// Renderer erzeugen und setzen
		_renderer = new Scene();
		setRenderer(_renderer);
		
		// Rendern nur auf Anforderung
		//setRenderMode(RENDERMODE_WHEN_DIRTY);
	}

}
