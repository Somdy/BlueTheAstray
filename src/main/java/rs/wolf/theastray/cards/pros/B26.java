package rs.wolf.theastray.cards.pros;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.lazymankits.interfaces.cards.UpgradeBranch;
import rs.wolf.theastray.cards.AstrayProCard;

import java.util.ArrayList;
import java.util.List;

public class B26 extends AstrayProCard {
    public B26() {
        super(26, 0, CardTarget.SELF_AND_ENEMY);
        setDamageValue(0, true);
        setBlockValue(0, true);
        setMagicValue(0, true);
        setMagicalDerivative(true);
        setCanEnlighten(true);
        setStorage(true);
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        addToBot(DamageAction(t, s, AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        addToBot(new GainBlockAction(s, block));
        if (upgraded) {
            if (finalBranch() == 0) {
                addToBot(new DrawCardAction(s, magicNumber));
            } else if (finalBranch() == 1) {
                addToBot(new GainEnergyAction(magicNumber));
            }
        }
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
               setMagicalDerivative(true);
           });
           add(() -> {
               upgradeTexts(1);
               setMagicalDerivative(true);
           });
        }};
    }
}
