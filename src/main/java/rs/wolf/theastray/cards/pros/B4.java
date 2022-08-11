package rs.wolf.theastray.cards.pros;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.lazymankits.interfaces.cards.UpgradeBranch;
import rs.wolf.theastray.cards.AstrayProCard;
import rs.wolf.theastray.powers.MagicPower;
import rs.wolf.theastray.utils.GlobalManaMst;

import java.util.ArrayList;
import java.util.List;

public class B4 extends AstrayProCard {
    public B4() {
        super(4, 1, CardTarget.SELF);
        setMagicValue(1, true);
        setCanEnlighten(true);
        exhaust = true;
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        if (!upgraded || finalBranch() == 0) {
            atbTmpAction(() -> {
                if (GlobalManaMst.CurrentMana() >= 2)
                    addToTop(ApplyPower(s, s, new MagicPower(s, magicNumber)));
            });
            return;
        }
        if (finalBranch() == 1) {
            addToBot(new GainBlockAction(s, block));
            addToBot(new DrawCardAction(s, magicNumber));
        }
    }
    
    @Override
    public void triggerOnGlowCheck() {
        glowColor = GlobalManaMst.CurrentMana() >= 2 ? AbstractCard.GOLD_BORDER_GLOW_COLOR : AbstractCard.BLUE_BORDER_GLOW_COLOR;
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
                setMagical(true);
            });
            add(() -> {
                upgradeTexts(1);
                setMagicValue(0, true);
                setBlockValue(8, true);
                setMagicalDerivative(true);
                exhaust = false;
            });
        }};
    }
}
