package com.swing.itesm;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

public class Ralentizacion extends Item {


    public Ralentizacion(Texture textura) {
        super(textura);
        probabilidad = 10;
        visible = false;
    }
}
