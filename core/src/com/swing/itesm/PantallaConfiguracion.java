package com.swing.itesm;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class PantallaConfiguracion extends Pantalla {


    private final Juego juego;


    private Stage escenaConfiguracion;
    private  final AssetManager assetManager;

    private Texture texturaFondo;

    private float buttonSpacing = 80;


    private Preferences preferencias;
    private boolean soundOn;
    private  boolean efectsOn;


    public PantallaConfiguracion(Juego juego) {
        assetManager = new AssetManager();
        this.juego = juego;
    }



    @Override
    public void show() {
        crearObjetoPreferencias();
        cargarPreferencias();
        cargarTexturas();
        crearPantallaConfiguracion();

    }

    private void cargarPreferencias() {
        soundOn = preferencias.getBoolean("Musica",true);
        efectsOn = preferencias.getBoolean("Efectos", true);
    }

    private void crearObjetoPreferencias() {
        preferencias = Gdx.app.getPreferences("Preferencias");
    }

    private void cargarTexturas() {
        assetManager.load("fondo.png", Texture.class);
        assetManager.finishLoading();

        texturaFondo = assetManager.get("fondo.png");

    }

    private void crearPantallaConfiguracion() {
        escenaConfiguracion = new Stage();

        //boton salir
        Texture texturaBtnMenu = new Texture("Salir.png");
        TextureRegionDrawable trdMenu = new TextureRegionDrawable(new TextureRegion(texturaBtnMenu));

        //boton efectos on
        Texture texturaBtnEfectosOn = new Texture("check.png");
        TextureRegionDrawable trdEfectosOn = new TextureRegionDrawable(new TextureRegion(texturaBtnEfectosOn));

        //boton efectos off
        Texture textureBtnEfectosOff = new Texture("cross.png");
        TextureRegionDrawable trdEfectosOff = new TextureRegionDrawable(new TextureRegion(textureBtnEfectosOff));

        //boton musica on
        Texture textureBtnMusicaOn = new Texture("check.png");
        TextureRegionDrawable trdMusicaOn = new TextureRegionDrawable(new TextureRegion(textureBtnMusicaOn));

        //Boton musica off
        Texture textureBtnMusicaOff = new Texture("cross.png");
        TextureRegionDrawable trdMusicaOff = new TextureRegionDrawable(new TextureRegion(textureBtnMusicaOff));

        final ImageButton btnMenu = new ImageButton(trdMenu);
        final ImageButton btnEfectosOn = new ImageButton(trdEfectosOn);
        final ImageButton btnEfectosOff = new ImageButton(trdEfectosOff);
        final ImageButton btnMusicOn = new ImageButton(trdMusicaOn);
        final ImageButton btnMusicOff = new ImageButton(trdMusicaOff);

        float  xButtonEfect = ANCHO/2 - ANCHO/5;
        float  yButtonEfeect = ALTO/2-100;

        float xButtonMusic = ANCHO/2 - ANCHO/5;
        float yButtonMusic = ALTO/2 -250;

        btnEfectosOn.setPosition(xButtonEfect,yButtonEfeect);
        btnEfectosOff.setPosition(xButtonEfect,yButtonEfeect);

        //elegir qué imagen debe ponerse
        if(preferencias.getBoolean("Efectos")){
            btnEfectosOn.setVisible(true);
            btnEfectosOff.setVisible(false);
        }else{
            btnEfectosOn.setVisible(false);
            btnEfectosOff.setVisible(true);
        }

        if (preferencias.getBoolean("Musica")){
            btnMusicOn.setVisible(true);
            btnMusicOff.setVisible(false);
        }else{
            btnMusicOff.setVisible(true);
            btnEfectosOn.setVisible(false);
        }

        btnMusicOn.setPosition(xButtonMusic,yButtonMusic);
        btnMusicOff.setPosition(xButtonMusic,yButtonMusic);

        btnMenu.setPosition(80,ALTO-40-btnMenu.getHeight());

        //listener regresar al menú
        btnMenu.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                juego.setScreen(new PantallaMenu(juego));
            }
        });

        //Listener efectos on (se apaga)
        btnEfectosOn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                btnEfectosOn.setVisible(false);
                btnEfectosOff.setVisible(true);
                preferencias.putBoolean("Efectos", false);
                preferencias.flush();
            }
        });
        //Listener efectos off (se prende)
        btnEfectosOff.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                btnEfectosOn.setVisible(true);
                btnEfectosOff.setVisible(false);
                preferencias.putBoolean("Efectos", true);
                preferencias.flush();
            }
        });

        //Listener musica On (se apaga)
        btnMusicOn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                btnMusicOn.setVisible(false);
                btnMusicOff.setVisible(true);
                preferencias.putBoolean("Musica", false);
                juego.musicaMenu.stop();
                preferencias.flush();
            }
        });

        //listener musica off (se prende)
        btnMusicOff.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                btnMusicOff.setVisible(false);
                btnMusicOn.setVisible(true);
                preferencias.putBoolean("Musica", true);
                juego.musicaMenu.play();
                juego.musicaMenu.setLooping(true);
                preferencias.flush();
            }
        });

        escenaConfiguracion.addActor(btnEfectosOn);
        escenaConfiguracion.addActor(btnEfectosOff);
        escenaConfiguracion.addActor(btnMusicOn);
        escenaConfiguracion.addActor(btnMusicOff);
        escenaConfiguracion.addActor(btnMenu);


        Gdx.input.setInputProcessor(escenaConfiguracion);


    }

    @Override
    public void render(float delta) {

        borrarPantalla();
        batch.setProjectionMatrix(camara.combined);
        batch.begin();

        batch.draw(texturaFondo,0,0);
        batch.end();
        escenaConfiguracion.draw();
        batch.begin();
        dibujarTexto();
        batch.end();



    }

    private void dibujarTexto() {
        Texto configText = new Texto("fontScore.fnt");
        String textoEfectos = "Efectos ";
        String textoMusic = "Musica ";
        configText.render(batch, textoEfectos, ANCHO/2 - ANCHO/5,ALTO/2-buttonSpacing);
        configText.render(batch, textoMusic, ANCHO/2 - ANCHO/5,ALTO/2 - 2*buttonSpacing);


    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
