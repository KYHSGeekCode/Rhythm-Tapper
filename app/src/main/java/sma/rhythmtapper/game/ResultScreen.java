package sma.rhythmtapper.game;

import android.graphics.Color;
import android.graphics.Paint;

import java.util.List;

import sma.rhythmtapper.DifficultySelectionActivity;
import sma.rhythmtapper.framework.Game;
import sma.rhythmtapper.framework.Graphics;
import sma.rhythmtapper.framework.Impl.RTGame;
import sma.rhythmtapper.framework.Input;
import sma.rhythmtapper.framework.Screen;
import sma.rhythmtapper.game.models.Difficulties;
import sma.rhythmtapper.game.models.GameResult;

public class ResultScreen extends Screen {
    int sizeX;
    int sizeY;
    GameResult result;
    Paint paint=new Paint();
    public ResultScreen(Game game,GameResult result) {
        super(game);
        sizeX=game.getScreenX();
        sizeY=game.getScreenY();
        this.result=result;
        paint.setColor(Color.MAGENTA);
        paint.setTextSize(60);
        paint.setStrokeWidth(5);
    }

    @Override
    public void update(float deltaTime) {
        Input input=game.getInput();
        List<Input.TouchEvent> events= input.getTouchEvents();
        if(events.size()>0)
        {
            for(Input.TouchEvent event:events)
            {
                if(event.x>=sizeX/3*2&&event.y>sizeY/3*2)
                {
                    //game.setScreen(((RTGame)game).chooseSongScreen);
                    game.goToActivity(DifficultySelectionActivity.class);
                }
            }
        }
    }

    @Override
    public void paint(float deltaTime) {
        Graphics g=game.getGraphics();
        g.drawRect(0,0,sizeX,sizeY, Color.WHITE);
        g.drawRect((int)(sizeX*0.1f), (int)(sizeY*0.1f), (int)(sizeX*0.6f), (int)(sizeY*0.8f),Color.rgb(0,0,0));
        g.drawRect((int)(sizeX*0.12f), (int)(sizeY*0.12f), (int)(sizeX*0.56f), (int)(sizeY*0.76f),Color.rgb(0xdf,0xd8,0xcf));
        paint.setColor(Color.MAGENTA);
        g.drawString("PERFECT : " +result.perfect,(int)(sizeX*0.14f),(int)(sizeY*0.20f),paint);
        paint.setColor(Color.CYAN);
        g.drawString("GREAT : " + result.great,(int)(sizeX*0.14f),(int)(sizeY*0.26f),paint);
        paint.setColor(Color.YELLOW);
        g.drawString("NICE : " + result.nice,(int)(sizeX*0.14f),(int)(sizeY*0.32f),paint);
        paint.setColor(Color.BLUE);
        g.drawString("BAD : " + result.bad,(int)(sizeX*0.14f),(int)(sizeY*0.38f),paint);
        paint.setColor(Color.RED);
        g.drawString("MISS : " + result.miss,(int)(sizeX*0.14f),(int)(sizeY*0.44f),paint);
        paint.setColor(Color.BLACK);
        g.drawString("MAX COMBO   " + result.maxcombo,(int)(sizeX*0.14f),(int)(sizeY*0.54f),paint);
//        paint.setColor(Color.BLACK);
        g.drawString("SCORE   " + result.score,(int)(sizeX*0.14f),(int)(sizeY*0.64f),paint);
        g.drawString("HIGHSCORE   " + result.highscore,(int)(sizeX*0.14f),(int)(sizeY*0.70f),paint);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public void backButton() {

    }

}
