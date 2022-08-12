package rs.wolf.theastray.cards.pros;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.lazymankits.interfaces.cards.UpgradeBranch;
import rs.wolf.theastray.cards.AstrayProCard;

import java.util.ArrayList;
import java.util.List;

public class B14 extends AstrayProCard {
    public B14() {
        super(14, 1, CardTarget.SELF);
        setDamageValue(10, true);
        setPromosValue(1, true);
        setCanEnlighten(true);
        setStorage(true);
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        addToBot(DamageAction(t, s, AbstractGameAction.AttackEffect.FIRE));
    }
    
    @Override
    public void selfUpgrade() {
        branchingUpgrade();
    }
    
    @Override
    protected List<UpgradeBranch> branches() {
        return new ArrayList<UpgradeBranch>() {{
            add(() -> {
                upgradeTexts();
                upgradeDamage(5);
                upgradePromos(1);
            });
            add(() -> {
                upgradeTexts(1);
                upgradeDamage(5);
                setMagicalDerivative(true);
            });
        }};
    }
}