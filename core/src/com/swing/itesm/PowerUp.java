package com.swing.itesm;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PowerUp {

    private static final int PROBABILIDAD_POWER_UP = 60;
    Sprite sprite;

    public PowerUp(Texture textura) {
        sprite = new Sprite(textura);
        generarPosicionPowerUp();
    }


    public void render(SpriteBatch batch){
        if (probabilidadPowerUp()){
            sprite.draw(batch);
            }
    }

    public void mover(){
        this.sprite.setX(sprite.getX() - PlayingScreen.speed);
    }

    private void generarPosicionPowerUp() {
        float xPos = Pantalla.ANCHO;
        float yPos = (int)((Math.random()* Pantalla.ALTO-sprite.getHeight())+sprite.getHeight()) ;
        this.sprite.setPosition(xPos,yPos);
    }

    public boolean probabilidadPowerUp(){
        int probabilidad = ((int)(Math.random()*100))+1;
        return probabilidad <= PROBABILIDAD_POWER_UP;
    }

}
