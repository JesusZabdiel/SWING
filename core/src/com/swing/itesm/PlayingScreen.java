package com.swing.itesm;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;

class PlayingScreen extends Pantalla {

    private final Juego juego;

    //jugability
    private float velocity = 4;


    //Textures
    private Texture backTexture;
    private Texture playerTexture;

    //Objects
    private Background background;
    private Background backgroundD;
    private  Player player;
    public Estado estado;
    private static int tempEstado;
    private Personaje personaje;
    private Texture texturaPersonaje;


    public PlayingScreen(Juego juego) {
        this.juego = juego;
    }

    @Override
    public void show() {
        cargarTexturas();
        createBackground();
        createPlayer();
        iniciarPersonaje();


        Gdx.input.setInputProcessor(new ProcesadorEntrada());

        

    }

    private void iniciarPersonaje() {
        personaje = new Personaje(texturaPersonaje);
        resetTempEstado();
        estado = Estado.CORRIENDO_ABAJO;
    }

    private void createBackground() {
        background = new Background(backTexture,0,0);
        backgroundD = new Background(backTexture,background.sprite.getX()+background.sprite.getWidth(),0);

    }
    
    private void createPlayer(){
        player =  new Player(playerTexture,50,200);
        estado = Estado.CORRIENDO_ABAJO;
    }



    private void cargarTexturas() {

        backTexture = new Texture("PantallaJuego.jpg");
        playerTexture = new Texture("redCircle.png");
        texturaPersonaje = new Texture("ninja.png");
    }

    @Override
    public void render(float delta) {
        //Update
        moveBackgound();

        tempEstado +=1;
        estado = personaje.mover(estado, tempEstado);

        borrarPantalla();
        batch.setProjectionMatrix(camara.combined);
        batch.begin();
        batch.draw(playerTexture,ANCHO/2f,ALTO/2f);
        background.render(batch);
        backgroundD.render(batch);
        player.render(batch);
        personaje.render(batch);
        batch.end();



    }

    private void moveBackgound() {

        if(background.sprite.getX()+background.sprite.getWidth() == 0){
            background.sprite.setX(0);

        }
        background.sprite.setX(background.sprite.getX()-velocity);
        backgroundD.sprite.setX(background.sprite.getX()+background.sprite.getWidth());

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

    public static void resetTempEstado() {
        tempEstado = 0;
    }
    public enum Estado{
        CORRIENDO_ABAJO,
        CORRIENDO_ARRIBA,
        SALTANDO,
        BAJANDO,
        GANCHO_ARRIBA,
        GANCHO_ABAJO
    }

    private class ProcesadorEntrada implements InputProcessor {
        //Si el personaje esta corriendo entonces salta y prepara el gancho
        //Si el personaje esta en el techo, se deja caer y prepara el gancho
        @Override
        public boolean keyDown(int keycode) {
            if (estado == Estado.CORRIENDO_ABAJO) {
                estado = Estado.SALTANDO;
                resetTempEstado();
                return true;
            }
            if (estado == Estado.CORRIENDO_ARRIBA) {
                estado = Estado.BAJANDO;
                resetTempEstado();
                return true;
            } else {
                return false;
            }
        }


        //El personaje lanza el gancho ya sea ariba o abajo dependiendo de las condiciones
        @Override
        public boolean keyUp(int keycode) {

            if (estado == Estado.CORRIENDO_ABAJO || estado == Estado.GANCHO_ABAJO || estado == Estado.SALTANDO || estado == Estado.BAJANDO) {
                estado = Estado.GANCHO_ARRIBA;
                resetTempEstado();
                return true;
            }
            if (estado == Estado.CORRIENDO_ARRIBA || estado == Estado.GANCHO_ARRIBA) {
                estado = Estado.GANCHO_ABAJO;
                resetTempEstado();
                return true;
            }
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
