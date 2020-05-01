package com.swing.itesm;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.swing.itesm.PlayingScreen.Estado;


public class Personaje {
    private Animation animacionTrazo;
    private Animation animacionColor;
    private Sprite color;
    public Sprite sprite;
    private TextureRegion[][] texturaPersonaje;
    private TextureRegion[][] colorPersonaje;
    private float timerAnimacion;
    private int velocidad = 30;
    private int aceleracion = 2;
    private final float fakeRoof = Juego.ALTO-15;
    private final float floor = 70;
    private Estado estado = Estado.IDLE;
    private float x, y;


    public Personaje(Texture textura, Texture relleno, Color chroma){
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

    }

    /*public Estado mover(Estado estado, int tempEstado){
        timerAnimacion = tempEstado/3;

        /*switch (estado){
            case CORRIENDO_ABAJO:
                int runTemp = timerAnimacion %8;
                if (runTemp==0) {
                    color.setRegion(226, 23, 140, 157);
                    sprite.setRegion(226, 23, 140, 157);
                }
                if (runTemp==1) {
                    color.setRegion(429, 23, 140, 157);
                    sprite.setRegion(429, 23, 140, 157);
                }
                if (runTemp==2) {
                    color.setRegion(626, 23, 140, 157);
                    sprite.setRegion(626, 23, 140, 157);
                }
                if (runTemp==3){
                    color.setRegion(831,23,140,157);
                    sprite.setRegion(831,23,140,157);
                }
                if (runTemp==4){
                    color.setRegion(1023,23,140,157);
                    sprite.setRegion(1023,23,140,157);
                }
                if (runTemp==5) {
                    color.setRegion(1224, 23, 140, 157);
                    sprite.setRegion(1224, 23, 140, 157);
                }
                if (runTemp==6) {
                    color.setRegion(1422, 23, 140, 157);
                    sprite.setRegion(1422, 23, 140, 157);
                }
                if (runTemp==7) {
                    color.setRegion(1628, 23, 140, 157);
                    sprite.setRegion(1628, 23, 140, 157);
                }

                sprite.setY(floor);
                color.setY(floor);
                return Estado.CORRIENDO_ABAJO;
            case SALTANDO:
                color.setRegion(31, 218, 122, 162);
                sprite.setRegion(31, 218, 122, 162);

                sprite.setY((velocidad* timerAnimacion -(aceleracion*(float)(Math.pow(timerAnimacion,2)))/2)+floor);
                color.setY((velocidad* timerAnimacion -(aceleracion*(float)(Math.pow(timerAnimacion,2)))/2)+floor);

                if(velocidad* timerAnimacion <(aceleracion*(float)(Math.pow(timerAnimacion,2)))) {
                    color.setRegion(226, 218, 127, 162);
                    sprite.setRegion(226, 218, 127, 162);
                } if (sprite.getY()<floor) {
                    PlayingScreen.resetTempEstado();
                    return Estado.CORRIENDO_ABAJO;
                }
                return Estado.SALTANDO;
            case BAJANDO:
                color.setRegion(226, 218, 127, 162);
                sprite.setRegion(226, 218, 127, 162);

                sprite.setY(sprite.getY()-(aceleracion*(float)(Math.pow(timerAnimacion,2)))/20);
                color.setY(color.getY()-(aceleracion*(float)(Math.pow(timerAnimacion,2)))/20);

                if(sprite.getY()<=floor) {
                    PlayingScreen.resetTempEstado();
                    return Estado.CORRIENDO_ABAJO;
                }
                return Estado.BAJANDO;
            case GANCHO_ABAJO:
                color.setRegion(626, 218, 122, 162);
                sprite.setRegion(626, 218, 122, 162);

                color.setY(sprite.getY()-velocidad* timerAnimacion /10);
                sprite.setY(sprite.getY()-velocidad* timerAnimacion /10);

                if (sprite.getY()<=floor) {
                    PlayingScreen.resetTempEstado();
                    return Estado.CORRIENDO_ABAJO;
                }
                return Estado.GANCHO_ABAJO;
            case GANCHO_ARRIBA:
                color.setRegion(429, 218, 127, 162);
                sprite.setRegion(429, 218, 127, 162);

                color.setY(color.getY()+velocidad* timerAnimacion /10);
                sprite.setY(sprite.getY()+velocidad* timerAnimacion /10);

                if (sprite.getY()+sprite.getHeight()>fakeRoof){
                    PlayingScreen.resetTempEstado();
                    return Estado.BAJANDO;
                }
                return Estado.GANCHO_ARRIBA;

            default:
                System.out.println("We have a problem");
                sprite.setY(0);
                return Estado.CORRIENDO_ABAJO;

        }
    }*/

    public void render(SpriteBatch batch, Estado estado){
        if (estado == Estado.IDLE){
            color.draw(batch);
            sprite.draw(batch);
        }else if (estado == Estado.CORRIENDO){
            TextureRegion regionColor = (TextureRegion)animacionColor.getKeyFrame(timerAnimacion);
            TextureRegion regionTrazo = (TextureRegion)animacionTrazo.getKeyFrame(timerAnimacion);
            batch.draw(regionColor,x,y);
            batch.draw(regionTrazo,x,y);
        }else if (estado==Estado.SALTANDO) {
            color.setRegion(colorPersonaje[1][0]);
            sprite.setRegion(texturaPersonaje[1][0]);

            color.draw(batch);
            sprite.draw(batch);

        }else if (estado==Estado.CAYENDO){
            color.setRegion(colorPersonaje[1][1]);
            sprite.setRegion(texturaPersonaje[1][1]);

            color.draw(batch);
            sprite.draw(batch);
        }else if(estado==Estado.SUBIENDO){
            color.setRegion(colorPersonaje[1][2]);
            sprite.setRegion(texturaPersonaje[1][2]);

            color.draw(batch);
            sprite.draw(batch);
        }else if (estado==Estado.BAJANDO){
            color.setRegion(colorPersonaje[1][3]);
            sprite.setRegion(texturaPersonaje[1][3]);

            color.draw(batch);
            sprite.draw(batch);
        }
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void moverPersonaje() {
        timerAnimacion+= Gdx.graphics.getDeltaTime();
    }
}
