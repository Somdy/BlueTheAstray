package rs.wolf.theastray.cards.pros;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import rs.lazymankits.actions.utility.QuickAction;
import rs.lazymankits.interfaces.cards.UpgradeBranch;
import rs.wolf.theastray.cards.AstrayProCard;

import java.util.ArrayList;
import java.util.List;

public class B9 extends AstrayProCard {
    public B9() {
        super(9, 1, CardTarget.ALL_ENEMY);
        setMagicValue(2, true);
        setExtraMagicValue(2, true);
        setMagical(true);
        setCanEnlighten(true);
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        if (!upgraded || finalBranch() == 1) {
            addToBot(new QuickAction(() -> {
                for (AbstractMonster m : getAllLivingMstrs()) {
                    if (m.getIntentBaseDmg() > 0)
                        addToTop(ApplyPower(m, s, new WeakPower(m, magicNumber, false)));
                    else 
                        addToTop(ApplyPower(m, s, new VulnerablePower(m, magicNumber, false)));
                }
            }));
            return;
        }
        if (finalBranch() == 0) {
            addToBot(new QuickAction(() -> {
                for (AbstractMonster m : getAllLivingMstrs()) {
                    addToTop(ApplyPower(m, s, new WeakPower(m, magicNumber, false)));
                    addToTop(ApplyPower(m, s, new VulnerablePower(m, magicNumber, false)));
                }
            }));
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
                exhaust = true;
            });
            add(() -> {
                upgradeTexts(1);
                upgradeBaseCost(0);
                exhaust = true;
            });
        }};
    }
}