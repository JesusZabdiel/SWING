package com.swing.itesm;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Background{
    private Sprite sprite, spriteCopy;

    public Background(Texture textura, float x, float y) {
        sprite = new Sprite(textura);
        spriteCopy = new Sprite(textura);
        sprite.setPosition(x,y);
        spriteCopy.setPosition(sprite.getWidth(), y);
    }

    public void moverBackgound(float velocidad) {
        if(sprite.getX()+ sprite.getWidth() <= 0){
            sprite.setX(0);
        }
        sprite.setX(sprite.getX()-velocidad);
        spriteCopy.setX(sprite.getX()+sprite.getWidth());

    }

    public void render(SpriteBatch batch){
        sprite.draw(batch);
        spriteCopy.draw(batch);
    }
}