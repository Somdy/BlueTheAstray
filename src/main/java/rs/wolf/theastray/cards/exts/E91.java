package rs.wolf.theastray.cards.exts;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.BufferPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import rs.lazymankits.actions.common.ApplyPowerToEnemiesAction;
import rs.lazymankits.enums.ApplyPowerParam;
import rs.wolf.theastray.cards.AstrayExtCard;

public class E91 extends AstrayExtCard {
    public E91() {
        super(91, 3, 9, CardTarget.ALL);
        setMagicValue(1, true);
        setExtraMagicValue(1, true);
        setMagical(true);
        setStorage(true);
        exhaust = true;
        addTip(MSG[0], MSG[1]);
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        addToBot(ApplyPower(s, s, new BufferPower(s, magicNumber)));
        addToBot(new ApplyPowerToEnemiesAction(s, WeakPower.class, 
                ApplyPowerParam.ANY_OWNER, getExtraMagic(), false));
    }
    
    @Override
    public void selfUpgrade() {
        upgradeTexts();
        upgradeMagicNumber(1);
        upgradeExtraMagic(1);
    }
}
