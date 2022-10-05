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
        setBlockValue(8, true);
        setMagicValue(1, true);
        setCanEnlighten(true);
    }
    
    @Override
    protected void beforePlaying(AbstractCreature s, AbstractCreature t) {
        if (!upgraded || finalBranch() == 0) {
            if (GlobalManaMst.CurrentMana() >= 2)
                exhaustOnUseOnce = true;
        }
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new GainBlockAction(s, block));
        if (!upgraded || finalBranch() == 0) {
            atbTmpAction(() -> {
                if (GlobalManaMst.CurrentMana() >= 2)
                    addToTop(ApplyPower(s, s, new MagicPower(s, magicNumber)));
            });
        } else if (finalBranch() == 1) {
            addToBot(new DrawCardAction(s, magicNumber));
        }
    }
    
    @Override
    public void triggerOnGlowCheck() {
        if (!upgraded || finalBranch() == 0) {
            glowColor = GlobalManaMst.CurrentMana() >= 2 ? GOLD_BORDER_GLOW_COLOR : BLUE_BORDER_GLOW_COLOR;
        } else {
            glowColor = BLUE_BORDER_GLOW_COLOR;
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
                upgradeBaseCost(0);
                setMagicValue(0, true);
                setMagicalDerivative(true);
            });
        }};
    }
}