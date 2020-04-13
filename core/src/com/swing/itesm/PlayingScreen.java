package com.swing.itesm;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;

class PlayingScreen extends Pantalla {

    private final Juego juego;

    //jugability
    public static float speed = 4;
    public int vidaPorSegundo = 10;
    private int aumentoVida = 4;
    private float vidaJugador;
    float barraVidaDimentions;
    private float score;


    //Textures
    private Texture backTexture;
    private Texture texturaPersonaje;
    private Texture rellenoPersonaje;
    private Texture texturePowerUp;
    private Texture barraVidaBack;
    private Texture barraVida;

    // Colores
    private Color amarillo = new Color(0.9764f,0.7647f,0.2078f,1);
    private Color azul = new Color(0.1529f,0.3647f,1,1);

    //Objects
    private Background background;
    private Background backgroundD;
    //private  Player player;
    public Estado estadoPersonaje;
    public  EstadoJuego  estadoJuego;
    private static int tempEstado;
    private Personaje personaje;
    private Color color;
    private Array<PowerUp> vidaConstante;
    private Marcador marcador;


    //Pausa
    private EscenaPausa escenaPausa;


    public PlayingScreen(Juego juego) {
        this.juego = juego;
    }

    /*// Menu
    private Stage escenaMenu;  // botones,....
    */

    @Override
    public void show() {
        estadoJuego = EstadoJuego.JUGANDO;
        cargarTexturas();
        createBackground();
        iniciarPersonaje();
        crearPowerUps();
        crearMarcador();


        Gdx.input.setInputProcessor(new ProcesadorEntrada());
        //crearMenu();

    }

    public void update(float delta) {
        moveBackgound();
        moverVidas();
        restarVida(delta);
        verificarColisiones();
        aumentarPuntos(delta);
        verificarFinDeJuego();

    }

    private void verificarFinDeJuego() {
        if(vidaJugador <= 0){
            estadoJuego = EstadoJuego.PERDIO;
        }
    }

    private void crearMarcador() {
        this.marcador = new Marcador(Pantalla.ANCHO/2,Pantalla.ALTO-50);
        this.score = 0;

    }

    private void verificarColisiones() {
        for (int i = vidaConstante.size-1; i > 0; i--) {
            PowerUp pUp = vidaConstante.get(i);
            Rectangle rectpUp = pUp.sprite.getBoundingRectangle();
            Rectangle rectPlayer = personaje.sprite.getBoundingRectangle();
            if (rectPlayer.overlaps(rectpUp)) {
                pUp.generarPosicionPowerUp();
                if (vidaJugador <=100 -aumentoVida ){
                    aumentarVida();
                }
            }
        }
    }

    private void aumentarVida() {
        vidaJugador += aumentoVida;
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
        vidaConstante = new Array<>();
        for (int i = 0; i<vidaPorSegundo; i++){
            vidaConstante.add(new PowerUp(texturePowerUp));
        }
    }

    private void iniciarPersonaje() {
        color = azul;
        personaje = new Personaje(texturaPersonaje, rellenoPersonaje, color);
        resetTempEstado();
        estadoPersonaje = Estado.CORRIENDO_ABAJO;
        vidaJugador = 100;
    }

    private void createBackground() {
        background = new Background(backTexture,0,0);
        backgroundD = new Background(backTexture,background.sprite.getX()+background.sprite.getWidth(),0);

    }
    
    private void cargarTexturas() {

        backTexture = new Texture("PantallaJuego.jpg");
        texturaPersonaje = new Texture("ninjaTempCont.png");
        rellenoPersonaje = new Texture("ninjaTempFill.png");
        texturePowerUp = new Texture("Life.png");
        barraVida = new Texture("lifeBar.png");
        barraVidaBack = new Texture("lifeBarBack.png");
    }

    @Override
    public void render(float delta) {
        //Update
        if (estadoJuego == EstadoJuego.JUGANDO){
            update(delta);

        }


        tempEstado +=1;
        estadoPersonaje = personaje.mover(estadoPersonaje, tempEstado);

        borrarPantalla();
        batch.setProjectionMatrix(camara.combined);
        batch.begin();
        background.render(batch);
        backgroundD.render(batch);
        personaje.render(batch);
        batch.draw(barraVidaBack, Pantalla.ANCHO-barraVida.getWidth()-15,Pantalla.ALTO - barraVida.getHeight()-15);
        if(vidaJugador >=0){
            batch.draw(barraVida,Pantalla.ANCHO-barraVida.getWidth()-15,
                    Pantalla.ALTO - barraVida.getHeight()-15,barraVidaDimentions,barraVida.getHeight());
        }




        for (PowerUp pUp: vidaConstante ) {
            pUp.render(batch);
        }

        marcador.render(batch);
        batch.end();
        //escenaMenu.draw();


        if(estadoJuego == EstadoJuego.PAUSADO){
            escenaPausa.draw();
        }

    }

    private void aumentarPuntos(float delta) {
        marcador.marcar(delta);
    }

    private void restarVida(float delta) {
        vidaJugador -= speed*delta;
        barraVidaDimentions = 350/100f * vidaJugador;
    }

    private void moverVidas() {
        for (PowerUp pUp: vidaConstante ) {
            pUp.mover();
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
                if (estadoPersonaje == Estado.CORRIENDO_ABAJO) {
                    estadoPersonaje = Estado.SALTANDO;
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

                if (estadoPersonaje == Estado.CORRIENDO_ABAJO || estadoPersonaje == Estado.GANCHO_ABAJO || estadoPersonaje == Estado.SALTANDO || estadoPersonaje == Estado.BAJANDO) {
                    estadoPersonaje = Estado.GANCHO_ARRIBA;
                    resetTempEstado();
                    return true;
                }
                if (estadoPersonaje == Estado.GANCHO_ARRIBA) {
                    estadoPersonaje = Estado.GANCHO_ABAJO;
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
