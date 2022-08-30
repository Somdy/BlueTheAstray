package rs.wolf.theastray.cards.pros;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.wolf.theastray.cards.AstrayProCard;

public class B34 extends AstrayProCard {
    public B34() {
        super(34, 1, CardTarget.NONE);
        setMagicValue(2, true);
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new DrawCardAction(s, magicNumber));
    }
    
    @Override
    public void selfUpgrade() {
        upgradeTexts();
        upgradeMagicNumber(1);
    }
    
    @Override
    public boolean freeToPlay() {
        return super.freeToPlay() || noAttackingEnemies();
    }
    
    private boolean noAttackingEnemies() {
        return !outOfDungeon() && getAllLivingMstrs().stream().noneMatch(m -> m.getIntentBaseDmg() > 0);
    }
}
