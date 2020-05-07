package com.swing.itesm;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
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

class PantallaPlay extends Pantalla {


    private final Juego juego;

    //jugability
    public static float speed = 4;
    public final float CONSTANT_VIDA = 4;
    public int vidaPorSegundo = 10;
    private int aumentoVida = 4;
    private float vidaJugador;
    private final int N_OBSTACULOS = 6;
    float barraVidaDimentions;
    private float score;
    private final float AUMENTO_VELOCIDAD = .01f;
    private float tiempo;

    //efectos sonido
    private Sound efectoGancho;
    private Sound efectoCorrer;
    private Sound efectoMuerte;
    private Sound efectoSalud;

    //Textures
    private Texture texturaPersonaje;
    private Texture rellenoPersonaje;
    private Texture texturaVida;
    private Texture barraVidaBack;
    private Texture barraVida;
    private Texture texturaBtnPause;
    private Texture texturaOjo;
    private Texture texturaInvulnerable;
    private Texture texturaDaño;

    //Background
    private Escenario escenario;
    private Texture backGround6, backGround5, backGround4, backGround3, backGround2, backGround1;

    // Colores
    private Color amarillo = new Color(0.9764f,0.7647f,0.2078f,1);
    private Color azul = new Color(0.1529f,0.3647f,1,1);

    //Objects
    //private  Player player;
    public Estado estadoPersonaje;
    public  EstadoJuego  estadoJuego;
    private int tempEstado;
    private Personaje personaje;
    private Color color;
    private Array<Vida> vidaConstante;
    private Array<Item> items;
    private Marcador marcador;
    private Ojo ojo;

    //AssetManager
    private AssetManager manager;


    //Pausa
    private EscenaPausa escenaPausa;

    //Game Over
    private EscenaGameOver escenaGameOver;



    public PantallaPlay(Juego juego) {
        this.juego = juego;
    }



    @Override
    public void show() {
        estadoJuego = EstadoJuego.JUGANDO;
        manager = juego.getAssetManager();
        cargarTexturas();
        crearEscenario();
        iniciarPersonaje();
        crearOjo();
        crearItems();
        crearVidaConstante();
        crearMarcador();

        Gdx.input.setInputProcessor(new ProcesadorEntrada());

    }

    private void crearItems() {
        items = new Array<>();
        items.add(new Daño(texturaDaño));
        items.add(new Ralentizacion(texturaVida));
        items.add(new Invulnerbilidad(texturaInvulnerable));
    }


    private void crearEscenario() {
        escenario = new Escenario(backGround1,backGround2,backGround3,backGround4,backGround5,backGround6);
        speed = 4;
    }


    public void update(float delta) {
        escenario.mover(speed);
        moverVidas();
        moverItems();
        generarItemsAleatorios(delta);
        verificarColisionesVida();
        verficarColsionesItems();
        restarVida(delta);
        aumentarPuntos(delta);
        aumentarVelocidad();
        verificarFinDeJuego();

    }

    private void verficarColsionesItems() {
        for (Item item:items) {
            Rectangle rectItem = item.sprite.getBoundingRectangle();
            Rectangle rectPlayer = personaje.sprite.getBoundingRectangle();
            if (rectPlayer.overlaps(rectItem)) {
                item.generarPosicionItem();
                item.setVisible(false);
            }
        }
    }

    private void generarItemsAleatorios(float delta) {
        //que se generen cada cierto tiempo aleatorio
        //generarTiempoAleatorio(delta);
        int randomTime = (int)(Math.random()*30)+10;
        if (score != 0 && score % randomTime == 0){
            int randProbabilidad = (int)(Math.random()*99) +1;
            for (Item item: items){
                if (item.getProbabilidad() >= randProbabilidad){
                    item.setVisible(true);
                }
            }
        }
    }

    private void moverItems() {
        for (Item item: items) {
            if (item.visible) {
                item.mover();
            }
        }
    }


    private void aumentarVelocidad() {
        if (score>0 && score % 5 == 0){
            speed += AUMENTO_VELOCIDAD;
        }

        if ((score > 0 && score % 10 == 0) && vidaConstante.size >= 5){
            vidaConstante.pop();
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

    private void verificarColisionesVida() {
        for (int i = vidaConstante.size-1; i >= 0; i--) {
            Vida vida = vidaConstante.get(i);
            Rectangle rectVida = vida.sprite.getBoundingRectangle();
            Rectangle rectPlayer = personaje.sprite.getBoundingRectangle();
            if (rectPlayer.overlaps(rectVida)) {
                vida.generarPosicionItem();
                if (vidaJugador <=100 -aumentoVida ){
                    aumentarVida();
                }
            }
        }
    }

    private void aumentarVida() {
        vidaJugador += aumentoVida;
        efectoSalud.play();
    }



    private void crearVidaConstante() {
        vidaConstante = new Array<>();
        for (int i = 0; i<vidaPorSegundo; i++){
            vidaConstante.add(new Vida(texturaVida));
        }
    }


    private void iniciarPersonaje() {
        color = azul;
        personaje = new Personaje(texturaPersonaje, rellenoPersonaje, color, Estado.CORRIENDO);
        efectoCorrer.loop();
        vidaJugador = 100;
    }

    private void crearOjo(){
        ojo = new Ojo(texturaOjo, 0 ,0);
    }

    private void cargarTexturas() {
        //El asset manager no carga la textura ojo en pantalla cargando

        //Sonido
        efectoCorrer=manager.get("correr5.mp3");
        efectoGancho=manager.get("gancho.wav");
        efectoMuerte=manager.get("muerte.mp3");
        efectoSalud=manager.get("salud.mp3");

        //Texturas
        backGround1 = manager.get("layers/1.png");
        backGround2 = manager.get("layers/2.png");
        backGround3 = manager.get("layers/3.png");
        backGround4 = manager.get("layers/4.png");
        backGround5 = manager.get("layers/5.png");
        backGround6 = manager.get("layers/6.png");
        texturaPersonaje = manager.get("ninjaTrazo.png");
        rellenoPersonaje = manager.get("ninjaRelleno.png");
        texturaVida = manager.get("Life.png");
        barraVida = manager.get("lifeBar.png");
        barraVidaBack = manager.get("lifeBarBack.png");
        texturaBtnPause = manager.get("pause.png");
        texturaDaño = manager.get("Obstaculo.png");
        texturaInvulnerable = manager.get("invulnerable.png");
        texturaOjo = manager.get("ojo.png");

    }

    @Override
    public void render(float delta) {
        //Update
        if (estadoJuego == EstadoJuego.JUGANDO){
            update(delta);
        }

        if (estadoJuego == EstadoJuego.JUGANDO) {
            ojo.moverOjo(personaje.sprite.getY());
            personaje.moverPersonaje(delta);
        }

        borrarPantalla();
        batch.setProjectionMatrix(camara.combined);
        batch.begin();
        escenario.render(batch);
        personaje.render(batch);
        ojo.render(batch);
        batch.draw(barraVidaBack, Pantalla.ANCHO-barraVida.getWidth()-15,Pantalla.ALTO - barraVida.getHeight()-15);

        if(vidaJugador >=0){
            batch.draw(barraVida,Pantalla.ANCHO-barraVida.getWidth()-15,
                    Pantalla.ALTO - barraVida.getHeight()-15,barraVidaDimentions,barraVida.getHeight());
        }


        for (Vida vida: vidaConstante ) {
            vida.render(batch);
        }

        for(Item item: items){

            if (item.isVisible()){
                item.render(batch);
            }
        }

        marcador.render(batch);
        batch.draw(texturaBtnPause, 20,ALTO-texturaBtnPause.getHeight()-20);
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
        for (Vida vida: vidaConstante ) {
            vida.mover();
        }
    }


    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        manager.unload("correr5.mp3");
        manager.unload("gancho.wav");
        manager.unload("muerte.mp3");
        manager.unload("salud.mp3");
        manager.unload("layers/1.png");
        manager.unload("layers/2.png");
        manager.unload("layers/3.png");
        manager.unload("layers/4.png");
        manager.unload("layers/5.png");
        manager.unload("layers/6.png");
        manager.unload("ninjaTrazo.png");
        manager.unload("ninjaRelleno.png");
        manager.unload("Life.png");
        manager.unload("lifeBar.png");
        manager.unload("lifeBarBack.png");
        manager.unload("pause.png");
        manager.unload("Obstaculo.png");
        manager.unload("ojo.png");

    }

    public enum Estado{
        IDLE,
        CORRIENDO,
        SALTANDO,
        CAYENDO,
        SUBIENDO,
        BAJANDO,

    }

    private class ProcesadorEntrada implements InputProcessor {
        //Si el personaje esta corriendo entonces salta y prepara el gancho
        //Si el personaje esta en el techo, se deja caer y prepara el gancho
        @Override
        public boolean keyDown(int keycode) {
            return false;
        }


        //El personaje lanza el gancho ya sea ariba o abajo dependiendo de las condiciones
        @Override
        public boolean keyUp(int keycode) {
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
            if(v.x >=20 && v.x <= texturaBtnPause.getWidth()+20
                    && v.y <= ALTO-20 && v.y >= ALTO-texturaBtnPause.getHeight()-20){
                estadoJuego = EstadoJuego.PAUSADO;
                escenaPausa = new EscenaPausa(vista, batch);
                Gdx.input.setInputProcessor(escenaPausa);

            }
            return true;
        }


        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            if (estadoJuego == EstadoJuego.JUGANDO) {
                personaje.giro();
                efectoCorrer.pause();
                efectoGancho.play();
                efectoCorrer.loop();
                return true;
            }
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
            Texture texturaBtnJugar = new Texture("Reanudar.png");
            TextureRegionDrawable trdJugar = new TextureRegionDrawable(new TextureRegion(texturaBtnJugar));


            ImageButton btnJugar = new ImageButton(trdJugar);

            btnJugar.setPosition(ANCHO/2-btnJugar.getWidth()/2,2*ALTO/3);

            // Boton Menu
            Texture texturaBtnMenu = new Texture("Salir.png");
            TextureRegionDrawable trdMenu = new TextureRegionDrawable(new TextureRegion(texturaBtnMenu));

            ImageButton btnMenu = new ImageButton(trdMenu);

            btnMenu.setPosition(ANCHO/2-btnJugar.getWidth()/2,2*ALTO/3-234);


            //this.addActor(imgPausa);
            this.addActor(btnJugar);
            this.addActor(btnMenu);

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

    class EscenaGameOver extends Stage{


        public EscenaGameOver (Viewport vista, SpriteBatch batch){

            super(vista, batch);
            efectoCorrer.stop();
            efectoMuerte.play();
            Texture texturaFondoGameOver = new Texture("gameOver.jpg");
            Image imgGameOver = new Image(texturaFondoGameOver);
            imgGameOver.setPosition(0,0);

            // Boton Jugar
            Texture texturaBtnJugar = new Texture("Volver_Jugar.png");
            TextureRegionDrawable trdPlay = new TextureRegionDrawable(new TextureRegion(texturaBtnJugar));

            ImageButton btnPlay = new ImageButton(trdPlay);

            btnPlay.setPosition(ANCHO/2-btnPlay.getWidth()/2,2*ALTO/3);

            // Boton Menu
            Texture texturaBtnMenu = new Texture("Salir.png");
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
                    juego.setScreen(new PantallaCargando(juego));

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
