package com.swing.itesm;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;

class PantallaShop extends Pantalla {

    private final Juego juego;


    private Texture backGround6, backGround5, backGround4, backGround3, backGround2, backGround1;

    private  final AssetManager assetManager;

    // Menu
    private Stage escenaMenu;  // botones,....



    public PantallaShop(Juego juego) {
        assetManager = new AssetManager();
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
        Texture texturaBtnMenu = new Texture("button_menu.png");
        TextureRegionDrawable trdMenu = new TextureRegionDrawable(new TextureRegion(texturaBtnMenu));

        ImageButton btnMenu = new ImageButton(trdMenu);

        btnMenu.setPosition(0,0);

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

        assetManager.load("layers/1.png", Texture.class);
        assetManager.load("layers/2.png", Texture.class);
        assetManager.load("layers/3.png", Texture.class);
        assetManager.load("layers/4.png", Texture.class);
        assetManager.load("layers/5.png", Texture.class);
        assetManager.load("layers/6.png", Texture.class);
        assetManager.finishLoading();
        backGround1 = assetManager.get("layers/1.png");
        backGround2 = assetManager.get("layers/2.png");
        backGround3 = assetManager.get("layers/3.png");
        backGround4 = assetManager.get("layers/4.png");
        backGround5 = assetManager.get("layers/5.png");
        backGround6 = assetManager.get("layers/6.png");

    }



    @Override
    public void render(float delta) {
        borrarPantalla();

        batch.setProjectionMatrix(camara.combined);
        batch.begin();
        batch.draw(backGround6,0,0);
        batch.draw(backGround5,0,0);
        batch.draw(backGround4,0,0);
        batch.draw(backGround3,0,0);
        batch.draw(backGround2,0,0);
        batch.draw(backGround1,0,0);

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

        backGround6.dispose();
        backGround5.dispose();
        backGround4.dispose();
        backGround3.dispose();
        backGround2.dispose();
        backGround1.dispose();
        assetManager.unload("layers/1.png");
        assetManager.unload("layers/2.png");
        assetManager.unload("layers/3.png");
        assetManager.unload("layers/4.png");
        assetManager.unload("layers/5.png");
        assetManager.unload("layers/6.png");
        assetManager.unload("button_play.png");
        assetManager.unload("button_options.png");
        assetManager.unload("button_shop.png");
        assetManager.unload("button_customize.png");

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
