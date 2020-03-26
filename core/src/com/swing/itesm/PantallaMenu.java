package mx.itesm.mcc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

class PantallaMenu extends Pantalla {

    private final SWING juego;

    private Texture texturaFondo;

    // Menu
    private Stage escenaMenu;  // botones,....

    public PantallaMenu(SWING juego) {
        this.juego = juego;
    }

    @Override
    public void show() {

        texturaFondo = new Texture("fondoCueva.jpg");
        crearMenu();

    }

    private void crearMenu() {

        escenaMenu = new Stage(vista);

        // Boton Play
        Texture texturaBtnPlay = new Texture("button_play.png");
        TextureRegionDrawable trdPlay = new TextureRegionDrawable(new TextureRegion(texturaBtnPlay));

        // Boton JugarP
        //Texture texturaBtnJugarP = new Texture("btnSpace.png");
        //TextureRegionDrawable trdJugarP = new TextureRegionDrawable(new TextureRegion(texturaBtnJugarP));

        //Boton Options
        Texture texturaBtnOptions = new Texture("button_options.png");
        TextureRegionDrawable trdOptions = new TextureRegionDrawable(new TextureRegion(texturaBtnOptions));

        // Boton shop
        Texture texturaBtnShop = new Texture("button_shop.png");
        TextureRegionDrawable trdShop = new TextureRegionDrawable(new TextureRegion(texturaBtnShop));

        // Boton customize
        Texture texturaBtnCustomize = new Texture("button_customize.png");
        TextureRegionDrawable trdCustomize = new TextureRegionDrawable(new TextureRegion(texturaBtnCustomize));



        ImageButton btnPlay = new ImageButton(trdPlay);
        ImageButton btnOptions = new ImageButton(trdOptions);
        ImageButton btnShop = new ImageButton(trdShop);
        ImageButton btnCustomize = new ImageButton(trdCustomize);


        btnPlay.setPosition(ANCHO/2-btnPlay.getWidth()/2,2*ALTO/3);

        btnCustomize.setPosition(ANCHO/2-btnPlay.getWidth()/2,2*ALTO/3-100);

        btnShop.setPosition(ANCHO/2-btnPlay.getWidth()/2,2*ALTO/3-200);

        btnOptions.setPosition(ANCHO/2-btnPlay.getWidth()/2,2*ALTO/3-300);

        //Listener1
        btnPlay.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                juego.setScreen(new PantallaSwing(juego));
            }
        });

        //Listener2
        btnCustomize.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                juego.setScreen(new PantallaCustomize(juego));
            }
        });

        //Listener3
        btnShop.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                juego.setScreen(new PantallaShop(juego));
            }
        });

        //Listener4
        btnOptions.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                juego.setScreen(new PantallaOptions(juego));
            }
        });

        escenaMenu.addActor(btnPlay);
        escenaMenu.addActor(btnOptions);
        escenaMenu.addActor(btnShop);
        escenaMenu.addActor(btnCustomize);

        Gdx.input.setInputProcessor(escenaMenu);


    }

    @Override
    public void render(float delta) {
        borrarPantalla();
        batch.setProjectionMatrix(camara.combined);

        batch.begin();
        batch.draw(texturaFondo,0,0);
        batch.end();

        escenaMenu.draw();

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

        texturaFondo.dispose();

    }

}