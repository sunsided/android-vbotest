package com.mmt.playground.vbo.scene;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

public abstract class SceneNode {

	/**
	 * Gibt an, ob Vertex Buffer Objects verfügbar sind
	 */
	protected static boolean VBOSupported = false;
	
	/**
	 *  Die Sichtbarkeit des Nodes
	 */
	private boolean _visible = true; 
	
	/**
	 * X-Position des Nodes
	 */
	private float _posX = .0f;
	
	/**
	 * Y-Position des Nodes
	 */
	private float _posY = .0f;
	
	/**
	 * Z-Position des Nodes
	 */
	private float _posZ = .0f;
	
	/**
	 * Rotkomponente der ambienten Farbe
	 */
	private float _ambientR = 1.0f;
	
	/**
	 * Grünkomponente der ambienten Farbe
	 */
	private float _ambientG = 1.0f;
	
	/**
	 * Blaukomponente der ambienten Farbe
	 */
	private float _ambientB = 1.0f;
	
	/**
	 * Liste der Kindknoten
	 */
	private List<SceneNode> _childNodes; 
	
	/**
	 * Der Elternknoten
	 */
	private SceneNode _parent;
	
	/**
	 * Erzeugt einen neuen Node
	 * @param parent Der Elternknoten
	 */
	public SceneNode() {
		this(null);
	}
	
	/**
	 * Erzeugt einen neuen Node
	 * @param parent Der Elternknoten
	 */
	public SceneNode(SceneNode parent) {
		_parent = parent;
	}
	
	/**
	 * Liefert den Elternknoten
	 * @return
	 */
	public final SceneNode getParent() { return _parent; } 
	
	/**
	 * Siefert den Elternknoten
	 * @param parent Der Elternknoten
	 */
	public final void setParent(SceneNode parent) { _parent = parent; } 
	
	/**
	 * Ermittelt, ob der Node sichtbar ist
	 * @return true, wenn sichtbar
	 */
	public final boolean isVisible() { return _visible; }
	
	/**
	 * Setzt die Sichtbarkeit des Nodes
	 * @param visible Die Sichtbarkeit
	 */
	public final void setVisible(boolean visible) { _visible = visible; }
	
	/**
	 * Liefert die X-Koordinate der Position des Nodes
	 * @return Die X-Koordinate
	 */
	public float getX() { return _posX; }
	
	/**
	 * Liefert die Y-Koordinate der Position des Nodes
	 * @return Die Y-Koordinate
	 */
	public float getY() { return _posY; }
	
	/**
	 * Liefert die Z-Koordinate der Position des Nodes
	 * @return Die Z-Koordinate
	 */
	public float getZ() { return _posZ; }
	
	/**
	 * Setzt die Position des Nodes
	 * @param x
	 * @param y
	 * @param z
	 */
	public SceneNode setPosition(float x, float y, float z) {
		_posX = x; _posY = y; _posZ = z;
		return this;
	}
	
	/**
	 * Setzt die ambiente Farbe
	 * @param r
	 * @param g
	 * @param b
	 */
	public SceneNode setColor(float r, float g, float b) {
		_ambientR = r;
		_ambientG = g;
		_ambientB = b;
		return this;
	}
	
	/**
	 * Fügt einen Kindknoten zum Node hinzu
	 * @param child Der hinzuzufügende Kindknoten
	 */
	public void add(SceneNode child) {
		if (_childNodes == null) _childNodes = new ArrayList<SceneNode>();
		_childNodes.add(child);
		child.setParent(this);
	}
	
	/**
	 * Entfernt einen Kindknoten
	 * @param child Das zu entfernende Kind
	 * @return true, wenn das Kind entfernt wurde
	 */
	public boolean remove(SceneNode child) {
		if (_childNodes == null) return false;
		if( _childNodes.remove(child)) {
			child.setParent(null);
			return true;
		}
		return false;
	}
	
	/**
	 * Liefert die Anzahl der Kindknoten
	 * @return
	 */
	public int getChildCount() {
		if (_childNodes == null) return 0;
		return _childNodes.size();
	}
	
	public SceneNode getChild(int index) {
		if (_childNodes == null || index < 0 || index >= _childNodes.size()) return null;
		return _childNodes.get(index);
	}
	
	/**
	 * Interne Logik zum Rendern des Elementes
	 * @param gl
	 * @return true, wenn Kindelemente gerendert werden dürfen
	 */
	protected abstract boolean renderInternal(GL10 gl);
		
	/**
	 * Rendert das Element
	 * @param gl
	 */
	public void render(GL10 gl) {
		if (!_visible) return;
		
		// Model view aktivieren
		gl.glPushMatrix();
		gl.glMatrixMode(GL10.GL_MODELVIEW);
				
		// Objekt verschieben
		gl.glTranslatef(_posX, _posY, _posZ);
		
		// Ambiente Farbe setzen
		gl.glColor4f(_ambientR, _ambientG, _ambientB, 1.0f);
		
		// Rendern
		boolean renderChilds = renderInternal(gl);

		// Child nodes rendern
		if (renderChilds && _childNodes != null) {
			for (int i=0; i<_childNodes.size(); ++i) {
				_childNodes.get(i).render(gl);
			}
		}
		
		// Alte Matrix wiederherstellen
		gl.glPopMatrix();
	}

	/**
	 * Führt ein initiales Setup des Scene Graph durch, testet auf OpenGL-Feature, etc.
	 * @param gl
	 */
	public final void setup(GL10 gl) {
		
		// Test auf Vertex Buffer Objects
		String extensions = gl.glGetString(GL10.GL_EXTENSIONS);
		VBOSupported = extensions.contains( "vertex_buffer_object" );
		
		// Node selbst vorbereiten
		setupNode(gl);
		
		// Child Nodes vorbereiten
		if (_childNodes != null) {
			for (int i=0; i<_childNodes.size(); ++i) {
				_childNodes.get(i).setupNode(gl);
			}
		}
	}
	
	/**
	 * Führt ein Setup des Nodes durch
	 */
	protected abstract void setupNode(GL10 gl);
	
	/**
	 * Führt ein Cleanup des Scene Graph durch
	 * @param gl
	 */
	public final void cleanup(GL10 gl) {
				
		// Node selbst aufräumen
		cleanupNode(gl);
		
		// Child Nodes aufräumen
		if (_childNodes != null) {
			for (int i=0; i<_childNodes.size(); ++i) {
				_childNodes.get(i).cleanupNode(gl);
			}
		}
	}
	
	/**
	 * Führt ein Cleanup des Nodes durch
	 */
	protected abstract void cleanupNode(GL10 gl);
}
