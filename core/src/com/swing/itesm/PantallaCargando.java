package com.swing.itesm;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class PantallaCargando implements Screen {
    private Juego juego;

    //Camara y vista
    private OrthographicCamera camara;
    private Viewport vista;
    private SpriteBatch batch;

    // Texturas
    private Texture texturaCargando;
    private Sprite spriteCargando;

    private  AssetManager assetManager;

    public PantallaCargando(Juego juego){
        this.juego = juego;
        this.assetManager = juego.getAssetManager();
    }

    @Override
    public void show() {

        camara = new OrthographicCamera(1280, 800);
        camara.position.set(1280/2,800/2,0);
        camara.update();
        vista = new StretchViewport(1280, 800, camara);
        batch = new SpriteBatch();

        //Cargar recursos de esta pantalla
        // Source : <a title="icono cargando png" href="https://pngimage.net/icono-cargando-png/">icono cargando png</a>
        assetManager.load("cargando.png", Texture.class);
        assetManager.finishLoading();
        texturaCargando=assetManager.get("cargando.png");
        spriteCargando = new Sprite(texturaCargando);
        spriteCargando.setPosition(1280/4*3-spriteCargando.getWidth()/2,
                720/3-spriteCargando.getHeight()/2);
        //Ahora inicia la carga de los recursos de la siguiente pantalla
        cargarRecursos();
    }

    //Recursos de la pantallaJuego
    private void cargarRecursos() {
        //Texturas
        assetManager.load("ojo.png", Texture.class);
        assetManager.load("layers/1.png", Texture.class);
        assetManager.load("layers/2.png", Texture.class);
        assetManager.load("layers/3.png", Texture.class);
        assetManager.load("layers/4.png", Texture.class);
        assetManager.load("layers/5.png", Texture.class);
        assetManager.load("layers/6.png", Texture.class);
        assetManager.load("ninjaTrazo.png", Texture.class);
        assetManager.load("ninjaRelleno.png", Texture.class);
        assetManager.load("Life.png", Texture.class);
        assetManager.load("lifeBar.png", Texture.class);
        assetManager.load("lifeBarBack.png", Texture.class);
        assetManager.load("lifeBarBlock.png", Texture.class);
        assetManager.load("pause.png", Texture.class);
        assetManager.load("Obstaculo.png", Texture.class);
        assetManager.load("invulnerable_Small.png", Texture.class);
        assetManager.load("reloj.png", Texture.class);

        assetManager.load("correr5.mp3", Music.class);
        assetManager.load("salto3.mp3",Sound.class);
        assetManager.load("muerte.mp3",Sound.class);
        assetManager.load("salud.mp3",Sound.class);
        assetManager.load("escudo6.mp3",Sound.class);
        assetManager.load("golpe2.mp3",Sound.class);
        assetManager.load("ralentizar3.mp3",Sound.class);
        assetManager.load("AugustUltraAmbienceLoop.wav", Music.class);

        assetManager.finishLoading();

    }

    @Override
    public void render(float delta) {
        actualizarCarga();
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        spriteCargando.setRotation(spriteCargando.getRotation()-10);//Animación
        batch.setProjectionMatrix(camara.combined);

        batch.begin();
        spriteCargando.draw(batch);
        batch.end();
    }

    private void actualizarCarga() {
        if (assetManager.update()){
            juego.setScreen(new PantallaPlay(juego));
        }else {
            float avance = assetManager.getProgress();
        }
    }

    @Override
    public void resize(int width, int height) {
        vista.update(width,height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        assetManager.unload("cargando.png");
        texturaCargando.dispose();

    }


}
