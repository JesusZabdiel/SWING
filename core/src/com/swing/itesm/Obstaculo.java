package com.swing.itesm;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Obstaculo{
    private static final int PROBABILIDAD_POWER_UP = 60;
    Sprite sprite;

    public Obstaculo(Texture textura) {
        sprite = new Sprite(textura);
        generarPosicionPowerUp();
    }


    public void render(SpriteBatch batch){
        sprite.draw(batch);
    }

    public void mover(){
        if(this.sprite.getX() > 0 - this.sprite.getWidth()){
            this.sprite.setX(sprite.getX() - PlayingScreen.speed);
        }else{
            this.generarPosicionPowerUp();
        }

    }

    public void generarPosicionPowerUp() {
        float xPos = (int)(((Math.random()* Pantalla.ANCHO-sprite.getWidth())+sprite.getWidth())+Pantalla.ANCHO);
        float yPos = (int)((Math.random()* Pantalla.ALTO-sprite.getHeight()))+sprite.getHeight();
        this.sprite.setPosition(xPos,yPos);
    }
}