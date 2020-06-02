package com.swing.itesm;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Array;

public class Juego extends Game {
	public static final float ANCHO = 1280;
	public static final float ALTO = 720;
	public static Array<Color> colores;
	public Music musicaMenu;

	private final AssetManager assetManager = new AssetManager();



	@Override
	public void create () {
		assetManager.setLoader(TiledMap.class,
				new TmxMapLoader(new InternalFileHandleResolver()));
		assetManager.load("song18.mp3", Music.class);

		assetManager.finishLoading();
		musicaMenu = assetManager.get("song18.mp3");


		//pantalla inicial
		setScreen(new PantallaMenu(this));
		colores = new Array<>();
		colores.add(Color.RED);
		colores.add(Color.BLUE);
		colores.add(Color.DARK_GRAY);
		colores.add(Color.SALMON);
		colores.add(Color.GREEN);
		colores.add(Color.GOLD);
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
