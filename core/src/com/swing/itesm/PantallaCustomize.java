package com.swing.itesm;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

class PantallaCustomize extends Pantalla {

    private final Juego juego;

    //Textures
    private Texture texturaPersonaje;
    private Texture rellenoPersonaje;

    // Menu
    private Stage escenaCustomize;  // botones,....

    // Colores
    public Color actualColor;


    //Preferences
    public Preferences preferencias;

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
        crearObjetoPreferencias();
        cargarPreferencias();
        iniciarPersonaje();

        Gdx.input.setInputProcessor(new ProcesadorEntrada());
        crearPantallaCustomize();

    }

    private void cargarPreferencias() {
        actualColor = Juego.colores.get(preferencias.getInteger("ColorPersonaje"));
    }

    private void crearObjetoPreferencias() {
        preferencias = Gdx.app.getPreferences("Preferencias");
    }


    private void iniciarPersonaje() {
        personaje = new Personaje(texturaPersonaje, rellenoPersonaje, actualColor, PantallaPlay.Estado.IDLE,juego );
        personaje.sprite.setScale(2);
        personaje.color.setScale(2);
        personaje.sprite.setPosition(ANCHO/3-personaje.sprite.getWidth(),ALTO/10+personaje.sprite.getHeight());
        personaje.color.setPosition(ANCHO/3-personaje.sprite.getWidth(),ALTO/10+personaje.sprite.getHeight());

    }

    private void crearPantallaCustomize() {

        escenaCustomize = new Stage(vista);

        // Boton Salir
        Texture texturaBtnMenu = new Texture("Salir.png");
        TextureRegionDrawable trdMenu = new TextureRegionDrawable(new TextureRegion(texturaBtnMenu));

        //btn cambiar color
        Texture texturaBtnTest = new Texture("Cambiar color.png");
        TextureRegionDrawable trdTest = new TextureRegionDrawable(new TextureRegion(texturaBtnTest));

        //Botón guardar
        Texture textureGuardar = new Texture("Guardar.png");
        TextureRegionDrawable trdGuardar = new TextureRegionDrawable(new TextureRegion(textureGuardar));

        ImageButton btnMenu = new ImageButton(trdMenu);
        ImageButton btnTest = new ImageButton(trdTest);
        ImageButton btbGuardar = new ImageButton(trdGuardar);


        btnMenu.setPosition(80,ALTO-40-btnMenu.getHeight());
        btnTest.setPosition(ANCHO/2+20, ALTO/2);
        btbGuardar.setPosition(ANCHO/2 + 220, ALTO/2 - 150);

        //Listener1
        btnMenu.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                juego.setScreen(new PantallaMenu(juego));
            }
        });

        btnTest.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                cambiarColorPersonaje();
            }
        });

        btbGuardar.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                preferencias.putInteger("ColorPersonaje", Juego.colores.indexOf(actualColor, true));
                preferencias.flush();
            }
        });

        escenaCustomize.addActor(btnMenu);
        escenaCustomize.addActor(btnTest);
        escenaCustomize.addActor(btbGuardar);

        Gdx.input.setInputProcessor(escenaCustomize);
    }

    private void cambiarColorPersonaje() {
        int nuevoColor = Juego.colores.indexOf(actualColor, true) +1;
        if (nuevoColor <= Juego.colores.size-1){
            personaje.setColor(Juego.colores.get(nuevoColor));
            actualColor = Juego.colores.get(nuevoColor);
        }else{
            actualColor = Color.RED;
            personaje.setColor(actualColor);
        }

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

        escenaCustomize.draw();

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
