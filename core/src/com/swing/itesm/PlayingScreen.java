package com.swing.itesm;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;

class PlayingScreen extends Pantalla {


    private final Juego juego;

    //jugability
    public static float speed = 4;
    public final float CONSTANT_VIDA = 4;
    public int vidaPorSegundo = 10;
    private int aumentoVida = 4;
    private float vidaJugador;
    float barraVidaDimentions;
    private float score;
    private final float AUMENTO_VELOCIDAD = .01f;

    //efectos sonido
    private Sound efectoGancho;
    private Sound efectoCorrer;

    //Textures
    private Texture backTexture;
    private Texture texturaPersonaje;
    private Texture rellenoPersonaje;
    private Texture texturePowerUp;
    private Texture barraVidaBack;
    private Texture barraVida;
    private Texture texturaBtnPause;


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

    //Game Over
    private EscenaGameOver escenaGameOver;



    public PlayingScreen(Juego juego) {
        this.juego = juego;
    }



    @Override
    public void show() {
        estadoJuego = EstadoJuego.JUGANDO;
        cargarTexturas();
        createBackground();
        iniciarPersonaje();
        crearPowerUps();
        crearMarcador();

       Gdx.input.setInputProcessor(new ProcesadorEntrada());

    }


    public void update(float delta) {
        moveBackgound();
        moverVidas();
        restarVida(delta);
        verificarColisiones();
        aumentarPuntos(delta);
        aumentarVelocidad();
        verificarFinDeJuego();

    }


    private void aumentarVelocidad() {
        if (score>0 && score % 5 == 0){
            speed += AUMENTO_VELOCIDAD;
        }

    }

    private void verificarFinDeJuego() {
        if(vidaJugador <= 0){
            estadoJuego = EstadoJuego.PERDIO;
            escenaGameOver = new EscenaGameOver(vista, batch);
            Gdx.input.setInputProcessor(escenaGameOver);
            tempEstado=0;
        }
    }

    private void crearMarcador() {
        this.marcador = new Marcador(Pantalla.ANCHO/2,Pantalla.ALTO-50);
        this.score = 0;

    }

    private void verificarColisiones() {
        for (int i = vidaConstante.size-1; i >= 0; i--) {
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



    private void crearPowerUps() {
        vidaConstante = new Array<>();
        for (int i = 0; i<vidaPorSegundo; i++){
            vidaConstante.add(new PowerUp(texturePowerUp));
        }
    }

    private void iniciarPersonaje() {
        color = azul;
        personaje = new Personaje(texturaPersonaje, rellenoPersonaje, color);
        AssetManager manager2=new AssetManager();
        manager2.load("correr.mp3",Sound.class);
        manager2.finishLoading();
        efectoCorrer=manager2.get("correr.mp3");
        resetTempEstado();
        estadoPersonaje = Estado.CORRIENDO_ABAJO;
        efectoCorrer.loop();
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
        texturaBtnPause = new Texture("pause.png");
    }

    @Override
    public void render(float delta) {
        //Update
        if (estadoJuego == EstadoJuego.JUGANDO){
            update(delta);
        }

        if (estadoJuego == EstadoJuego.JUGANDO) {
            tempEstado += 1;
            estadoPersonaje = personaje.mover(estadoPersonaje, tempEstado);
        }

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
        batch.draw(texturaBtnPause, 0,ALTO-texturaBtnPause.getHeight());
        batch.end();


        if(estadoJuego == EstadoJuego.PAUSADO){
            escenaPausa.draw();
        }

        if(estadoJuego == EstadoJuego.PERDIO){
            escenaGameOver.draw();
        }

    }

    private void aumentarPuntos(float delta) {
        marcador.marcar(delta);
        this.score = marcador.getScore();
    }

    private void restarVida(float delta) {
        vidaJugador -= CONSTANT_VIDA*delta;
        barraVidaDimentions = 350/100f * vidaJugador;
    }

    private void moverVidas() {
        for (PowerUp pUp: vidaConstante ) {
            pUp.mover();
        }
    }

    private void moveBackgound() {

        if(background.sprite.getX()+background.sprite.getWidth() <= 0){
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
                    AssetManager manager=new AssetManager();
                    manager.load("gancho.wav",Sound.class);
                    manager.finishLoading();
                    efectoGancho=manager.get("gancho.wav");
                    estadoPersonaje = Estado.GANCHO_ARRIBA;
                    efectoCorrer.pause();
                    efectoGancho.play();
                    resetTempEstado();
                    efectoCorrer.loop();
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
            Vector3 v = new Vector3(screenX,screenY,0);
            camara.unproject(v);
            if(v.x >=0 && v.x <= texturaBtnPause.getWidth()
                    && v.y <= ALTO && v.y >= ALTO-texturaBtnPause.getHeight()){
                estadoJuego = EstadoJuego.PAUSADO;
                escenaPausa = new EscenaPausa(vista, batch);
                Gdx.input.setInputProcessor(escenaPausa);
                tempEstado=0;

            }
            return true;
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

    // Clase pausa ( ventana que se muestra cuando el usuario pausa la app)

    class EscenaPausa extends Stage{


        public EscenaPausa (Viewport vista, SpriteBatch batch){

            super(vista, batch);

            //Pixmap pixmap = new Pixmap((int)(ANCHO*0.7f), (int)(ALTO*0.8f), Pixmap.Format.RGBA8888);
            //pixmap.setColor(255,255,255,0.5f);
            //pixmap.fillCircle(300,300,300);
            efectoCorrer.pause();
            Texture texturaFondoPausa = new Texture("escenaPausa.png");
            Image imgPausa = new Image(texturaFondoPausa);
            imgPausa.setPosition(0,0);

            // Boton Jugar
            Texture texturaBtnJugar = new Texture("button_play.png");
            TextureRegionDrawable trdJugar = new TextureRegionDrawable(new TextureRegion(texturaBtnJugar));


            ImageButton btnJugar = new ImageButton(trdJugar);

            btnJugar.setPosition(ANCHO/2-btnJugar.getWidth()/2,2*ALTO/3);

            //this.addActor(imgPausa);
            this.addActor(btnJugar);

            //Listener
            btnJugar.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    estadoJuego = EstadoJuego.JUGANDO;
                    Gdx.input.setInputProcessor(new ProcesadorEntrada());
                    efectoCorrer.loop();
                }
            });



        }
    }

    class EscenaGameOver extends Stage{


        public EscenaGameOver (Viewport vista, SpriteBatch batch){

            super(vista, batch);
            efectoCorrer.stop();
            Texture texturaFondoGameOver = new Texture("gameOver.jpg");
            Image imgGameOver = new Image(texturaFondoGameOver);
            imgGameOver.setPosition(0,0);

            // Boton Jugar
            Texture texturaBtnJugar = new Texture("button_play.png");
            TextureRegionDrawable trdPlay = new TextureRegionDrawable(new TextureRegion(texturaBtnJugar));

            ImageButton btnPlay = new ImageButton(trdPlay);

            btnPlay.setPosition(ANCHO/2-btnPlay.getWidth()/2,2*ALTO/3);

            // Boton Menu
            Texture texturaBtnMenu = new Texture("button_menu.png");
            TextureRegionDrawable trdMenu = new TextureRegionDrawable(new TextureRegion(texturaBtnMenu));

            ImageButton btnMenu = new ImageButton(trdMenu);

            btnMenu.setPosition(ANCHO/2-btnPlay.getWidth()/2,2*ALTO/3-234);

            this.addActor(imgGameOver);
            this.addActor(btnPlay);
            this.addActor(btnMenu);

            //Listener  Play
            btnPlay.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    juego.setScreen(new PlayingScreen(juego));

                }
            });

            //Listener Menu
            btnMenu.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    juego.setScreen(new PantallaMenu(juego));

                }
            });


        }
    }

    private enum EstadoJuego {

        JUGANDO,
        PAUSADO,
        PERDIO,
    }



}
