package rs.wolf.theastray.cards.pros;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import rs.wolf.theastray.cards.AstrayProCard;
import rs.wolf.theastray.powers.FrostPower;

public class B16 extends AstrayProCard {
    public B16() {
        super(16, 1, CardTarget.ALL);
        setBlockValue(6, true);
        setMagicValue(1, true);
        setExtraMagicValue(1, true);
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new GainBlockAction(s, block));
        addToBot(new DrawCardAction(s, magicNumber));
        atbTmpAction(() -> {
            for (AbstractMonster m : getAllLivingMstrs()) {
                addToTop(ApplyPower(m, s, new WeakPower(m, getExtraMagic(), false)));
                if (upgraded) addToTop(ApplyPower(m, s, frostPower(m, s, getExtraMagic())));
            }
        });
    }
    
    @Override
    public void selfUpgrade() {
        upgradeTexts();
    }
}