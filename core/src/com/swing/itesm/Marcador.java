package com.swing.itesm;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

class Marcador {

    private float puntos;
    private float x;
    private float y;

    private Texto texto; //Objeto Texto  Puntos

    //Constructor
    public Marcador(float x, float y){
        this.x = x;
        this.y = y;
        this.puntos = 0;
        this.texto =  new Texto("fontScore.fnt");   //Font
    }
    public void reset(){
        this.puntos = 0;
    }

    //Agregar puntos
    public  void marcar(float puntos){
        this.puntos+=puntos;
    }

    public void render(SpriteBatch bacth){
        String mesaje = (int)puntos + "";
        texto.render(bacth, mesaje,x,y);
    }
}
