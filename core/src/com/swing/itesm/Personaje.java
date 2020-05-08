package com.swing.itesm;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.swing.itesm.PantallaPlay.Estado;


public class Personaje {
    private Animation animacionTrazo;
    private Animation animacionColor;
    public Sprite color;
    public Sprite sprite;
    private TextureRegion[][] texturaPersonaje;
    private TextureRegion[][] colorPersonaje;
    private float timerAnimacion;
    private final float fakeRoof = Juego.ALTO-30;
    private final float floor = 70;
    private Estado estado = Estado.IDLE;
    private float x, y;
    private boolean giro = false, enElAire=false;
    private int gravedad = 90;
    private float timerMovimiento;
    private boolean invulnerabilidad;


    public Personaje(Texture textura, Texture relleno, Color chroma, Estado estado){
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
        color.setColor(chroma);

        this.x=50;
        this.y=floor;
        sprite.setPosition(x,y);
        color.setPosition(x,y);
        this.estado = estado;
        this.invulnerabilidad = false;

    }

    public void render(SpriteBatch batch){
        if (estado == Estado.IDLE){
            color.setRegion(colorPersonaje[0][0]);
            sprite.setRegion(texturaPersonaje[0][0]);

        }else if (estado == Estado.CORRIENDO){
            TextureRegion regionColor = (TextureRegion)animacionColor.getKeyFrame(timerAnimacion);
            TextureRegion regionTrazo = (TextureRegion)animacionTrazo.getKeyFrame(timerAnimacion);
            color.setRegion(regionColor);
            sprite.setRegion(regionTrazo);


        }else if (estado==Estado.SALTANDO) {
            color.setRotation(color.getRotation()+5);
            sprite.setRotation(sprite.getRotation()+5);
            if (sprite.getRotation()>170){
                estado=Estado.CAYENDO;
            }
            color.setRegion(colorPersonaje[1][0]);
            sprite.setRegion(texturaPersonaje[1][0]);

        }else if (estado==Estado.CAYENDO){
            color.setRotation(0);
            sprite.setRotation(0);

            color.setRegion(colorPersonaje[1][1]);
            sprite.setRegion(texturaPersonaje[1][1]);

        }else if(estado==Estado.SUBIENDO){
            color.setRegion(colorPersonaje[1][2]);
            sprite.setRegion(texturaPersonaje[1][2]);
            color.setRotation(color.getRotation()+5);
            sprite.setRotation(sprite.getRotation()+5);
            if (sprite.getRotation()>80){
                estado = Estado.SALTANDO;
            }

        }else if (estado==Estado.BAJANDO){
            color.setRegion(colorPersonaje[1][3]);
            sprite.setRegion(texturaPersonaje[1][3]);
        }

        color.draw(batch);
        sprite.draw(batch);
    }


    public void moverPersonaje(float delta) {
        timerAnimacion+= Gdx.graphics.getDeltaTime();
        timerMovimiento+= delta;

        if(enElAire==false && giro==false) {
            estado = Estado.CORRIENDO;
            color.setY(floor);
            sprite.setY(floor);
        }else if (enElAire==false && giro==true) {
            estado = Estado.CORRIENDO;
            color.setY(fakeRoof-color.getHeight());
            sprite.setY(fakeRoof-sprite.getHeight());
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

    public void giro() {
        timerMovimiento = 0;
        gravedad = gravedad * -1;
        enElAire = true;
        estado=Estado.SUBIENDO;

    }

    public void setInvulnerabilidad(boolean invulnerabilidad) {
        this.invulnerabilidad = invulnerabilidad;
    }

    public boolean isInvulnerable() {
        return invulnerabilidad;
    }
}
