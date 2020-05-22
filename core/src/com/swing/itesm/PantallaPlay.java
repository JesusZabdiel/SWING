package com.swing.itesm;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
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
    private float speedAuxiliar;
    public final float CONSTANT_VIDA = 4;
    public int vidaPorSegundo = 10;
    private final int AUMENTO_VIDA = 4;
    private  final int MIN_VIDA_SEGUNDO = 4;
    private float vidaJugador;
    private final float DAÑO_POR_ITEM = 7;
    float barraVidaDimentions;
    private int score;
    private final float AUMENTO_VELOCIDAD = .01f;
    private final float SPEED_LENTA = 2;
    private float tiempoControlarVida;
    private final float TIEMPO_REDUCIR_VIDA = 12;
    private float tiempoItemRalentizacion = 0;
    private float tiempoItemInvulnerable = 0;
    private static final float DURACION_ITEM = 5;//valor en segundos
    private boolean juegoRalentizado = false;

    //efectos sonido
    private Sound efectoGancho;
    private Sound efectoCorrer;
    private Sound efectoMuerte;
    private Sound efectoSalud;
    private Sound efectoEscudo;
    private Sound efectoGolpe;
    private Sound efectoRalentizar;

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
    private Texture texturaReloj;

    //Background
    private Escenario escenario;
    private Texture backGround6, backGround5, backGround4, backGround3, backGround2, backGround1;


    //Objects
    //private  Player player;
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
    private Preferences prefPersonaje;


    public PantallaPlay(Juego juego) {
        this.juego = juego;
    }



    @Override
    public void show() {
        prefPersonaje = Gdx.app.getPreferences("Preferencias Customize");
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
        for (int i = 0; i<3; i++) {
            items.add(new Daño(texturaDaño));
        }
        items.add(new Ralentizacion(texturaReloj));
        items.add(new Invulnerbilidad(texturaInvulnerable));
    }


    private void crearEscenario() {
        escenario = new Escenario(backGround1,backGround2,backGround3,backGround4,backGround5,backGround6);
        speed = 7;
    }


    public void update(float delta) {
        escenario.mover(speed);

        moverVidas();
        moverItems();
        generarItemsAleatorios();
        controlarInvulnerabilidad(delta);
        controlarRalentizacion(delta);
        verificarColisionesVida();
        verficarColsionesItems(delta);
        restarVida(delta);
        aumentarPuntos(delta);
        aumentarVelocidad();
        controlarGeneradorVida(delta);
        verificarFinDeJuego();

    }

    private void controlarGeneradorVida(float delta) {
        tiempoControlarVida += delta;
        if (tiempoControlarVida >= TIEMPO_REDUCIR_VIDA){
            if (vidaConstante.get(vidaConstante.size-1).sprite.getX() > ANCHO
            && vidaPorSegundo > MIN_VIDA_SEGUNDO){
                vidaConstante.pop();
                tiempoControlarVida = 0;
                vidaPorSegundo--;
            }
        }
    }

    private void verficarColsionesItems(float delta) {
        for (Item item:items) {
            Rectangle rectItem = item.sprite.getBoundingRectangle();
            Rectangle rectPlayer = personaje.sprite.getBoundingRectangle();
            if (rectPlayer.overlaps(rectItem)) {
                item.setVisible(false);
                item.generarPosicionItem();
                if (item instanceof Daño){
                    restarVidaDano();

                }else if (item instanceof Invulnerbilidad){
                    personaje.setInvulnerabilidad(true);
                    efectoEscudo.play();
                    //Si agarra otro item de invulnerabilidad antes que se acabe el tiempo, el tiempo se reincia
                    tiempoItemInvulnerable = 0;
                }else{
                    tiempoItemRalentizacion = 0;
                    personaje.setRelentizado(true);
                    efectoRalentizar.play();
                }
            }
        }
    }

    private void restarVidaDano() {
        if (!personaje.isInvulnerable()){
            vidaJugador -= DAÑO_POR_ITEM;
            efectoGolpe.play();
        }
    }

    private void setLowSpeed() {
        juegoRalentizado = true;
        speedAuxiliar = speed;
        speed = SPEED_LENTA;
    }

    private void controlarInvulnerabilidad(float delta) {
        if (personaje.isInvulnerable()) {
            tiempoItemInvulnerable += delta;

            if (tiempoItemInvulnerable >= DURACION_ITEM) {
                personaje.setInvulnerabilidad(false);
                tiempoItemInvulnerable = 0;
            }
        }
    }


    private void controlarRalentizacion(float delta){
        if (personaje.isRelentizado()) {
            tiempoItemRalentizacion += delta;

            if (tiempoItemRalentizacion >= DURACION_ITEM) {
                personaje.setRelentizado(false);
                tiempoItemRalentizacion = 0;
            }
        }
    }

    private void generarItemsAleatorios() {
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
                marcador.marcar(6);
                efectoSalud.play();
                vida.generarPosicionItem();
                if (vidaJugador <= 100 - AUMENTO_VIDA){
                    aumentarVida();
                }
            }
        }
    }

    private void aumentarVida() {
        vidaJugador += AUMENTO_VIDA;
    }



    private void crearVidaConstante() {
        vidaConstante = new Array<>();
        for (int i = 0; i<vidaPorSegundo; i++){
            vidaConstante.add(new Vida(texturaVida));
        }
    }


    private void iniciarPersonaje() {
        Color chroma = Juego.colores.get(prefPersonaje.getInteger("ColorPersonaje"));
        personaje = new Personaje(texturaPersonaje, rellenoPersonaje, chroma, Estado.CORRIENDO);
        efectoCorrer.loop();
        personaje.sprite.setScale(.7f);
        personaje.color.setScale(.7f);
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
        efectoEscudo=manager.get("escudo2.mp3");
        efectoGolpe=manager.get("golpe.mp3");
        efectoRalentizar=manager.get("ralentizar.mp3");

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
        texturaInvulnerable = manager.get("invulnerable_Small.png");
        texturaReloj = manager.get("reloj.png");
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
        batch.draw(barraVidaBack, Pantalla.ANCHO-barraVida.getWidth()-150,Pantalla.ALTO - barraVida.getHeight()-25);

        if(vidaJugador >=0){
            batch.draw(barraVida,Pantalla.ANCHO-barraVida.getWidth()-150,
                    Pantalla.ALTO - barraVida.getHeight()-25,barraVidaDimentions,barraVida.getHeight());
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
        batch.draw(texturaBtnPause, ANCHO-100,ALTO-texturaBtnPause.getHeight(), 56,74);
        escenario.renderFront(batch);
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
        if (!personaje.isInvulnerable()) {
            vidaJugador -= CONSTANT_VIDA * delta;
        }
        barraVidaDimentions = 350 / 100f * vidaJugador;
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
        manager.unload("escudo2.mp3");
        manager.unload("golpe.mp3");
        manager.unload("ralentizar.mp3");
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

            if(v.x >=ANCHO-100 && v.x <= texturaBtnPause.getWidth()+ANCHO-100
                    && v.y <= ALTO && v.y >= ALTO-texturaBtnPause.getHeight()){
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
            efectoEscudo.pause();
            efectoRalentizar.pause();
            Texture texturaFondoGameOver = new Texture("negro.png");
            Image imgGameOver = new Image(texturaFondoGameOver);
            imgGameOver.setColor(0,0,0,0.7f);
            imgGameOver.setPosition(0,0);


            // Boton Jugar
            Texture texturaBtnJugar = new Texture("Reanudar.png");
            TextureRegionDrawable trdJugar = new TextureRegionDrawable(new TextureRegion(texturaBtnJugar));


            ImageButton btnJugar = new ImageButton(trdJugar);

            btnJugar.setPosition(ANCHO/2-(btnJugar.getWidth())/2,2*ALTO/3);

            // Boton Menu
            Texture texturaBtnMenu = new Texture("Salir.png");
            TextureRegionDrawable trdMenu = new TextureRegionDrawable(new TextureRegion(texturaBtnMenu));

            ImageButton btnMenu = new ImageButton(trdMenu);

            btnMenu.setPosition(ANCHO/2-btnMenu.getWidth()/2,2*ALTO/3-234);


            this.addActor(imgGameOver);
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
            efectoEscudo.stop();
            efectoRalentizar.stop();
            efectoMuerte.play();
            Texture texturaFondoGameOver = new Texture("negro.png");
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

            btnMenu.setPosition(ANCHO/2-btnMenu.getWidth()/2,2*ALTO/3-234);

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
