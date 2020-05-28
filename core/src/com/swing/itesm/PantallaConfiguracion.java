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

public class PantallaConfiguracion extends Pantalla {


    private final Juego juego;


    private Stage escenaConfiguracion;
    private  final AssetManager assetManager;

    private Texture texturaFondo;

    private float buttonSpacing = 80;


    public PantallaConfiguracion(Juego juego) {
        assetManager = new AssetManager();
        this.juego = juego;
    }



    @Override
    public void show() {
        cargarTexturas();
        crearPantallaConfiguracion();

    }

    private void cargarTexturas() {
        assetManager.load("fondo.png", Texture.class);
        assetManager.finishLoading();

        texturaFondo = assetManager.get("fondo.png");

    }

    private void crearPantallaConfiguracion() {
        escenaConfiguracion = new Stage();

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
        btnEfectosOff.setVisible(false);
        btnMusicOn.setPosition(xButtonMusic,yButtonMusic);
        btnMusicOff.setPosition(xButtonMusic,yButtonMusic);
        btnMusicOff.setVisible(false);

        //Listener efectos on (se apaga)
        btnEfectosOn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                btnEfectosOn.setVisible(false);
                btnEfectosOff.setVisible(true);
            }
        });
        //Listener efectos off (se prende)
        btnEfectosOff.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                btnEfectosOn.setVisible(true);
                btnEfectosOff.setVisible(false);
            }
        });

        //Listener musica On (se apaga)
        btnMusicOn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                btnMusicOn.setVisible(false);
                btnMusicOff.setVisible(true);
            }
        });

        //listener musica off (se prende)
        btnMusicOff.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                btnMusicOff.setVisible(false);
                btnMusicOn.setVisible(true);
            }
        });

        escenaConfiguracion.addActor(btnEfectosOn);
        escenaConfiguracion.addActor(btnEfectosOff);
        escenaConfiguracion.addActor(btnMusicOn);
        escenaConfiguracion.addActor(btnMusicOff);


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
        Texto efecText = new Texto("fontScore.fnt");
        Texto musicText = new Texto("fontScore.fnt");
        String textoEfectos = "Efectos ";
        String textoMusic = "Musica ";
        efecText.render(batch, textoEfectos, ANCHO/2 - ANCHO/5,ALTO/2-buttonSpacing);
        efecText.render(batch, textoMusic, ANCHO/2 - ANCHO/5,ALTO/2 - 2*buttonSpacing);


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
