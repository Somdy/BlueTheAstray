package rs.wolf.theastray.monsters;

import basemod.ReflectionHacks;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.unique.RemoveAllPowersAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.vfx.combat.HeartBuffEffect;
import com.megacrit.cardcrawl.vfx.combat.ViceCrushEffect;
import rs.lazymankits.actions.utility.QuickAction;
import rs.wolf.theastray.abstracts.AstrayMonster;
import rs.wolf.theastray.powers.monsters.AssimilationPower;
import rs.wolf.theastray.powers.monsters.FocusedEyesPower;
import rs.wolf.theastray.powers.monsters.MasteryBossPower;
import rs.wolf.theastray.utils.TAUtils;
import rs.wolf.theastray.vfx.combat.BlueConvergeEffect;

public class BlueTheBoss extends AstrayMonster {
    public static final String ID = TAUtils.MakeID("BlueTheBoss");
    public static boolean AsFinal = false;
    private static final String IMG_PATH = "AstrayAssets/images/monsters/blue/image.png";
    private static final byte CONVERGE = 0;
    private static final byte SLICE = 1;
    private static final byte BLOSSOM = 2;
    
    private final int invincibleAmt;
    private final int strAmt;
    private final int weakAmt;
    private final int vulnerableAmt;
    private final int regenAmt;
    private final int sliceMult;
    private int turnCount;
    
    public BlueTheBoss(float x, float y) {
        super(ID, 500, 0, 0, 220F, 335F, IMG_PATH, x, y);
        type = EnemyType.BOSS;
        setHp(hardTime(9) ? 550 : 500);
        regenAmt = 10;
        weakAmt = 3;
        strAmt = 5;
        vulnerableAmt = 3;
        invincibleAmt = 0;
        int sliceDmg = 15;
        sliceMult = hardTime(4) ? 4 : 3;
        turnCount = 0;
        damage.add(0, new DamageInfo(this, sliceDmg));
        addPower(new FocusedEyesPower(this, 12, hardTime(19) ? 25 : 50));
        addPower(new InvinciblePower(this, invincibleAmt));
        addPower(new MasteryBossPower(this, 12, 1));
    }
    
    @Override
    public void usePreBattleAction() {
        CardCrawlGame.music.unsilenceBGM();
        AbstractDungeon.scene.fadeOutAmbiance();
        currRoom().playBgmInstantly("BOSS_ENDING");
        AsFinal = true;
    }
    
    @Override
    protected void selfTakeTurn() {
        switch (nextMove) {
            case CONVERGE:
                addToBot(new VFXAction(new BlueConvergeEffect(MathUtils.random(10, 12), hb.cX, hb.cY, 
                        15F * Settings.scale)));
                addToBot(new ApplyPowerAction(this, this, new StrengthPower(this, strAmt)));
                addToBot(new GainBlockAction(this, this, 30));
                addToBot(new ApplyPowerAction(cpr(), this, new WeakPower(cpr(), weakAmt, true)));
                addToBot(new ApplyPowerAction(cpr(), this, new FrailPower(cpr(), weakAmt, true)));
                break;
            case SLICE:
                for (int i = 0; i < sliceMult; i++) {
                    addToBot(new VFXAction(new ViceCrushEffect(cpr().hb.cX, cpr().hb.cY), 0.5F));
                    addToBot(DamageAction(cpr(), damage.get(0), AbstractGameAction.AttackEffect.NONE));
                }
                addToBot(new ApplyPowerAction(cpr(), this, new VulnerablePower(cpr(), vulnerableAmt, true)));
                break;
            case BLOSSOM:
                turnCount = 0;
                addToBot(new VFXAction(new HeartBuffEffect(hb.cX, hb.cY)));
                addToBot(new RemoveAllPowersAction(this, true));
                addToBot(new ApplyPowerAction(this, this, new RegenPower(this, regenAmt)));
                addToBot(new GainBlockAction(this, this, 50));
                addToBot(new QuickAction(() -> {
                    if (!hasPower(BeatOfDeathPower.POWER_ID)) {
                        addToTop(new ApplyPowerAction(this, this, new BeatOfDeathPower(this, 2)));
                    }
                    if (!hasPower(AssimilationPower.ID)) {
                        addToTop(new ApplyPowerAction(this, this, new AssimilationPower(this, 1, 1)));
                    }
                    if (!hasPower(FocusedEyesPower.ID)) {
                        addToTop(new ApplyPowerAction(this, this, new FocusedEyesPower(this, 12, hardTime(19) ? 25 : 50)));
                    }
                    if (!hasPower(MasteryBossPower.ID)) {
                        addToTop(new ApplyPowerAction(this, this, new MasteryBossPower(this, 12, 1)));
                    }
                    AbstractPower p = getPower(InvinciblePower.POWER_ID);
                    if (p != null) {
                        p.amount = 0;
                        ReflectionHacks.setPrivate(p, InvinciblePower.class, "maxAmt", 0);
                        p.updateDescription();
                    } else {
                        addToTop(new ApplyPowerAction(this, this, new InvinciblePower(this, invincibleAmt)));
                    }
                }));
                break;
        }
        turnCount++;
        addToBot(new RollMoveAction(this));
    }
    
    @Override
    protected void getMove(int roll) {
        if (turnCount != 0 && turnCount % 2 == 0) {
            setMove(MOVES[BLOSSOM], BLOSSOM, Intent.BUFF);
        } else if (monsterAiRng().randomBoolean()) {
            setMove(MOVES[CONVERGE], CONVERGE, Intent.DEBUFF);
        } else {
            setMove(MOVES[SLICE], SLICE, Intent.ATTACK_DEBUFF, damage.get(0).base, sliceMult, true);
        }
    }
    
    @Override
    public void die() {
        if (!currRoom().cannotLose) {
            super.die();
            onBossVictoryLogic();
            onFinalBossVictoryLogic();
            CardCrawlGame.stopClock = true;
        }
    }
}