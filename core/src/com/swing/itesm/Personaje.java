package com.swing.itesm;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.swing.itesm.PlayingScreen.Estado;


public class Personaje {
    private int temp;
    public Sprite sprite;
    private int velocidad = 30;
    private int aceleracion = 2;
    private float fakeRoof = Juego.ALTO-60;

    public Personaje(Texture textura){
        temp = 0;
        sprite = new Sprite(textura,12,10,60,70);
        sprite.setBounds(60,20, 150, 150);

    }

    public Estado mover(Estado estado, int tempEstado){
        sprite.setColor((float)Math.random(),(float)Math.random(),(float)Math.random(),1);
        temp = tempEstado/3;

        switch (estado){
            case CORRIENDO_ABAJO:
                int runTemp = temp%8;
                if (runTemp==0)
                    sprite.setRegion(88,8,62,70);
                if (runTemp==1)
                    sprite.setRegion(169,8,62,70);
                if (runTemp==2)
                    sprite.setRegion(247,8,62,70);
                if (runTemp==3)
                    sprite.setRegion(327,8,62,70);
                if (runTemp==4)
                    sprite.setRegion(407,8,62,70);
                if (runTemp==5)
                    sprite.setRegion(487,8,62,70);
                if (runTemp==6)
                    sprite.setRegion(567,8,62,70);
                if (runTemp==7)
                    sprite.setRegion(648,8,62,70);

                sprite.setY(0);
                return Estado.CORRIENDO_ABAJO;
            case SALTANDO:
                sprite.setRegion(12,170,60,70);
                sprite.setY(velocidad*temp-(aceleracion*(float)(Math.pow(temp,2)))/2);
                if(velocidad*temp<(aceleracion*(float)(Math.pow(temp,2))))
                    sprite.setRegion(90,170,60,70);

                if (sprite.getY()<0) {
                    PlayingScreen.resetTempEstado();
                    return Estado.CORRIENDO_ABAJO;
                }
                return Estado.SALTANDO;
            case BAJANDO:
                sprite.setRegion(890,727,65,70);
                sprite.setY(sprite.getY()-(aceleracion*(float)(Math.pow(temp,2)))/20);

                if(sprite.getY()<=0) {
                    PlayingScreen.resetTempEstado();
                    return Estado.CORRIENDO_ABAJO;
                }
                return Estado.BAJANDO;
            case GANCHO_ABAJO:
                sprite.setRegion(400,170,65,70);
                sprite.setY(sprite.getY()-velocidad*temp/10);
                if (sprite.getY()<=0) {
                    PlayingScreen.resetTempEstado();
                    return Estado.CORRIENDO_ABAJO;
                }
                return Estado.GANCHO_ABAJO;
            case GANCHO_ARRIBA:
                sprite.setRegion(169,647,60,70);
                sprite.setY(sprite.getY()+velocidad*temp/10);
                if (sprite.getY()+sprite.getHeight()>fakeRoof){
                    PlayingScreen.resetTempEstado();
                    return Estado.BAJANDO;
                }
                return Estado.GANCHO_ARRIBA;
            case CORRIENDO_ARRIBA:
                sprite.setY(Juego.ALTO-sprite.getHeight());
                return Estado.CORRIENDO_ARRIBA;
            default:
                System.out.println("We have a problem");
                sprite.setY(0);
                return Estado.CORRIENDO_ABAJO;

        }
    }
    public void render(SpriteBatch batch){
        sprite.draw(batch);
    }
}
