package com.swing.itesm;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.swing.itesm.PantallaPlay.Estado;


public class Personaje {

    private Juego juego;

    private Preferences preferences;

    private boolean efectosOn;
    private Color chroma;
    private Animation animacionTrazo;
    private Animation animacionColor;
    public Sprite color;
    public Sprite sprite;
    private TextureRegion[][] texturaPersonaje;
    private TextureRegion[][] colorPersonaje;
    private float timerAnimacion;
    private final float fakeRoof = Juego.ALTO-30;
    private final float floor = 30;
    private Estado estado = Estado.IDLE;
    private float x, y;
    private boolean giro = false, enElAire=false;
    private int gravedad = 30;
    private float timerMovimiento;
    private boolean invulnerabilidad, relentizado;

    //Asset manager
    private AssetManager manager;

    //Efectos de sonido
    private Sound efectoGancho;


    public Personaje(Texture textura, Texture relleno, Color chroma, Estado estado, Juego juego){



        this.juego = juego;
        manager = juego.getAssetManager();

        TextureRegion regionTrazo = new TextureRegion(textura);
        TextureRegion regionColor = new TextureRegion(relleno);

        texturaPersonaje = regionTrazo.split(150,190);
        colorPersonaje = regionColor.split(150,190);

        animacionTrazo = new Animation(0.08f, texturaPersonaje[0][1],texturaPersonaje[0][2],texturaPersonaje[0][3],
                texturaPersonaje[0][4],texturaPersonaje[0][5],texturaPersonaje[0][6],texturaPersonaje[0][7],texturaPersonaje[0][8]);
        animacionColor = new Animation(0.08f, colorPersonaje[0][1],colorPersonaje[0][2],colorPersonaje[0][3],
                colorPersonaje[0][4],colorPersonaje[0][5],colorPersonaje[0][6],colorPersonaje[0][7],colorPersonaje[0][8]);
        animacionTrazo.setPlayMode(Animation.PlayMode.LOOP);
        animacionColor.setPlayMode(Animation.PlayMode.LOOP);
        timerAnimacion = 0;

        sprite = new Sprite(texturaPersonaje[0][0]);
        color = new Sprite(colorPersonaje[0][0]);

        this.x=50;
        this.y=floor;
        sprite.setPosition(x,y);
        color.setPosition(x,y);
        this.estado = estado;
        this.chroma = chroma;
        this.invulnerabilidad = false;

        cargarPreferencias();
        verificarRecursos();

        //Tomo el assetmanager de PantallaCargando y uso sus efectos aqui
        this.efectoGancho = manager.get("salto3.mp3",Sound.class);


    }

    private void cargarPreferencias() {
        preferences = Gdx.app.getPreferences("Preferencias");
        efectosOn = preferences.getBoolean("Efectos", true);
    }

    private void verificarRecursos() {
        try {
            //this.efectoCorrer = manager.get("correr5.mp3",Music.class);
            this.efectoGancho = manager.get("salto3.mp3",Sound.class);
            //this.efectoEscudo = manager.get("escudo5.mp3",Sound.class);
            //this.efectoRalentizar = manager.get("ralentizacion.mp3",Sound.class);
        }catch (Exception ex){
            manager.load("correr5.mp3", Music.class);
            manager.load("salto3.mp3",Sound.class);
            manager.load("muerte.mp3",Sound.class);
            manager.load("salud.mp3",Sound.class);
            manager.load("escudo6.mp3",Sound.class);
            manager.load("golpe2.mp3",Sound.class);
            manager.load("ralentizar3.mp3",Sound.class);
            manager.finishLoading();
        }
    }

    public void render(SpriteBatch batch){

        color.setColor(chroma);

        if (estado == Estado.IDLE){
            color.setRegion(colorPersonaje[0][0]);
            sprite.setRegion(texturaPersonaje[0][0]);

        }else if (estado == Estado.CORRIENDO){
            color.setRotation(0);
            sprite.setRotation(0);
            TextureRegion regionColor = (TextureRegion)animacionColor.getKeyFrame(timerAnimacion);
            TextureRegion regionTrazo = (TextureRegion)animacionTrazo.getKeyFrame(timerAnimacion);
            color.setRegion(regionColor);
            sprite.setRegion(regionTrazo);


        }else if (estado==Estado.SALTANDO) {
            color.setRotation(-30);
            sprite.setRotation(-30);
            color.setRegion(colorPersonaje[1][0]);
            sprite.setRegion(texturaPersonaje[1][0]);

        }else if (estado==Estado.CAYENDO){
            color.setRegion(colorPersonaje[1][1]);
            sprite.setRegion(texturaPersonaje[1][1]);

        }else if(estado==Estado.SUBIENDO){
            float sacudida = (float)Math.random()*4;
            color.setRotation(-30+sacudida);
            sprite.setRotation(-30+sacudida);
            color.setRegion(300,190,150,200);
            sprite.setRegion(300,190,150,200);
        }else if (estado==Estado.BAJANDO){
            color.setRotation(-30);
            sprite.setRotation(-30);
            color.setRegion(colorPersonaje[1][3]);
            sprite.setRegion(texturaPersonaje[1][3]);
        }

        color.draw(batch);
        sprite.draw(batch);
    }


    public void moverPersonaje(float delta) {
        timerAnimacion+= delta;
        timerMovimiento+= delta;
        if(relentizado){
            timerMovimiento = timerMovimiento/1.08f;
        }


        if(!enElAire && !giro) {
            estado = Estado.CORRIENDO;
            color.setY(floor);
            sprite.setY(floor);
            gravedad = 90;

        }else if (!enElAire && giro) {
            estado = Estado.SUBIENDO;
            color.setY(fakeRoof-color.getHeight());
            sprite.setY(fakeRoof-sprite.getHeight());
            gravedad = -90;

        }else if (enElAire==true && giro == false){
            color.setPosition(color.getX(), -gravedad * (float)Math.pow(timerMovimiento,2) + color.getY());
            sprite.setPosition(sprite.getX(), -gravedad * (float)Math.pow(timerMovimiento,2) + sprite.getY());
            if (sprite.getY()+sprite.getHeight()>fakeRoof){
                giro = true;
                enElAire=false;
            }else if (sprite.getY()<floor){
                giro = false;
                enElAire=false;
            }
        }else if (enElAire==true && giro == true){
            color.setPosition(color.getX(), -gravedad * (float)Math.pow(timerMovimiento,2) + color.getY());
            sprite.setPosition(sprite.getX(), -gravedad * (float)Math.pow(timerMovimiento,2) + sprite.getY());
            if (sprite.getY()<floor){
                giro = false;
                enElAire=false;
            }else if (sprite.getY()+sprite.getHeight()>fakeRoof){
                giro = true;
                enElAire=false;
            }
        }
    }

    //Cuando salta o baja
    public void giro() {
        timerMovimiento = 0;
        gravedad = gravedad * -1;
        enElAire = true;
        estado = Estado.SUBIENDO;
        if(preferences.getBoolean("Efectos",true)){
            efectoGancho.play();
        }

    }

    public void setInvulnerabilidad(boolean invulnerabilidad) {
        //Cuando es invulnerable
        if((this.invulnerabilidad = invulnerabilidad)==true){
            this.invulnerabilidad=invulnerabilidad;
            //Cuando se acaba el efecto
        }else{
            this.invulnerabilidad=invulnerabilidad;
        }
    }

    public boolean isInvulnerable() {

        return invulnerabilidad;
    }

    public void setRelentizado(boolean relentizado){
        //Cuando se ralentiza
        if((this.relentizado = relentizado)==true){
            this.relentizado=relentizado;
            //Cuando se pasa el efecto
        }else{
            this.relentizado=relentizado;
        }

    }

    public boolean isRelentizado(){

        return relentizado;
    }

    public void setColor(Color color){
        this.chroma = color;
    }


    public boolean isCorriendo(){
        return this.estado == Estado.CORRIENDO;
    }


}
