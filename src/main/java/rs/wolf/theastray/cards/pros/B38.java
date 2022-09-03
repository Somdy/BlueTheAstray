package rs.wolf.theastray.cards.pros;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.InstantKillAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.lazymankits.actions.utility.DamageCallbackBuilder;
import rs.wolf.theastray.cards.AstrayProCard;
import rs.wolf.theastray.powers.MagicPower;

public class B38 extends AstrayProCard {
    public B38() {
        super(38, 1, CardTarget.ENEMY);
        setDamageValue(6, true);
        setMagicValue(6, true);
        setPromosValue(1, true);
        setMagical(true);
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        if (!upgraded) {
            addToBot(new DamageCallbackBuilder(t, crtDmgInfo(s, damage, damageTypeForTurn), 
                    AbstractGameAction.AttackEffect.SLASH_HORIZONTAL, m -> {
                if (s.hasPower(MagicPower.ID)) {
                    int threshold = s.getPower(MagicPower.ID).amount * magicNumber;
                    if (threshold > m.currentHealth)
                        addToTop(new InstantKillAction(m));
                }
            }));
        } else {
            addToBot(DamageAllEnemiesAction(s, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL, m -> {
                if (s.hasPower(MagicPower.ID)) {
                    int threshold = s.getPower(MagicPower.ID).amount * magicNumber;
                    if (threshold > m.currentHealth)
                        addToTop(new InstantKillAction(m));
                }
            }));
        }
    }
    
    @Override
    public void selfUpgrade() {
        upgradeTexts();
        target = CardTarget.ALL_ENEMY;
        isMultiDamage = true;
    }
}