package com.swing.itesm;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

class PantallaMenu extends Pantalla {

    private final Juego juego;

    //private Texture texturaFondo;

    private Texture backGround6, backGround5, backGround4, backGround3, backGround2, backGround1,
            texturaBtnPlay, texturaBtnOptions, texturaBtnShop, texturaBtnCustomize;

    // Menu
    private Stage escenaMenu;  // botones,....
    //Administra la carga de assets
    private  final AssetManager assetManager;

    public PantallaMenu(Juego juego) {
        assetManager = new AssetManager();
        this.juego = juego;
    }

    @Override
    public void show() {
        cargarTexturas();
        crearMenu();
        Gdx.input.setInputProcessor(escenaMenu);

    }

    private void cargarTexturas() {
        assetManager.load("layers/1.png", Texture.class);
        assetManager.load("layers/2.png", Texture.class);
        assetManager.load("layers/3.png", Texture.class);
        assetManager.load("layers/4.png", Texture.class);
        assetManager.load("layers/5.png", Texture.class);
        assetManager.load("layers/6.png", Texture.class);
        assetManager.load("button_play.png", Texture.class);
        assetManager.load("button_options.png", Texture.class);
        assetManager.load("button_shop.png", Texture.class);
        assetManager.load("button_customize.png", Texture.class);

        assetManager.finishLoading();
        backGround1 = assetManager.get("layers/1.png");
        backGround2 = assetManager.get("layers/2.png");
        backGround3 = assetManager.get("layers/3.png");
        backGround4 = assetManager.get("layers/4.png");
        backGround5 = assetManager.get("layers/5.png");
        backGround6 = assetManager.get("layers/6.png");
        texturaBtnPlay = assetManager.get("button_play.png");
        texturaBtnOptions = assetManager.get("button_options.png");
        texturaBtnShop = assetManager.get("button_shop.png");
        texturaBtnCustomize = assetManager.get("button_customize.png");

    }

    private void crearMenu() {

        escenaMenu = new Stage(vista);


        // Boton Play
        TextureRegionDrawable trdPlay = new TextureRegionDrawable(new TextureRegion(texturaBtnPlay));

        // Boton JugarP
        //Texture texturaBtnJugarP = new Texture("btnSpace.png");
        //TextureRegionDrawable trdJugarP = new TextureRegionDrawable(new TextureRegion(texturaBtnJugarP));

        //Boton Options
        TextureRegionDrawable trdOptions = new TextureRegionDrawable(new TextureRegion(texturaBtnOptions));

        // Boton shop
        TextureRegionDrawable trdShop = new TextureRegionDrawable(new TextureRegion(texturaBtnShop));

        // Boton customize
        TextureRegionDrawable trdCustomize = new TextureRegionDrawable(new TextureRegion(texturaBtnCustomize));



        ImageButton btnPlay = new ImageButton(trdPlay);
        ImageButton btnOptions = new ImageButton(trdOptions);
        ImageButton btnShop = new ImageButton(trdShop);
        ImageButton btnCustomize = new ImageButton(trdCustomize);


        btnPlay.setPosition(ANCHO/2-btnPlay.getWidth()/2,2*ALTO/3);

        btnCustomize.setPosition(ANCHO/2-btnPlay.getWidth()/2,2*ALTO/3-100);

        btnShop.setPosition(ANCHO/2-btnPlay.getWidth()/2,2*ALTO/3-200);

        btnOptions.setPosition(ANCHO/2-btnPlay.getWidth()/2,2*ALTO/3-300);

        //Listener1
        btnPlay.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                juego.setScreen(new PantallaCargando(juego));
            }
        });

        //Listener2
        btnCustomize.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                juego.setScreen(new PantallaCustomize(juego));
            }
        });

        //Listener3
        btnShop.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                juego.setScreen(new PantallaShop(juego));
            }
        });

        //Listener4
        btnOptions.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                juego.setScreen(new PantallaOptions(juego));
            }
        });

        escenaMenu.addActor(btnPlay);
        escenaMenu.addActor(btnOptions);
        escenaMenu.addActor(btnShop);
        escenaMenu.addActor(btnCustomize);

        Gdx.input.setInputProcessor(escenaMenu);


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
      texturaBtnCustomize.dispose();
      texturaBtnOptions.dispose();
      texturaBtnPlay.dispose();
      texturaBtnShop.dispose();
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

}