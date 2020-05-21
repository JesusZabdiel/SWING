package com.swing.itesm;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class Juego extends Game {
	public static final float ANCHO = 1280;
	public static final float ALTO = 720;
	private final AssetManager assetManager = new AssetManager();



	@Override
	public void create () {
		assetManager.setLoader(TiledMap.class,
				new TmxMapLoader(new InternalFileHandleResolver()));

		//pantalla inicial
		setScreen(new PantallaMenu(this));
	}

	// Accesor del AssetManager para que otras clases lo utilicen
	public AssetManager getAssetManager(){
		return assetManager;
	}

	public void dispose(){
		super.dispose();
		assetManager.clear();
	}

}
