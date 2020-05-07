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

    private Texture menu_1, texturaMenu_2, texturaOjo, texturaPupila,
            texturaBtnPlay, texturaBtnOptions, texturaBtnShop, texturaBtnCustomize;

    // Menu
    private Stage escenaMenu;  // botones,....
    //Administra la carga de assets
    private  final AssetManager assetManager;
    private Sprite pupila, menu_2;
    private float time = 0;

    PantallaMenu(Juego juego) {
        assetManager = new AssetManager();
        this.juego = juego;
    }

    @Override
    public void show() {
        cargarTexturas();
        crearMenu();
        Gdx.input.setInputProcessor(escenaMenu);
        pupila = new Sprite(texturaPupila);
        menu_2 = new Sprite(texturaMenu_2);
        pupila.setPosition(840,300);
        menu_2.setPosition(0,0);

    }

    private void cargarTexturas() {
        assetManager.load("menu_1.png", Texture.class);
        assetManager.load("menu_2.png", Texture.class);
        assetManager.load("menu_ojo.png", Texture.class);
        assetManager.load("menu_pupila.png", Texture.class);

        assetManager.load("Jugar.png", Texture.class);
        assetManager.load("Acerca.png", Texture.class);
        assetManager.load("Como_jugar.png", Texture.class);
        assetManager.load("Personalización.png", Texture.class);

        assetManager.finishLoading();
        menu_1 = assetManager.get("menu_1.png");
        texturaMenu_2 = assetManager.get("menu_2.png");
        texturaOjo = assetManager.get("menu_ojo.png");
        texturaPupila = assetManager.get("menu_pupila.png");

        texturaBtnPlay = assetManager.get("Jugar.png");
        texturaBtnOptions = assetManager.get("Acerca.png");
        texturaBtnShop = assetManager.get("Como_jugar.png");
        texturaBtnCustomize = assetManager.get("Personalización.png");

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

        btnPlay.setPosition(80,370);

        btnCustomize.setPosition(80,280);

        btnShop.setPosition(80,180);

        btnOptions.setPosition(80,100);

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
        menu_2.setColor(1,1,1,1-(float)Math.random()*0.3f);
        pupila.setScale(-(float)Math.random()*0.2f+1);
        time = time+delta;
        if ((int)time%10==3){
            pupila.setPosition(855,320);
        }else if ((int)time%10==4){
            pupila.setPosition(800,280);
            pupila.setRotation(20);
        }else if ((int)time%10==7){
            pupila.setPosition(880,330);
            pupila.setRotation(-10);
        }else if ((int)time%10==8) {
            pupila.setPosition(860, 320);
        }else {
            pupila.setPosition(840,300);
            pupila.setRotation(0);
        }


        batch.begin();
        batch.draw(menu_1,0,0);
        batch.draw(texturaOjo,660,190);
        pupila.draw(batch);
        menu_2.draw(batch);

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
        assetManager.unload("Jugar.png");
        assetManager.unload("Acerca.png");
        assetManager.unload("Como_jugar.png");
        assetManager.unload("Personalización.png");

    }

}