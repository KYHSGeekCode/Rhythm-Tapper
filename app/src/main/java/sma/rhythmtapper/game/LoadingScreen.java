package sma.rhythmtapper.game;

import sma.rhythmtapper.framework.Game;
import sma.rhythmtapper.framework.Graphics;
import sma.rhythmtapper.framework.Screen;
import sma.rhythmtapper.game.models.AppealType;
import sma.rhythmtapper.game.models.Card;
import sma.rhythmtapper.game.models.CenterSkill;
import sma.rhythmtapper.game.models.ColorType;
import sma.rhythmtapper.game.models.Deck;
import sma.rhythmtapper.game.models.Skill.Skill;
import sma.rhythmtapper.game.models.Skill.SkillBoost;
import sma.rhythmtapper.game.models.Skill.SkillComboUp;
import sma.rhythmtapper.game.models.Skill.SkillDamageGuard;
import sma.rhythmtapper.game.models.Skill.SkillHeal;
import sma.rhythmtapper.game.models.Skill.SkillOverload;
import sma.rhythmtapper.game.models.Skill.SkillPerfectSupport;
import sma.rhythmtapper.game.models.Skill.SkillScoreUp;
import sma.rhythmtapper.models.Difficulty;


public class LoadingScreen extends Screen {
    private Difficulty _diff;
    private static final String IMAGE_PATH = "img/";
    private static final String SOUND_EFFECTS_PATH = "audio/";
    private static final String MUSIC_PATH = "music/";


    public LoadingScreen(Game game, Difficulty difficulty) {
        super(game);
        this._diff = difficulty;
    }


    @Override
    public void update(float deltaTime) {
        Graphics g = game.getGraphics();

        Assets.background = g.newImage(IMAGE_PATH + "background.png", Graphics.ImageFormat.RGB565);
        Assets.gameover = g.newImage(IMAGE_PATH + "gameover.png", Graphics.ImageFormat.RGB565);
        Assets.pause = g.newImage(IMAGE_PATH + "pause.png", Graphics.ImageFormat.RGB565);
        Assets.ballNormal = g.newImage(IMAGE_PATH + "ball_normal.png", Graphics.ImageFormat.RGB565);
        Assets.ballMultiplier = g.newImage(IMAGE_PATH + "ball_multiplier.png", Graphics.ImageFormat.RGB565);
        Assets.ballOneUp = g.newImage(IMAGE_PATH + "ball_oneup.png", Graphics.ImageFormat.RGB565);
        Assets.ballSpeeder = g.newImage(IMAGE_PATH + "ball_speeder.png", Graphics.ImageFormat.RGB565);
        Assets.ballBomb = g.newImage(IMAGE_PATH + "ball_bomb.png", Graphics.ImageFormat.RGB565);
        Assets.explosion = g.newImage(IMAGE_PATH + "explosion.png", Graphics.ImageFormat.RGB565);
        Assets.explosionBright = g.newImage(IMAGE_PATH + "explosion_bright.png", Graphics.ImageFormat.RGB565);
        Assets.ballSkull = g.newImage(IMAGE_PATH + "skull-ball-icon.png", Graphics.ImageFormat.RGB565);
        Assets.sirens = g.newImage(IMAGE_PATH + "sirens.png", Graphics.ImageFormat.RGB565);
        Assets.ballHitpoint = g.newImage(IMAGE_PATH + "ball_hitpoint.png",Graphics.ImageFormat.RGB565);
        Assets.ballFlickLeft = g.newImage(IMAGE_PATH + "ball_flickleft.png",Graphics.ImageFormat.RGB565);
        Assets.ballFlickRight = g.newImage(IMAGE_PATH + "ball_flickright.png",Graphics.ImageFormat.RGB565);

        Assets.soundClick = game.getAudio().createSound(SOUND_EFFECTS_PATH + "sound_guiclick.ogg");
        Assets.soundExplosion = game.getAudio().createSound(SOUND_EFFECTS_PATH + "sound_explosion.ogg");
        Assets.soundCreepyLaugh = game.getAudio().createSound(SOUND_EFFECTS_PATH + "sound_creepy_laugh.mp3");

        Assets.musicTrack = game.getAudio().createMusic(MUSIC_PATH + _diff.getMusic());
        Deck deck=new Deck();
        deck.SetCard(0,new Card(ColorType.CUTE,5000,5000,5000,30, new CenterSkill(CenterSkill.Condition.ANY,ColorType.CUTE, AppealType.ANY,30), new SkillHeal(7,90,6)));
        deck.SetCard(1,new Card(ColorType.COOL,5000,5000,5000,30, new CenterSkill(CenterSkill.Condition.ANY,ColorType.CUTE, AppealType.ANY,30), new SkillPerfectSupport(8,99,8)));
        deck.SetCard(2,new Card(ColorType.COOL,5000,5000,5000,30, new CenterSkill(CenterSkill.Condition.TRICOLOR,ColorType.ANY, AppealType.ANY,40), new SkillScoreUp(4,60,3)));
        deck.SetCard(3,new Card(ColorType.PASSION,5000,5000,5000,30, new CenterSkill(CenterSkill.Condition.ANY,ColorType.CUTE, AppealType.ANY,30), new SkillBoost(4,99,4)));
        deck.SetCard(4,new Card(ColorType.PASSION,5000,5000,5000,30, new CenterSkill(CenterSkill.Condition.ANY,ColorType.CUTE, AppealType.ANY,30), new SkillDamageGuard(4,90,4)));
        deck.ApplyCenterSkill();
        game.setScreen(new GameScreen(game, _diff,deck));
    }
    @Override
    public void paint(float deltaTime) {

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
