package com.swing.itesm;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.Viewport;

class PlayingScreen extends Pantalla {

    private final Juego juego;

    //jugability
    public static float speed = 4;


    //Textures
    private Texture backTexture;
    private Texture texturaPersonaje;
    private Texture rellenoPersonaje;
    private Texture texturePowerUp;

    // Colores
    private Color amarillo = new Color(0.9764f,0.7647f,0.2078f,1);
    private Color azul = new Color(0.1529f,0.3647f,1,1);

    //Objects
    private Background background;
    private Background backgroundD;
    //private  Player player;
    public Estado estado;
    private static int tempEstado;
    private Personaje personaje;
    private PowerUp powerUp;
    private Color color;

    //Pausa
    private EscenaPausa escenaPausa;
    private EstadoJuego estadoJuego = EstadoJuego.JUGANDO;  //Jugando, Pausado, Gano, Perdio


    public PlayingScreen(Juego juego) {
        this.juego = juego;
    }

    /*// Menu
    private Stage escenaMenu;  // botones,....
    */

    @Override
    public void show() {
        cargarTexturas();
        createBackground();
        iniciarPersonaje();
        crearPowerUps();


        Gdx.input.setInputProcessor(new ProcesadorEntrada());
        //crearMenu();

    }



    /*private void crearMenu() {

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
     */
    private void crearPowerUps() {
        this.powerUp = new PowerUp(texturePowerUp);
    }

    private void iniciarPersonaje() {
        color = azul;
        personaje = new Personaje(texturaPersonaje, rellenoPersonaje, color);
        resetTempEstado();
        estado = Estado.CORRIENDO_ABAJO;
    }

    private void createBackground() {
        background = new Background(backTexture,0,0);
        backgroundD = new Background(backTexture,background.sprite.getX()+background.sprite.getWidth(),0);

    }
    
    private void cargarTexturas() {

        backTexture = new Texture("PantallaJuego.jpg");
        //playerTexture = new Texture("redCircle.png");
        texturaPersonaje = new Texture("ninjaTempCont.png");
        rellenoPersonaje = new Texture("ninjaTempFill.png");
        texturePowerUp = new Texture("redCircle.png");
    }

    @Override
    public void render(float delta) {
        //Update
        moveBackgound();
        powerUp.mover();

        tempEstado +=1;
        estado = personaje.mover(estado, tempEstado);

        borrarPantalla();
        batch.setProjectionMatrix(camara.combined);
        batch.begin();
        background.render(batch);
        backgroundD.render(batch);
        personaje.render(batch);
        powerUp.render(batch);

        batch.end();
        //escenaMenu.draw();


        if(estadoJuego == EstadoJuego.PAUSADO){
            escenaPausa.draw();
        }


    }

    private void moveBackgound() {

        if(background.sprite.getX()+background.sprite.getWidth() == 0){
            background.sprite.setX(0);
        }
        background.sprite.setX(background.sprite.getX()-speed);
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
        SALTANDO,
        BAJANDO,
        GANCHO_ARRIBA,
        GANCHO_ABAJO,

    }

    private class ProcesadorEntrada implements InputProcessor {
        //Si el personaje esta corriendo entonces salta y prepara el gancho
        //Si el personaje esta en el techo, se deja caer y prepara el gancho
        @Override
        public boolean keyDown(int keycode) {
            if (estadoJuego == EstadoJuego.JUGANDO) {
                if (estado == Estado.CORRIENDO_ABAJO) {
                    estado = Estado.SALTANDO;
                    resetTempEstado();
                    return true;
                }
            }else {

                return false;

            }

            return false;
        }


        //El personaje lanza el gancho ya sea ariba o abajo dependiendo de las condiciones
        @Override
        public boolean keyUp(int keycode) {

            if (estadoJuego == EstadoJuego.JUGANDO) {

                if (estado == Estado.CORRIENDO_ABAJO || estado == Estado.GANCHO_ABAJO || estado == Estado.SALTANDO || estado == Estado.BAJANDO) {
                    estado = Estado.GANCHO_ARRIBA;
                    resetTempEstado();
                    return true;
                }
                if (estado == Estado.GANCHO_ARRIBA) {
                    estado = Estado.GANCHO_ABAJO;
                    resetTempEstado();
                    return true;
                }
            }
                return false;

        }

        @Override
        public boolean keyTyped(char character) {
            return false;
        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            estadoJuego = EstadoJuego.PAUSADO;
            if (escenaPausa == null) {
                escenaPausa = new EscenaPausa(vista, batch);
            }
            return true;
        }


        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            estadoJuego = EstadoJuego.JUGANDO;


            return true;
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

    // Clase pausa ( ventana que se muestra cuando el usuario pausa la app)

    class EscenaPausa extends Stage{

        public EscenaPausa (Viewport vista, SpriteBatch batch){
            super(vista, batch);

            Pixmap pixmap = new Pixmap((int)(ANCHO*0.7f), (int)(ALTO*0.8f), Pixmap.Format.RGBA8888);

            pixmap.setColor(255,255,255,0.5f);
            pixmap.fillCircle(300,300,300);
            Texture texturaCirculo = new Texture(pixmap);

            Image imgCirculo = new Image(texturaCirculo);
            imgCirculo.setPosition(ANCHO/2-pixmap.getWidth()/2,ALTO/2-pixmap.getHeight()/2);

            this.addActor(imgCirculo);
        }
    }

    private enum EstadoJuego {

        JUGANDO,
        PAUSADO,
        GANO,
        PERDIO,
    }
}
