package com.swing.itesm;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

class Texto {
    private BitmapFont font;

    public Texto(String archivo){
        this.font = new BitmapFont(Gdx.files.internal(archivo));//Este archivo es .fnt
    }

    public void render(SpriteBatch bacth, String mesaje, float x, float y) {
        GlyphLayout glyph = new GlyphLayout();
        glyph.setText(font, mesaje);
        float anchoTexto = glyph.width;
        font.setColor(1/255f*46,1/255f*109,1/255f*14,1);
        font.draw(bacth,glyph,x-anchoTexto/2,y);
    }
}
