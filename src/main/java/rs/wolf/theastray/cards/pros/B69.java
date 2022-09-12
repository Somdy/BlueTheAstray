package rs.wolf.theastray.cards.pros;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.lazymankits.actions.utility.SimpleXCostActionBuilder;
import rs.wolf.theastray.actions.commons.ModifyManaAction;
import rs.wolf.theastray.cards.AstrayProCard;
import rs.wolf.theastray.powers.MagicPower;
import rs.wolf.theastray.utils.GlobalManaMst;

public class B69 extends AstrayProCard {
    public B69() {
        super(69, -1, CardTarget.SELF);
        exhaust = true;
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new SimpleXCostActionBuilder(freeToPlayOnce, energyOnUse, upgraded)
                .addEffect((i, e) -> e)
                .addAction(e -> {
                    if (upgraded) {
                        addToTop(new DrawCardAction(s, e));
                    }
                    int currMana = GlobalManaMst.CurrentMana();
                    int overflow = (currMana + e) - GlobalManaMst.MaxMana();
                    if (overflow > 0) {
                        addToTop(ApplyPower(s, s, new MagicPower(s, overflow)));
                    }
                    addToTop(new ModifyManaAction(e));
                    addToTop(new GainEnergyAction(currMana));
                })
                .build());
    }
    
    @Override
    public void selfUpgrade() {
        upgradeTexts();
    }
}
