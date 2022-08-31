package rs.wolf.theastray.cards.pros;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import rs.lazymankits.interfaces.cards.UpgradeBranch;
import rs.wolf.theastray.cards.AstrayProCard;
import rs.wolf.theastray.powers.MagicPower;

import java.util.ArrayList;
import java.util.List;

public class B44 extends AstrayProCard {
    public B44() {
        super(44, 1, CardTarget.SELF);
        setMagicValue(3, true);
        setExtraMagicValue(3, true);
        setCanEnlighten(true);
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        addToBot(ApplyPower(s, s, new MagicPower(s, -1)));
        if (!upgraded) {
            AbstractPower p = cardRandomRng().randomBoolean() ? new StrengthPower(s, magicNumber)
                    : new DexterityPower(s, getExtraMagic());
            addToBot(ApplyPower(s, s, p));
        } else if (finalBranch() == 0) {
            addToBot(ApplyPower(s, s, new StrengthPower(s, magicNumber)));
            addToBot(ApplyPower(s, s, new DexterityPower(s, magicNumber)));
        } else if (finalBranch() == 1) {
            AbstractPower p = cardRandomRng().randomBoolean() ? new StrengthPower(s, magicNumber)
                    : new DexterityPower(s, magicNumber);
            addToBot(ApplyPower(s, s, p));
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
                upgradeMagicNumber(-1);
                upgradeExtraMagic(-1);
            });
            add(() -> {
                upgradeTexts(1);
                upgradeMagicNumber(1);
                shuffleBackIntoDrawPile = true;
            });
        }};
    }
}