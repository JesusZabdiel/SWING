package com.swing.itesm;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

public class Daño extends Item{


    public Daño(Texture textura) {
        super(textura);
        probabilidad = 40;
        visible = false;
        sprite.setColor(Color.RED);
    }

}

