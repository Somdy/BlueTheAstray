package rs.wolf.theastray.cards.pros;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.lazymankits.actions.utility.DamageCallbackBuilder;
import rs.wolf.theastray.actions.commons.ModifyManaAction;
import rs.wolf.theastray.cards.AstrayProCard;
import rs.wolf.theastray.utils.GlobalManaMst;
import rs.wolf.theastray.vfx.combat.ManaShotEffect;

public class B45 extends AstrayProCard {
    public B45() {
        super(45, 2, CardTarget.ENEMY);
        setDamageValue(7, true);
//        setPromosValue(1, true);
        setMagical(true);
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        int currentMana = GlobalManaMst.CurrentMana();
        addToBot(new ModifyManaAction(-currentMana));
        if (currentMana > 0) {
            addToBot(new VFXAction(new ManaShotEffect(s.hb.cX, s.hb.cY, t.hb.cX, t.hb.cY, currentMana), 0.25F));
            for (int i = 0; i < currentMana; i++) {
                addToBot(new DamageCallbackBuilder(t, crtDmgInfo(s, damage, damageTypeForTurn), 
                        AbstractGameAction.AttackEffect.BLUNT_LIGHT, c -> {
                    if ((c.currentHealth <= 0 || c.isDying) && !c.halfDead)
                        addToTop(new ModifyManaAction(currentMana));
                }));
            }
        }
    }
    
    @Override
    public void selfUpgrade() {
        upgradeTexts();
        upgradeDamage(3);
    }
    
    @Override
    protected int selfModifyManaOnUse(int manaCost) {
        return 0;
    }
}