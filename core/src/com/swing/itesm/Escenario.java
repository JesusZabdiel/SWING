package com.swing.itesm;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Escenario {
    private Background background1, background2, background3, background4, background5, background6;
    private Background[] backgrounds;


    public Escenario(Texture texture1, Texture texture2, Texture texture3, Texture texture4, Texture texture5, Texture texture6){
        background1 = new Background(texture1,0,0);
        background2 = new Background(texture2,0,0);
        background3 = new Background(texture3,0,0);
        background4 = new Background(texture4,0,0);
        background5 = new Background(texture5,0,0);
        background6 = new Background(texture6,0,0);
        backgrounds = new Background[6];
        backgrounds[5] = background1;
        backgrounds[4] = background2;
        backgrounds[3] = background3;
        backgrounds[2] = background4;
        backgrounds[1] = background5;
        backgrounds[0] = background6;
    }

    public void mover(float velocidad) {
        backgrounds[5].moverBackgound(velocidad*1.2f);
        backgrounds[4].moverBackgound(velocidad*0.8f);
        backgrounds[3].moverBackgound(velocidad*0.35f);
        backgrounds[2].moverBackgound(velocidad*0.2f);
        backgrounds[1].moverBackgound(velocidad*0.08f);
        backgrounds[0].moverBackgound(velocidad*0.02f);
    }

    public void render(SpriteBatch batch) {
        for (int i=0; i<5; i++) {
            backgrounds[i].render(batch);
        }
    }
    public void renderFront(SpriteBatch batch){
        backgrounds[5].render(batch);
    }
}
