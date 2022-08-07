package rs.wolf.theastray.cards.pros;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.lazymankits.interfaces.cards.UpgradeBranch;
import rs.wolf.theastray.cards.AstrayProCard;

import java.util.ArrayList;
import java.util.List;

public class B3 extends AstrayProCard {
    public B3() {
        super(3, 1, CardTarget.ENEMY);
        setDamageValue(9, true);
        setCanEnlighten(true); // 将该牌设为启迪牌
        setMagical(true); // 将该牌设为魔法牌
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        addToBot(DamageAction(t, s, AbstractGameAction.AttackEffect.FIRE));
    }
    
    @Override
    public void selfUpgrade() {
        upgradeWithChosenBranch();
    }
    
    @Override
    protected List<UpgradeBranch> branches() { // 启迪牌一定要重写该方法
        return new ArrayList<UpgradeBranch>() {{
            add(() -> {
                upgradeTexts();
                upgradeDamage(6);
            });
            add(() -> {
                upgradeTexts(1);
                upgradeDamage(1);
                setMagicValue(1, true);
                setPromosValue(1, true);
            });
        }};
    }
}
