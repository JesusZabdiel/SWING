package com.swing.itesm;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;


class PantallaMenu extends Pantalla {

    private final Juego juego;

    //private Texture texturaFondo;

    private Texture menu_1, menu_2, texturaOjo, texturaPupila,
            texturaBtnPlay, texturaBtnOptions, texturaBtnShop, texturaBtnCustomize;

    // Menu
    private Stage escenaMenu;  // botones,....
    //Administra la carga de assets
    private  final AssetManager assetManager;
    private Sprite pupila;
    private float time = 0;

    public PantallaMenu(Juego juego) {
        assetManager = new AssetManager();
        this.juego = juego;
    }

    @Override
    public void show() {
        cargarTexturas();
        crearMenu();
        Gdx.input.setInputProcessor(escenaMenu);
        pupila = new Sprite(texturaPupila);
        pupila.setPosition(840,300);

    }

    private void cargarTexturas() {
        assetManager.load("menu_1.png", Texture.class);
        assetManager.load("menu_2.png", Texture.class);
        assetManager.load("menu_ojo.png", Texture.class);
        assetManager.load("menu_pupila.png", Texture.class);

        assetManager.load("button_play.png", Texture.class);
        assetManager.load("button_options.png", Texture.class);
        assetManager.load("button_shop.png", Texture.class);
        assetManager.load("button_customize.png", Texture.class);

        assetManager.finishLoading();
        menu_1 = assetManager.get("menu_1.png");
        menu_2 = assetManager.get("menu_2.png");
        texturaOjo = assetManager.get("menu_ojo.png");
        texturaPupila = assetManager.get("menu_pupila.png");

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
        pupila.setScale(-(float)Math.random()*0.2f+1);
        time = time+delta;
        if ((int)time%10==3){
            System.out.println('1');
            pupila.setPosition(855,320);
        }else if ((int)time%10==4){
            System.out.println('2');
            pupila.setPosition(830,280);
        }else if ((int)time%10==7){
            System.out.println('3');
            pupila.setPosition(880,330);
        }else if ((int)time%10==8) {
            pupila.setPosition(860, 320);
        }else {
            pupila.setPosition(840,300);
        }


        batch.begin();
        batch.draw(menu_1,0,0);
        batch.draw(texturaOjo,660,190);
        pupila.draw(batch);
        batch.draw(menu_2,0,0);

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

      texturaBtnCustomize.dispose();
      texturaBtnOptions.dispose();
      texturaBtnPlay.dispose();
      texturaBtnShop.dispose();
      assetManager.unload("menu_1.png");
      assetManager.unload("menu_2.png");
      assetManager.unload("menu_ojo.png");
      assetManager.unload("menu_pupila.png");
      assetManager.unload("button_play.png");
      assetManager.unload("button_options.png");
      assetManager.unload("button_shop.png");
      assetManager.unload("button_customize.png");

    }

}