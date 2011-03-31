package com.mmt.playground.vbo.scene;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

/**
 * Ein Knoten, der einen Kubus rendert
 * @author sunside
 *
 */
public class CubeNode extends SceneNode {

	/**
	 * Breite des Würfels
	 */
	private final float _width;
	
	/**
	 * Höhe des Würfels
	 */
	private final float _height;
	
	/**
	 * Tiefe des Würfels
	 */
	private final float _depth;
	
	/**
	 * Die ID des VBO
	 */
	private int _vertexVboId;
	
	/**
	 * Die ID des Index-VBO
	 */
	private int _indexVboId;
	
	/**
	 * Erzeugt einen neuen Würfel der Dimensionen 1x1x1
	 */
	public CubeNode() {
		this(1, 1, 1);
	}
	
	/**
	 * Erzeugt einen neuen Kubus
	 * @param width Die Breite
	 * @param height Die Höhe
	 * @param depth Die Länge
	 */
	public CubeNode(float width, float height, float depth) {
		_width = width;
		_height = height;
		_depth = depth;
	}
	
	@Override
	public boolean renderInternal(GL10 gl) {
		
		// OpenGL 1.1-Instanz beziehen
		GL11 gl11 = (GL11)gl;
		
		// VBO binden
		gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, _vertexVboId);
		gl11.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, _indexVboId);
		
		// Vertex Array-State aktivieren und Vertexttyp setzen
		gl11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		
		// Zeichnen!
		gl11.glVertexPointer(3, GL11.GL_FLOAT, 0, 0);
		gl11.glDrawElements(GL11.GL_TRIANGLES, 6, GL11.GL_UNSIGNED_SHORT, 0);
		//                                     ^-- Anzahl der Indizes

		// Vertex Array-State deaktivieren
		gl11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
		
		// Puffer abwählen
		gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
		gl11.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, 0);
		
		return true;
	}

	@Override
	protected void setupNode(GL10 gl) {

		// Vertices erzeugen
		float[] vertices = new float[4*3];
		vertices[ 0] =  0.5f; vertices[ 1] =  0.5f; vertices[ 2] =  0.0f;
		vertices[ 3] = -0.5f; vertices[ 4] =  0.5f; vertices[ 5] =  0.0f;
		vertices[ 6] = -0.5f; vertices[ 7] = -0.5f; vertices[ 8] =  0.0f;
		vertices[ 9] =  0.5f; vertices[10] = -0.5f; vertices[11] =  0.0f;
		
		// Indizes erzeugen
		short[] indices = new short[6];
		indices[0] = 0; indices[1] = 1; indices[2] = 2;
		indices[3] = 2; indices[4] = 3; indices[5] = 0;
		
		// Byte-Puffer für Vertex Buffer erzeugen
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4); // 4 byte per float
		vbb.order(ByteOrder.nativeOrder());
		FloatBuffer vertexBuffer = vbb.asFloatBuffer();

		// Byte-Puffer für Index Buffer erzeugen
		ByteBuffer ibb = ByteBuffer.allocateDirect(indices.length * 2); // 4 byte per short
		ibb.order(ByteOrder.nativeOrder());
		ShortBuffer indexBuffer = ibb.asShortBuffer();
		
		// Daten laden
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);
		indexBuffer.put(indices);
		indexBuffer.position(0);
		
		// OpenGL 1.1-Instanz beziehen
		GL11 gl11 = (GL11)gl;
		
		// VBO erzeugen, ID ermitteln
		IntBuffer buffer = IntBuffer.allocate(2);
		gl11.glGenBuffers(2, buffer);
		_vertexVboId = buffer.get(0);
		_indexVboId = buffer.get(1);
		
		// Vertex-VBO binden und beladen
		gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, _vertexVboId);
		gl11.glBufferData(GL11.GL_ARRAY_BUFFER, 12*4, vertexBuffer, GL11.GL_STATIC_DRAW);
		//                                       ^-- Anzahl der Koordinaten * sizeof(float)
		
		// Index-VBO binden und beladen
		gl11.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, _indexVboId);
		gl11.glBufferData(GL11.GL_ELEMENT_ARRAY_BUFFER, 6*2, indexBuffer, GL11.GL_STATIC_DRAW);
		//                                              ^-- Anzahl der Indizes * sizeof(short)
		
		// Puffer abwählen
		gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
		gl11.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, 0);
		
		// Vertex Buffer wegwerfen
		vertexBuffer.clear();
		indexBuffer.clear();
	}

	@Override
	protected void cleanupNode(GL10 gl) {

		// OpenGL 1.1-Instanz beziehen
		GL11 gl11 = (GL11)gl;
		
		// Puffer aufräumen
		gl11.glDeleteBuffers(2, new int [] { _vertexVboId, _indexVboId}, 0);
		
	}
}
