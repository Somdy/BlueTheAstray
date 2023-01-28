package rs.wolf.theastray.cards.pros;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.lazymankits.interfaces.cards.UpgradeBranch;
import rs.wolf.theastray.cards.AstrayProCard;

import java.util.ArrayList;
import java.util.List;

public class B76 extends AstrayProCard {
    public B76() {
        super(76, 1, CardTarget.ALL_ENEMY);
        setDamageValue(22, true);
        setCanEnlighten(true);
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        if (!upgraded || finalBranch() == 0) {
            atbTmpAction(() -> {
                List<AbstractMonster> tmp = getAllLivingMstrs();
                int size = tmp.size();
                int finalDamage = damage / size;
                for (AbstractMonster m : tmp) {
                    addToTop(DamageAction(m, s, finalDamage, AbstractGameAction.AttackEffect.FIRE));
                }
            });
        } else if (finalBranch() == 1) {
            addToBot(DamageAllEnemiesAction(s, AbstractGameAction.AttackEffect.FIRE));
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
                upgradeDamage(8);
            });
            add(() -> {
                upgradeTexts(1);
                upgradeDamage(-3);
                setPromosValue(4, true);
                setMagical(true);
                isMultiDamage = true;
            });
        }};
    }
}
