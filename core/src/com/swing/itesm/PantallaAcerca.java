package com.swing.itesm;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;

class PantallaAcerca extends Pantalla {

    private final Juego juego;


    private Texture texturaFondoOptions, creditosMusica;

    // Menu
    private Stage escenaMenu;  // botones,....



    public PantallaAcerca(Juego juego) {
        this.juego = juego;
    }

    @Override
    public void show() {

        cargarTexturas();



        Gdx.input.setInputProcessor(new ProcesadorEntrada());
        crearMenu();

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
                juego.setScreen(new PantallaMenu(juego));
            }
        });

        escenaMenu.addActor(btnMenu);

        Gdx.input.setInputProcessor(escenaMenu);
    }



    private void cargarTexturas() {

        texturaFondoOptions = new Texture("creditos.png");
        creditosMusica = new Texture("musica.png");

    }



    @Override
    public void render(float delta) {
        borrarPantalla();

        batch.setProjectionMatrix(camara.combined);

        batch.begin();

        batch.draw(texturaFondoOptions,0,0);
        batch.draw(creditosMusica, ANCHO/2-creditosMusica.getWidth()/2, 20);

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

        texturaFondoOptions.dispose();
        creditosMusica.dispose();

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
