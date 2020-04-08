package com.swing.itesm;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.swing.itesm.PlayingScreen.Estado;


public class Personaje {
    private Sprite color;
    private int temp;
    public Sprite sprite;
    private int velocidad = 30;
    private int aceleracion = 2;
    private float fakeRoof = Juego.ALTO-60;

    public Personaje(Texture textura, Texture relleno){
        temp = 0;
        sprite = new Sprite(textura,31,23,118,157);
        color = new Sprite(relleno, 31,23,118,157);
        sprite.setBounds(60,50, 150, 150);
        color.setBounds(60,50, 150, 150);


        color.setColor(249f/255,197f/255,53f/255,1);
        //color.setColor(0.1529f,0.3647f,1,1);
    }

    public Estado mover(Estado estado, int tempEstado){
        temp = tempEstado/3;

        switch (estado){
            case CORRIENDO_ABAJO:
                int runTemp = temp%8;
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

                sprite.setY(20);
                color.setY(20);
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
        color.draw(batch);
        sprite.draw(batch);
    }
}
