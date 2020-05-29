package com.swing.itesm;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
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
    private boolean musicOn;
    private boolean efectsOn;

    //Musica
    private Music musicaBG;

    //Efectos sonido
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
    private Texture barraVidaBloqueada;
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

    //Objeto Preferencias
    private Preferences preferencias;



    //Pausa
    private EscenaPausa escenaPausa;

    //En cond¿figuración
    private EscenaConfiguracion escenaConfiguracion;


    //Game Over
    private EscenaGameOver escenaGameOver;



    public PantallaPlay(Juego juego) {
        this.juego = juego;
    }



    @Override
    public void show() {
        estadoJuego = EstadoJuego.JUGANDO;
        manager = juego.getAssetManager();
        cargarPreferencias();
        cargarTexturas();
        crearEscenario();
        iniciarPersonaje();
        crearOjo();
        crearItems();
        crearVidaConstante();
        crearMarcador();
        Gdx.input.setInputProcessor(new ProcesadorEntrada());
        if(preferencias.getBoolean("Musica")){
            musicaBG.play();
            musicaBG.setLooping(true);
        }


    }

    private void cargarPreferencias() {
        preferencias = Gdx.app.getPreferences("Preferencias");
        musicOn = preferencias.getBoolean("Musica",true);
        efectsOn = preferencias.getBoolean("Efectos",true);
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
            if (score > preferencias.getInteger("BestScore")){
                preferencias.putInteger("BestScore", score);
                preferencias.flush();
            }
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
        Color chroma = Juego.colores.get(preferencias.getInteger("ColorPersonaje"));
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
        efectoGancho=manager.get("salto3.mp3");
        efectoMuerte=manager.get("muerte.mp3");
        efectoSalud=manager.get("salud.mp3");
        efectoEscudo=manager.get("escudo5.mp3");
        efectoGolpe=manager.get("golpe2.mp3");
        efectoRalentizar=manager.get("ralentizacion.mp3");
        musicaBG = manager.get("BGMusic.mp3");

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
        barraVidaBloqueada = manager.get("lifeBarBlock.png");
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
            if(personaje.isInvulnerable()){
                batch.draw(barraVidaBloqueada,Pantalla.ANCHO-barraVida.getWidth()-150,
                        Pantalla.ALTO - barraVida.getHeight()-25,barraVidaDimentions,barraVida.getHeight());
            }
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

        if(estadoJuego == EstadoJuego.CONFIGURACION){
            escenaConfiguracion.draw();
            batch.begin();
            dibujarTextoConfiguraciones();
            batch.end();
        }

        if(estadoJuego == EstadoJuego.PERDIO){
            escenaGameOver.draw();
            batch.begin();
            dibujarTextoScore();
            batch.end();
        }

    }

    private void dibujarTextoScore() {
        int bestScore = preferencias.getInteger("BestScore");
        Texto scoreText = new Texto("fontScore.fnt");
        String textoScore = "Score: " + score;
        String textoBestScore = "¡New Best Score!: " + score;
        if (score > bestScore){
            scoreText.render(batch, textoBestScore, ANCHO/2,ALTO-100);
        }else{
            scoreText.render(batch, textoScore, ANCHO/2,ALTO-100);
        }
    }

    private void dibujarTextoConfiguraciones() {
        Texto configText = new Texto("fontScore.fnt");
        String textoEfectos = "Efectos ";
        String textoMusic = "Musica ";
        configText.render(batch, textoEfectos, ANCHO/2-ANCHO/5,ALTO/2+50);
        configText.render(batch, textoMusic, ANCHO/2-ANCHO/5,ALTO/2 -50);


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
        manager.unload("salto3.mp3");
        manager.unload("muerte.mp3");
        manager.unload("salud.mp3");
        manager.unload("escudo5.mp3");
        manager.unload("golpe2.mp3");
        manager.unload("ralentizacion.mp3");
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
        manager.unload("lifeBarBlock.png");
        manager.unload("pause.png");
        manager.unload("Obstaculo.png");
        manager.unload("ojo.png");
        manager.unload("BGMusic.mp3");

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
        private final float BUTTON_SPACING = 100;

        public EscenaPausa (final Viewport vista, final SpriteBatch batch){

            super(vista, batch);

            efectoCorrer.pause();
            efectoEscudo.pause();
            efectoRalentizar.pause();
            Texture texturaFondoGameOver = new Texture("negro.png");
            Image imgGameOver = new Image(texturaFondoGameOver);
            imgGameOver.setColor(0,0,0,0.7f);
            imgGameOver.setPosition(0,0);
            musicaBG.setVolume(0.5f);


            // Boton Jugar
            Texture texturaBtnJugar = new Texture("Reanudar.png");
            TextureRegionDrawable trdJugar = new TextureRegionDrawable(new TextureRegion(texturaBtnJugar));
            ImageButton btnReanudar = new ImageButton(trdJugar);

            // Boton Menu
            Texture texturaBtnMenu = new Texture("Salir.png");
            TextureRegionDrawable trdMenu = new TextureRegionDrawable(new TextureRegion(texturaBtnMenu));
            ImageButton btnMenu = new ImageButton(trdMenu);

            //Boton configuracion
            Texture texturaBtnConfig = new Texture("btnConfig.png");
            TextureRegionDrawable trdBtnConfiguracion = new TextureRegionDrawable(new TextureRegion(texturaBtnConfig));
            ImageButton btnConfig = new ImageButton(trdBtnConfiguracion);


            btnReanudar.setPosition(ANCHO/2-(btnReanudar.getWidth())/2,2*ALTO/3);
            btnConfig.setPosition(ANCHO/2-(btnReanudar.getWidth())/2,btnReanudar.getY()
                    - btnConfig.getHeight() - BUTTON_SPACING);
            btnMenu.setPosition(ANCHO/2-btnMenu.getWidth()/2,btnConfig.getY()
                    - btnMenu.getHeight() - BUTTON_SPACING);



            this.addActor(imgGameOver);
            this.addActor(btnReanudar);
            this.addActor(btnMenu);
            this.addActor(btnConfig);

            //Listener
            btnReanudar.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    estadoJuego = EstadoJuego.JUGANDO;
                    Gdx.input.setInputProcessor(new ProcesadorEntrada());
                    efectoCorrer.loop();
                    musicaBG.setVolume(1);
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

            //Listener configuración
            btnConfig.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    escenaConfiguracion = new EscenaConfiguracion(vista, batch);
                    estadoJuego = EstadoJuego.CONFIGURACION;
                    Gdx.input.setInputProcessor(escenaConfiguracion);
                }
            });

        }
    }

    class EscenaGameOver extends Stage{


        public EscenaGameOver (Viewport vista, SpriteBatch batch){

            super(vista, batch);
            musicaBG.stop();
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

            btnPlay.setPosition(ANCHO/2-btnPlay.getWidth()/2,2*ALTO/4);

            // Boton Menu
            Texture texturaBtnMenu = new Texture("Salir.png");
            TextureRegionDrawable trdMenu = new TextureRegionDrawable(new TextureRegion(texturaBtnMenu));

            ImageButton btnMenu = new ImageButton(trdMenu);

            btnMenu.setPosition(ANCHO/2-btnMenu.getWidth()/2,2*ALTO/5);


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

    private class EscenaConfiguracion extends Stage {
        public EscenaConfiguracion(Viewport vista, SpriteBatch batch){
            super(vista,batch);

            Texture texturaFondoGameOver = new Texture("negro.png");
            Image imgGameOver = new Image(texturaFondoGameOver);
            imgGameOver.setColor(0,0,0,0.7f);
            imgGameOver.setPosition(0,0);

            //Botón guardar
            Texture texturaBtnSalir = new Texture("Salir.png");
            TextureRegionDrawable trdSalir = new TextureRegionDrawable(new TextureRegion(texturaBtnSalir));

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


            final ImageButton btnAtras = new ImageButton(trdSalir);
            final ImageButton btnEfectosOn = new ImageButton(trdEfectosOn);
            final ImageButton btnEfectosOff = new ImageButton(trdEfectosOff);
            final ImageButton btnMusicOn = new ImageButton(trdMusicaOn);
            final ImageButton btnMusicOff = new ImageButton(trdMusicaOff);

            float  xButtonEfect = ANCHO/2 + 50;
            float  yButtonEfeect = ALTO/2 + 50 ;
            float xButtonMusic = ANCHO/2 + 50;
            float yButtonMusic = ALTO/2 - 50 - btnMusicOn.getHeight();

            btnEfectosOn.setPosition(xButtonEfect,yButtonEfeect);
            btnEfectosOff.setPosition(xButtonEfect,yButtonEfeect);
            btnMusicOn.setPosition(xButtonMusic, yButtonMusic);
            btnMusicOff.setPosition(xButtonMusic, yButtonMusic);
            btnAtras.setPosition(80, ALTO-40-btnAtras.getHeight());

            //elegir qué imagen debe ponerse
            if(efectsOn){
                btnEfectosOn.setVisible(true);
                btnEfectosOff.setVisible(false);
            }else{
                btnEfectosOn.setVisible(false);
                btnEfectosOff.setVisible(true);
            }

            if (musicOn){
                btnMusicOn.setVisible(true);
                btnMusicOff.setVisible(false);
            }else{
                btnMusicOff.setVisible(true);
                btnEfectosOn.setVisible(false);
            }

            //listener regresar al menú
            btnAtras.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    estadoJuego = EstadoJuego.PAUSADO;
                    preferencias.flush();
                    Gdx.input.setInputProcessor(escenaPausa);
                }
            });

            //Listener efectos on (se apaga)
            btnEfectosOn.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
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
                    super.clicked(event, x, y);
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
                    super.clicked(event, x, y);
                    btnMusicOn.setVisible(false);
                    btnMusicOff.setVisible(true);
                    preferencias.putBoolean("Musica", false);
                    musicaBG.stop();
                    preferencias.flush();
                }
            });

            //listener musica off (se prende)
            btnMusicOff.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    btnMusicOff.setVisible(false);
                    btnMusicOn.setVisible(true);
                    preferencias.putBoolean("Musica", true);
                    musicaBG.play();
                    musicaBG.setLooping(true);
                    preferencias.flush();
                }
            });

            this.addActor(imgGameOver);
            this.addActor(btnEfectosOn);
            this.addActor(btnEfectosOff);
            this.addActor(btnMusicOn);
            this.addActor(btnMusicOff);
            this.addActor(btnAtras);

        }

    }

    private enum EstadoJuego {
        JUGANDO,
        PAUSADO,
        PERDIO,
        CONFIGURACION,
    }



}
