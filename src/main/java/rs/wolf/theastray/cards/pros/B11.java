package rs.wolf.theastray.cards.pros;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import rs.wolf.theastray.cards.AstrayProCard;

public class B11 extends AstrayProCard {
    public B11() {
        super(11, 0, CardTarget.SELF_AND_ENEMY);
        setDamageValue(4, true);
        setBlockValue(2, true);
        setMagicValue(1, true);
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        for (int i = 0; i < magicNumber; i++) {
            addToBot(DamageAction(t, s, AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
            addToBot(new GainBlockAction(s, s, block));
        }
    }
    
    @Override
    public void selfUpgrade() {
        upgradeTexts();
        upgradeMagicNumber(1);
    }
}