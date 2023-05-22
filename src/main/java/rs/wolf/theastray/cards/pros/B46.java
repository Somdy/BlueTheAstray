package rs.wolf.theastray.cards.pros;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import rs.wolf.theastray.cards.AstrayProCard;
import rs.wolf.theastray.powers.MagicPower;

public class B46 extends AstrayProCard {
    private static boolean playedOnceInCombat = false;
    
    public B46() {
        super(46, 1, CardTarget.SELF);
        setBlockValue(11, true);
        setMagicValue(2, true);
        exhaust = true;
        selfRetain = true;
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new GainBlockAction(s, s, block));
        int amount = Math.min(magicNumber, AbstractDungeon.bossCount);
        addToBot(ApplyPower(s, s, new MagicPower(s, amount)));
    }
    
    @Override
    public void selfUpgrade() {
        upgradeTexts();
//        upgradeBlock(3);
        upgradeMagicNumber(1);
    }
}
