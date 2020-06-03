package com.swing.itesm;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Ojo extends Objeto {
    private Animation animacion;
    private float x, y;
    private float timerAnimacion;

    public Ojo(Texture textura, float x, float y) {
        super(textura, x, y);
        this.x = x;
        this.y = y;

        TextureRegion region = new TextureRegion(textura);
        TextureRegion[][] texturaOjo = region.split(50,50);
        animacion = new Animation(0.1f, texturaOjo[0][0],texturaOjo[0][1], texturaOjo[0][2],
                texturaOjo[0][3], texturaOjo[0][4],texturaOjo[0][5], texturaOjo[0][6],
                texturaOjo[0][7]);
        animacion.setPlayMode(Animation.PlayMode.LOOP_RANDOM);
        timerAnimacion = 0;
    }

    public void render(SpriteBatch batch){
        TextureRegion region = (TextureRegion)animacion.getKeyFrame(timerAnimacion);
        batch.draw(region,x,y);
    }


    public void moverOjo(float y){
        timerAnimacion+= Gdx.graphics.getDeltaTime();
        this.x = 140+(float)Math.random()*10;
        this.y = y+100+(float)Math.random()*5;
    }
}
