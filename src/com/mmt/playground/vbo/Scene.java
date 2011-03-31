package com.mmt.playground.vbo;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLU;

import com.mmt.playground.vbo.scene.CubeNode;
import com.mmt.playground.vbo.scene.RootSceneNode;

public class Scene implements android.opengl.GLSurfaceView.Renderer {

	/**
	 * Scene Graph
	 */
	private final RootSceneNode _graph = new RootSceneNode();
	
	public Scene() {
	}
	
	/**
	 * Erzeugt den Scene Graph
	 */
	private void createScene() {
		if (_graph.getChildCount() > 0) return;
		
		// Würfel erzeugen
		_graph.add(new CubeNode()
					.setPosition(0.5f, 0, -10)
				);
		
		_graph.add(new CubeNode()
			.setPosition(0, 0, -5)
			.setColor(0, 1, 0)
		);
	}
	
	float angle = 0;
	
	public void onDrawFrame(GL10 gl) {
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		
		// Depth Buffering aktivieren
		gl.glEnable(GL10.GL_DEPTH_TEST);
		
		// Culling aktivieren
		gl.glEnable(GL10.GL_CULL_FACE);
		gl.glCullFace(GL10.GL_BACK);
		gl.glFrontFace(GL10.GL_CCW);
			
		angle = (angle + 0.05f)%360f;
		_graph.getChild(0).setPosition((float)Math.sin(angle)*2, 0, (float)Math.cos(angle)*9 - 10);
				
		// Und los.
		_graph.render(gl);
	}

	public void onSurfaceChanged(GL10 gl, int width, int height) {
		gl.glViewport(0, 0, width, height);
		
		gl.glMatrixMode(GL10.GL_PROJECTION);
	    gl.glLoadIdentity();
	    GLU.gluPerspective(gl, 45.0f, (float)width / (float)height, 0.1f, 100.0f);
	    
	    gl.glMatrixMode(GL10.GL_MODELVIEW);
	}

	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		gl.glClearColor(0.25f, 0.25f, 0.25f, 1f);

		// Erzeugt die Szene
		createScene();
		
		// Scene Graph initialisieren
		_graph.setup(gl);
	}
}
