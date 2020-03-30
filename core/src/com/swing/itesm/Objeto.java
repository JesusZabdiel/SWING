package com.swing.itesm;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Objeto {
    protected Sprite sprite;

    public Objeto(Texture textura, float  x, float y){
        sprite = new Sprite(textura);
        sprite.setPosition(x,y);
    }

    public void render(SpriteBatch batch){
        sprite.draw(batch);
    }
}
