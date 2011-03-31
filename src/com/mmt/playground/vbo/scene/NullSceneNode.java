package com.mmt.playground.vbo.scene;

import javax.microedition.khronos.opengles.GL10;

/**
 * Ein Knoten, der selbst nichts rendert und nur zu organisatorischen Zwecken verwendet wird
 * @author sunside
 *
 */
public class NullSceneNode extends SceneNode {

	/**
	 * Ignoriert
	 */
	@Override
	protected boolean renderInternal(GL10 gl) {
		return true;
	}

	@Override
	protected void setupNode(GL10 gl) {
	}

	@Override
	protected void cleanupNode(GL10 gl) {
	}

}
