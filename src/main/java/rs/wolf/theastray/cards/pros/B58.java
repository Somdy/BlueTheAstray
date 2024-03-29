package rs.wolf.theastray.cards.pros;

import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.wolf.theastray.cards.AstrayProCard;
import rs.wolf.theastray.powers.unique.ImbalancePower;

public class B58 extends AstrayProCard {
    public B58() {
        super(58, 1, CardTarget.SELF);
        setMagicValue(5, true);
        setExtraMagicValue(3, true);
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        atbTmpAction(() -> {
            if (s.powers.stream().noneMatch(p -> p instanceof ImbalancePower)) {
                addToTop(ApplyPower(s, s, new ImbalancePower(magicNumber, getExtraMagic())));
            } else {
                s.powers.stream().filter(p -> p instanceof ImbalancePower)
                        .findFirst()
                        .map(p -> (ImbalancePower) p)
                        .ifPresent(p -> {
                            int discards = p.extraAmt;
                            if (discards > getExtraMagic()) {
                                p.upgrade(getExtraMagic());
                            } else {
                                p.advance();
                            }
                        });
            }
        });
    }
    
    @Override
    public void selfUpgrade() {
        upgradeTexts();
        upgradeExtraMagic(-1);
    }
}