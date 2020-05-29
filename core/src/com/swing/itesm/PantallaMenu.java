package com.swing.itesm;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
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
            texturaBtnJugar, texturaBtnAcerca, texturaBtnComoJugar, texturaBtnPersonalizacion;

    //Objeto preferencias score
    Preferences preferencias;

    //Musica
    //highScore
    private int higScore;

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
        crearObjetosPreferencias();
        cargarPreferencias();
        cargarTexturas();
        crearMenu();
        cargarPreferencias();
        Gdx.input.setInputProcessor(escenaMenu);
        pupila = new Sprite(texturaPupila);
        menu_2 = new Sprite(texturaMenu_2);
        pupila.setPosition(840,300);
        menu_2.setPosition(0,0);
        if(preferencias.getBoolean("Musica")){
            juego.musicaMenu.play();
            juego.musicaMenu.setLooping(true);
        }

    }

    private void crearObjetosPreferencias() {
        preferencias =  Gdx.app.getPreferences("Preferencias");
    }

    private void cargarPreferencias() {
        higScore = preferencias.getInteger("HighScore",0);
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

        texturaBtnJugar = assetManager.get("Jugar.png");
        texturaBtnAcerca = assetManager.get("Acerca.png");
        texturaBtnComoJugar = assetManager.get("Como_jugar.png");
        texturaBtnPersonalizacion = assetManager.get("Personalización.png");

    }

    private void crearMenu() {

        escenaMenu = new Stage(vista);


        // Boton Play
        TextureRegionDrawable trdPlay = new TextureRegionDrawable(new TextureRegion(texturaBtnJugar));

        // Boton JugarP
        //Texture texturaBtnJugarP = new Texture("btnSpace.png");
        //TextureRegionDrawable trdJugarP = new TextureRegionDrawable(new TextureRegion(texturaBtnJugarP));

        //Boton Options
        TextureRegionDrawable trdAcerca = new TextureRegionDrawable(new TextureRegion(texturaBtnAcerca));

        // Boton Cómo jugar
        TextureRegionDrawable trdComoJugar = new TextureRegionDrawable(new TextureRegion(texturaBtnComoJugar));

        // Boton customize
        TextureRegionDrawable trdPersonalizacion = new TextureRegionDrawable(new TextureRegion(texturaBtnPersonalizacion));


        //******** Boton Provisional Pantalla config **********
        Texture texturaBtnConfig = new Texture("check.png");
        TextureRegionDrawable trdBtnConfiguracion = new TextureRegionDrawable(new TextureRegion(texturaBtnConfig));


        ImageButton btnConfig = new ImageButton(trdBtnConfiguracion);


        ImageButton btnJugar = new ImageButton(trdPlay);
        ImageButton btnAcerca = new ImageButton(trdAcerca);
        ImageButton btnComoJugar = new ImageButton(trdComoJugar);
        ImageButton btnPersonalizacion = new ImageButton(trdPersonalizacion);

        int separacion = 35;
        btnJugar.setPosition(80,480);
        btnComoJugar.setPosition(80, btnJugar.getY()-btnComoJugar.getHeight()-separacion);
        btnPersonalizacion.setPosition(80,btnComoJugar.getY()-btnPersonalizacion.getHeight()-separacion);
        btnAcerca.setPosition(80,btnPersonalizacion.getY()-btnAcerca.getHeight()-separacion);

        btnConfig.setPosition(100,250);

        //Listener1
        btnJugar.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                juego.musicaMenu.stop();
                juego.setScreen(new PantallaCargando(juego));
            }
        });

        //Listener2
        btnPersonalizacion.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                juego.setScreen(new PantallaCustomize(juego));
            }
        });

        //Listener3
        btnComoJugar.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                juego.setScreen(new PantallaInstrucciones(juego));
            }
        });

        //Listener4
        btnAcerca.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                juego.setScreen(new PantallaAcerca(juego));
            }
        });

        btnConfig.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                juego.setScreen(new PantallaConfiguracion(juego));
            }
        });

        escenaMenu.addActor(btnJugar);
        escenaMenu.addActor(btnAcerca);
        escenaMenu.addActor(btnComoJugar);
        escenaMenu.addActor(btnPersonalizacion);
        escenaMenu.addActor(btnConfig);

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
        batch.begin();
        dibujarBestScore();
        batch.end();

    }

    private void dibujarBestScore() {
        Texto scoreText = new Texto("fontScore.fnt");
        String textoBestScore = "Best Score " + preferencias.getInteger("BestScore");
        scoreText.render(batch, textoBestScore, ANCHO-300,ALTO-50);
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

        texturaBtnPersonalizacion.dispose();
        texturaBtnAcerca.dispose();
        texturaBtnJugar.dispose();
        texturaBtnComoJugar.dispose();
        assetManager.unload("menu_1.png");
        assetManager.unload("menu_2.png");
        assetManager.unload("menu_ojo.png");
        assetManager.unload("menu_pupila.png");
        assetManager.unload("Jugar.png");
        assetManager.unload("Acerca.png");
        assetManager.unload("Como_jugar.png");
        assetManager.unload("Personalización.png");
        assetManager.unload("Redhead_From_Mars.mp3");


    }

}