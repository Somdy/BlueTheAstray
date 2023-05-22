package rs.wolf.theastray.cards.pros;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.lazymankits.interfaces.cards.UpgradeBranch;
import rs.wolf.theastray.cards.AstrayProCard;

import java.util.ArrayList;
import java.util.List;

public class B50 extends AstrayProCard {
    public B50() {
        super(50, 1, CardTarget.SELF_AND_ENEMY);
        setDamageValue(3, true);
        setBlockValue(3, true);
        setPromosValue(0, true);
        setMagicalDerivative(true);
        setCanEnlighten(true);
//        setStorage(true);
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        addToBot(DamageAction(t, s, damage > 10 ? AbstractGameAction.AttackEffect.SLASH_HEAVY
                : AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        addToBot(new GainBlockAction(s, block));
        atbTmpAction(() -> {
            upgradePromos(1);
            updateDescription(UPDATED_DESC[upgraded ? finalBranch() : 0]);
        });
    }
    
    @Override
    public void applyPowers() {
        super.applyPowers();
        if (getPromos() > 0)
            updateDescription(UPDATED_DESC[upgraded ? finalBranch() : 0]);
    }
    
    @Override
    public void selfUpgrade() {
        branchingUpgrade();
    }
    
    @Override
    protected List<UpgradeBranch> branches() {
        return new ArrayList<UpgradeBranch>(){{
           add(() -> {
               upgradeTexts();
               upgradeBaseCost(0);
               setStorage(true);
           }); 
           add(() -> {
               upgradeTexts(1);
               setMagicValue(1, true);
               upgradePromos(2);
               setStorage(true);
           });
        }};
    }
}