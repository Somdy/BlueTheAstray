package rs.wolf.theastray.characters;

import basemod.abstracts.CustomPlayer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.colorless.Madness;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.relics.BurningBlood;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.ui.panels.energyorb.EnergyOrbBlue;
import com.megacrit.cardcrawl.ui.panels.energyorb.EnergyOrbGreen;
import rs.wolf.theastray.core.CardMst;
import rs.wolf.theastray.core.Leader;
import rs.wolf.theastray.patches.TACardEnums;
import rs.wolf.theastray.ui.manalayout.ManaMst;
import rs.wolf.theastray.utils.GlobalIDMst;
import rs.wolf.theastray.utils.TAUtils;

import java.util.ArrayList;

public class BlueTheAstray extends CustomPlayer implements TAUtils {
    public static final String ID = TAUtils.MakeID("TheAstray");
    public static final CharacterStrings charStrings = CardCrawlGame.languagePack.getCharacterString(ID);
    public static final String NAME = charStrings.NAMES[0];
    public static final String DESCRIPTION = charStrings.TEXT[0];
    public static final String Shoulder_1 = "AstrayAssets/images/char/shoulder.png";
    public static final String Shoulder_2 = "AstrayAssets/images/char/shoulder2.png";
    public static final String corpse = "AstrayAssets/images/char/corpse.png";
    public static final String SK_ALT = "AstrayAssets/images/char/anim/skeleton.atlas";
    public static final String SK_JSON = "AstrayAssets/images/char/anim/skeleton.json";
    public static final int STARTING_HP = 75;
    public static final int MAX_HP = 75;
    public static final int STARTING_GOLD = 99;
    public static final int ORB_SLOTS = 0;
    public static final int DRAW_PER_TURN = 5;
    public static final Color TAColor = Leader.TAColor;
    public ManaMst manaMst;
    
    public BlueTheAstray() {
        super("Nightmare", TACardEnums.BlueTheAstray, new EnergyOrbBlue(), null, null);
        initializeClass(null, Shoulder_2, Shoulder_1, corpse, getLoadout(), 20F, -10F,
                300F, 370F, new EnergyManager(3));
        manaMst = new ManaMst(this, 10, 0, ManaMst.Layout.REVOLUTION);
        loadAnimation(SK_ALT, SK_JSON, 1.25F);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "Idle", true);
        e.setTime(e.getEndTime() * MathUtils.random());
        e.setTimeScale(0.8F);
        dialogX = drawX + scale(1.5F);
        dialogY = drawY + scale(220F);
    }
    
    @Override
    public ArrayList<String> getStartingDeck() {
        return listFromRepeatableObjs(GlobalIDMst.CardID("打击"), GlobalIDMst.CardID("打击"), 
                GlobalIDMst.CardID("打击"), GlobalIDMst.CardID("打击"), GlobalIDMst.CardID("防御"), 
                GlobalIDMst.CardID("防御"), GlobalIDMst.CardID("防御"), GlobalIDMst.CardID("防御"), 
                GlobalIDMst.CardID("魔法飞弹"), GlobalIDMst.CardID("魔力释放"));
    }
    
    @Override
    public ArrayList<String> getStartingRelics() {
        return listFromObjs(GlobalIDMst.RelicID(1));
    }
    
    @Override
    public void update() {
        super.update();
        manaMst.update();
    }
    
    @Override
    public void preBattlePrep() {
        manaMst.initPreBattle();
        super.preBattlePrep();
    }
    
    @Override
    public Texture getEnergyImage() {
        return ImageMaster.BLUE_ORB_FLASH_VFX;
    }
    
    @Override
    public void renderOrb(SpriteBatch sb, boolean enabled, float current_x, float current_y) {
        super.renderOrb(sb, enabled, current_x, current_y);
        manaMst.render(sb);
    }
    
    @Override
    public CharSelectInfo getLoadout() {
        return new CharSelectInfo(NAME, DESCRIPTION, STARTING_HP, MAX_HP, ORB_SLOTS, STARTING_GOLD, DRAW_PER_TURN,
                this, getStartingRelics(), getStartingDeck(), false);
    }
    
    @Override
    public String getTitle(PlayerClass playerClass) {
        return NAME;
    }
    
    @Override
    public AbstractCard.CardColor getCardColor() {
        return TACardEnums.TA_CardColor;
    }
    
    @Override
    public Color getCardRenderColor() {
        return TAColor;
    }
    
    @Override
    public AbstractCard getStartCardForEvent() {
        return CardMst.GetCard("魔力释放");
    }
    
    @Override
    public Color getCardTrailColor() {
        return TAColor;
    }
    
    @Override
    public int getAscensionMaxHPLoss() {
        return 5;
    }
    
    @Override
    public BitmapFont getEnergyNumFont() {
        return FontHelper.energyNumFontGreen;
    }
    
    @Override
    public void doCharSelectScreenSelectEffect() {
        
    }
    
    @Override
    public String getCustomModeCharacterButtonSoundKey() {
        return "ATTACK_MAGIC_BEAM_SHORT";
    }
    
    @Override
    public String getLocalizedCharacterName() {
        return NAME;
    }
    
    @Override
    public AbstractPlayer newInstance() {
        return new BlueTheAstray();
    }
    
    @Override
    public String getSpireHeartText() {
        return charStrings.TEXT[1];
    }
    
    @Override
    public Color getSlashAttackColor() {
        return TAColor;
    }
    
    @Override
    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() {
        return new AbstractGameAction.AttackEffect[] {
                AbstractGameAction.AttackEffect.FIRE, AbstractGameAction.AttackEffect.FIRE,
                AbstractGameAction.AttackEffect.SLASH_HEAVY, AbstractGameAction.AttackEffect.FIRE,
                AbstractGameAction.AttackEffect.SLASH_HEAVY
        };
    }
    
    @Override
    public String getVampireText() {
        return charStrings.TEXT[2];
    }
}