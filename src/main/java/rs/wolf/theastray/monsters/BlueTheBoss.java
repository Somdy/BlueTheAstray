package rs.wolf.theastray.monsters;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.unique.RemoveAllPowersAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.vfx.combat.ClawEffect;
import rs.lazymankits.actions.utility.QuickAction;
import rs.wolf.theastray.abstracts.AstrayMonster;
import rs.wolf.theastray.core.Leader;
import rs.wolf.theastray.powers.monsters.FocusedEyesPower;
import rs.wolf.theastray.powers.monsters.MasteryBossPower;
import rs.wolf.theastray.utils.TAUtils;

public class BlueTheBoss extends AstrayMonster {
    public static final String ID = TAUtils.MakeID("BlueTheBoss");
    
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
        if (hardTime(19)) {
            invincibleAmt = 0;
            weakAmt = 3;
            regenAmt = 10;
        } else {
            invincibleAmt = 50;
            weakAmt = 2;
            regenAmt = 5;
        }
        setHp(hardTime(9) ? 500 : 550);
        strAmt = 5;
        vulnerableAmt = 2;
        int sliceDmg = hardTime(4) ? 15 : 12;
        sliceMult = 3;
        turnCount = 0;
        damage.add(0, new DamageInfo(this, sliceDmg));
        powers.add(new FocusedEyesPower(this, 12, 50));
        powers.add(new InvinciblePower(this, invincibleAmt));
        powers.add(new MasteryBossPower(this, 12, 1));
    }
    
    @Override
    protected void selfTakeTurn() {
        switch (nextMove) {
            case CONVERGE:
                addToBot(new ApplyPowerAction(this, this, new StrengthPower(this, strAmt)));
                addToBot(new ApplyPowerAction(cpr(), this, new WeakPower(cpr(), weakAmt, true)));
                addToBot(new ApplyPowerAction(cpr(), this, new FrailPower(cpr(), weakAmt, true)));
                break;
            case SLICE:
                for (int i = 0; i < sliceMult; i++) {
                    addToBot(new VFXAction(new ClawEffect(cpr().hb.cX, cpr().hb.cY, Color.SCARLET, Leader.TAColor)));
                    addToBot(DamageAction(cpr(), damage.get(0), AbstractGameAction.AttackEffect.NONE));
                }
                addToBot(new ApplyPowerAction(cpr(), this, new VulnerablePower(cpr(), vulnerableAmt, true)));
                break;
            case BLOSSOM:
                turnCount = 0;
                addToBot(new RemoveAllPowersAction(this, true));
                addToBot(new ApplyPowerAction(this, this, new RegenPower(this, regenAmt)));
                addToBot(new QuickAction(() -> {
                    boolean noBeat = !hasPower(BeatOfDeathPower.POWER_ID);
                    boolean noInvincible = !hasAnyPowerOf(this, p -> p instanceof InvinciblePower && p.amount > 0);
                    if (noBeat || noInvincible) {
                        addToBot(new ApplyPowerAction(this, this, new BeatOfDeathPower(this, 2)));
                        if (!noInvincible) {
                            AbstractPower p = getPower(InvinciblePower.POWER_ID);
                            if (p != null) {
                                p.amount = 0;
                                p.updateDescription();
                            }
                        }
                    }
                }));
                break;
        }
        turnCount++;
        addToBot(new RollMoveAction(this));
    }
    
    @Override
    protected void getMove(int roll) {
        if (turnCount != 0 && turnCount % 3 == 0) {
            setMove(MOVES[BLOSSOM], BLOSSOM, Intent.BUFF);
        } else if (monsterAiRng().randomBoolean()) {
            setMove(MOVES[CONVERGE], CONVERGE, Intent.DEBUFF);
        } else {
            setMove(MOVES[SLICE], SLICE, Intent.ATTACK_DEBUFF, damage.get(0).base, sliceMult, true);
        }
    }
}
