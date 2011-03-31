package com.mmt.playground.vbo.scene;

import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLU;

public class RootSceneNode extends NullSceneNode {

	/**
	 * Die aktive Kamera
	 */
	private CameraSceneNode _activeCamera = new CameraSceneNode();

	/**
	 * Setzt die aktive Kamera
	 * @param camera
	 */
	public void setActiveCamera(CameraSceneNode camera) {
		_activeCamera = camera;
	}
	
	@Override
	public void render(GL10 gl) {

		// Model View setzen
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		
		// Kamera aktivieren
		_activeCamera.setActive(gl);
		
		// Rechtes Auge
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		//gl.glColorMask(false, true, true, true);
		gl.glTranslatef(0.035f, 0, 0);
		
		super.render(gl);
		
		/*
		// Linkes Auge
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		//gl.glColorMask(true, false, false, true);
		gl.glTranslatef(-0.035f, 0, 0);
		
		gl.glClear(GL10.GL_DEPTH_BUFFER_BIT);
		gl.glEnable(GL10.GL_BLEND);
	    gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE);
		
		super.render(gl);
		*/
	}
	
	
	
}
