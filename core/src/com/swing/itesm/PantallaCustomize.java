package com.swing.itesm;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;

class PantallaCustomize extends Pantalla {

    private final Juego juego;

    //Textures
    private Texture texturaPersonaje;
    private Texture rellenoPersonaje;

    // Menu
    private Stage escenaMenu;  // botones,....

    // Colores

    public PantallaPlay.Estado estadoPersonaje;
    private Personaje personaje;
    private Texture texturaPantalla;

    //AssetManager
    private AssetManager assetManager;



    public PantallaCustomize(Juego juego)
    {
        assetManager = new AssetManager();
        this.juego = juego;
    }

    @Override
    public void show() {

        cargarTexturas();
        iniciarPersonaje();


        Gdx.input.setInputProcessor(new ProcesadorEntrada());
        crearMenu();

    }

    private void iniciarPersonaje() {
        personaje = new Personaje(texturaPersonaje, rellenoPersonaje, PantallaPlay.Estado.IDLE);

        personaje.sprite.setScale(2);
        personaje.color.setScale(2);

        personaje.sprite.setPosition(ANCHO/2-personaje.sprite.getWidth(),ALTO/6+personaje.sprite.getHeight());
        personaje.color.setPosition(ANCHO/2-personaje.sprite.getWidth(),ALTO/6+personaje.sprite.getHeight());

    }

    private void crearMenu() {

        escenaMenu = new Stage(vista);

        // Boton Play
        Texture texturaBtnMenu = new Texture("Salir.png");
        TextureRegionDrawable trdMenu = new TextureRegionDrawable(new TextureRegion(texturaBtnMenu));

        ImageButton btnMenu = new ImageButton(trdMenu);

        btnMenu.setPosition(80,ALTO-40-btnMenu.getHeight());

        //Listener1
        btnMenu.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                personaje.setColor(Color.VIOLET);
                juego.setScreen(new PantallaMenu(juego));
            }
        });

        escenaMenu.addActor(btnMenu);

        Gdx.input.setInputProcessor(escenaMenu);
    }



    private void cargarTexturas() {
        assetManager.load("fondo.png", Texture.class);
        assetManager.load("ninjaTrazo.png", Texture.class);
        assetManager.load("ninjaRelleno.png", Texture.class);

        assetManager.finishLoading();
        texturaPantalla = assetManager.get("fondo.png");

        texturaPersonaje = assetManager.get("ninjaTrazo.png");
        rellenoPersonaje = assetManager.get("ninjaRelleno.png");
    }



    @Override
    public void render(float delta) {

        borrarPantalla();
        batch.setProjectionMatrix(camara.combined);
        batch.begin();

        batch.draw(texturaPantalla,0,0);

        personaje.render(batch);

        batch.end();

        escenaMenu.draw();

    }

    @Override
    public void resize(int width, int height) {

    }


    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        texturaPantalla.dispose();

        assetManager.unload("ninjaTrazo.png");
        assetManager.unload("ninjaRelleno.png");

        assetManager.finishLoading();

        texturaPantalla = assetManager.get("fondo.png");





    }

    private class ProcesadorEntrada implements InputProcessor {
        @Override
        public boolean keyDown(int keycode) {
            return false;
        }

        @Override
        public boolean keyUp(int keycode) {
            return false;
        }

        @Override
        public boolean keyTyped(char character) {
            return false;
        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            return false;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            return false;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            return false;
        }

        @Override
        public boolean mouseMoved(int screenX, int screenY) {
            return false;
        }

        @Override
        public boolean scrolled(int amount) {
            return false;
        }
    }


}
