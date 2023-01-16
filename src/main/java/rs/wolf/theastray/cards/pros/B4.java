package rs.wolf.theastray.cards.pros;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.lazymankits.interfaces.cards.UpgradeBranch;
import rs.wolf.theastray.cards.AstrayProCard;
import rs.wolf.theastray.powers.MagicPower;
import rs.wolf.theastray.powers.unique.AntiMagicPower;
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
        exhaustOnUseOnce = goExhaust();
    }
    
    private boolean goExhaust() {
        if (canTrigger()) {
            if (!upgraded || finalBranch() == 1) {
                log("go exhaust");
                return true;
            }
            if (finalBranch() == 0 && GlobalManaMst.CurrentMana() <= 3) {
                log("no enough mana, go exhaust");
                return true;
            }
        }
        return false;
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
            atbTmpAction(() -> {
                if (GlobalManaMst.CurrentMana() >= 3) {
                    addToTop(ApplyPower(s, s, new MagicPower(s, magicNumber)));
                    addToBot(ApplyPower(s, s, new AntiMagicPower(s)));
                }
            });
        }
    }
    
    private boolean canTrigger() {
        int cm = GlobalManaMst.CurrentMana();
        return !upgraded || finalBranch() == 0 ? cm >= 2 : cm >= 3;
    }
    
    @Override
    public void triggerOnGlowCheck() {
        glowColor = canTrigger() ? GOLD_BORDER_GLOW_COLOR : BLUE_BORDER_GLOW_COLOR;
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
            });
            add(() -> {
                upgradeTexts(1);
                upgradeMagicNumber(1);
            });
        }};
    }
}