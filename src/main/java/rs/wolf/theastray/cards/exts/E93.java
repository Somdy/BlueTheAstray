package rs.wolf.theastray.cards.exts;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.WeakPower;
import rs.lazymankits.actions.utility.DamageCallbackBuilder;
import rs.wolf.theastray.cards.AstrayExtCard;

public class E93 extends AstrayExtCard {
    public E93() {
        super(93, 1, 1, CardTarget.ENEMY);
        setDamageValue(7, true);
        setMagicValue(2, true);
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new DamageCallbackBuilder(t, crtDmgInfo(s, damage, damageTypeForTurn), 
                AbstractGameAction.AttackEffect.SLASH_HORIZONTAL, c -> {
            if (c.lastDamageTaken <= 0) {
                if (!upgraded) {
                    addToTop(ApplyPower(t, s, new WeakPower(t, magicNumber, false)));
                } else {
                    for (int i = 0; i < getExtraMagic(); i++) {
                        addToTop(ApplyPower(t, s, new WeakPower(t, magicNumber, false)));
                    }
                }
            }
        }));
    }
    
    @Override
    public void selfUpgrade() {
        upgradeTexts();
        setExtraMagicValue(1, true);
    }
}
