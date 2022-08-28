package rs.wolf.theastray.cards.pros;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.wolf.theastray.cards.AstrayProCard;

public class B29 extends AstrayProCard {
    public B29() {
        super(29, 1, CardTarget.ENEMY);
        setDamageValue(6, true);
        setMagicValue(3, true);
        cardsToPreview = new Burn();
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        addToBot(DamageAction(t, s, AbstractGameAction.AttackEffect.FIRE));
        addToBot(ApplyPower(t, s, burntPower(t, s, magicNumber)));
        addToBot(new MakeTempCardInHandAction(new Burn(), 1));
    }
    
    @Override
    public void selfUpgrade() {
        upgradeTexts();
        upgradeDamage(3);
        upgradeMagicNumber(3);
    }
}
