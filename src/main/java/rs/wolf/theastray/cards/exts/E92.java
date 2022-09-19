package rs.wolf.theastray.cards.exts;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;
import rs.lazymankits.interfaces.cards.UpgradeBranch;
import rs.wolf.theastray.cards.AstrayExtCard;

import java.util.ArrayList;
import java.util.List;

public class E92 extends AstrayExtCard {
    public E92() {
        super(92, 1, 1, CardTarget.ENEMY);
        setDamageValue(8, true);
        setCanEnlighten(true);
        setMagical(true);
        setStorage(true);
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new VFXAction(new LightningEffect(t.hb.cX, t.hb.cY)));
        addToBot(DamageAction(t, s, AbstractGameAction.AttackEffect.NONE));
        atbTmpAction(() -> {
            if (!cpr().hand.isEmpty()) {
                for (AbstractCard card : cpr().hand.group) {
                    if (card instanceof E92) {
                        addToTop(new NewQueueCardAction(card, true, true, true));
                    }
                }
            }
            if (upgraded && finalBranch() == 1) {
                if (!cpr().drawPile.isEmpty()) {
                    for (AbstractCard card : cpr().drawPile.group) {
                        if (card instanceof E92) {
                            addToTop(new NewQueueCardAction(card, true, true, true));
                        }
                    }
                }
                if (!cpr().discardPile.isEmpty()) {
                    for (AbstractCard card : cpr().discardPile.group) {
                        if (card instanceof E92) {
                            addToTop(new NewQueueCardAction(card, true, true, true));
                        }
                    }
                }
            }
        });
    }
    
    @Override
    public void selfUpgrade() {
        branchingUpgrade();
    }
    
    @Override
    protected List<UpgradeBranch> branches() {
        return new ArrayList<UpgradeBranch>(){{
            add(() -> {
                upgradeTexts();
                upgradeDamage(6);
            });
            add(() -> upgradeTexts(1));
        }};
    }
}