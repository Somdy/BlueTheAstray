package rs.wolf.theastray.relics;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.lazymankits.actions.common.NullableSrcDamageAction;
import rs.wolf.theastray.abstracts.AstrayRelic;

import java.util.List;

public class Relic11 extends AstrayRelic {
    public Relic11() {
        super(11);
    }
    
    @Override
    public void onPlayerEndTurn() {
        atbTmpAction(() -> {
            List<AbstractMonster> monsters = getAllExptMstrs(m -> !m.isDeadOrEscaped() && m.getIntentBaseDmg() > 0);
            if (!monsters.isEmpty()) {
                addToTop(new NullableSrcDamageAction(cpr(), crtDmgInfo(null, 1, DamageInfo.DamageType.THORNS)));
                for (AbstractMonster m : monsters) {
                    addToTop(ApplyPower(m, cpr(), frostPower(m, cpr(), 3)));
                }
            }
        });
    }
}