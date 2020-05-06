package com.swing.itesm;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Item {
    public Sprite sprite;

    public Item(Texture textura) {
        sprite = new Sprite(textura);
        generarPosicionItem();
    }


    public void render(SpriteBatch batch){
            sprite.draw(batch);
    }

    public void mover(){
        if(this.sprite.getX() > 0 - this.sprite.getWidth()){
            this.sprite.setX(sprite.getX() - PantallaPlay.speed);
        }else{
            this.generarPosicionItem();
        }

    }

    public void generarPosicionItem() {
        float xPos = (int)(((Math.random()* Pantalla.ANCHO-sprite.getWidth())+sprite.getWidth())+Pantalla.ANCHO);
        float yPos = (int)(Math.random()* (Pantalla.ALTO-200))+70;
        this.sprite.setPosition(xPos,yPos);
    }
}
