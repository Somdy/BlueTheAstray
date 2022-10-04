package rs.wolf.theastray.cards.pros;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import rs.lazymankits.actions.common.DrawExptCardAction;
import rs.lazymankits.actions.utility.QuickAction;
import rs.lazymankits.interfaces.cards.UpgradeBranch;
import rs.wolf.theastray.cards.AstrayProCard;
import rs.wolf.theastray.powers.MagicPower;
import rs.wolf.theastray.powers.TurnMagicPower;
import rs.wolf.theastray.utils.TAUtils;

import java.util.ArrayList;
import java.util.List;

public class B9 extends AstrayProCard {
    public B9() {
        super(9, 1, CardTarget.ALL_ENEMY);
        setMagicValue(2, true);
        setExtraMagicValue(2, true);
        setCanEnlighten(true);
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        if (!upgraded) {
            addToBot(new DrawCardAction(s, magicNumber));
        } else {
            CardType type = finalBranch() == 0 ? CardType.ATTACK : CardType.SKILL;
            addToBot(new DrawExptCardAction(s, magicNumber, c -> isCardTypeOf(c, type)));
        }
        atbTmpAction(() -> {
            if (DrawCardAction.drawnCards.stream().anyMatch(TAUtils::IsMagical)) {
                addToTop(ApplyPower(s, s, new TurnMagicPower(s, getExtraMagic())));
                addToTop(ApplyPower(s, s, new MagicPower(s, getExtraMagic())));
            }
        });
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
                upgradeMagicNumber(1);
            });
            add(() -> {
                upgradeTexts(1);
                upgradeMagicNumber(1);
            });
        }};
    }
}