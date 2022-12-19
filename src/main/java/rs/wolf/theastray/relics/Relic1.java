package rs.wolf.theastray.relics;

import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import rs.wolf.theastray.abstracts.AstrayRelic;
import rs.wolf.theastray.actions.commons.ModifyManaAction;
import rs.wolf.theastray.core.Leader;
import rs.wolf.theastray.ui.manalayout.ManaMst;
import rs.wolf.theastray.utils.GlobalManaMst;

public class Relic1 extends AstrayRelic {
    public Relic1() {
        super(1);
        counter = 0;
    }
    
    @Override
    public void atTurnStart() {
        counter++;
        if (counter != 0 && counter % 2 == 0) {
            counter = 0;
            addToBot(new RelicAboveCreatureAction(cpr(), this));
            addToBot(new ModifyManaAction(1));
        }
    }
    
    @Override
    protected boolean onRightClick() {
        ManaMst mst = GlobalManaMst.GetPlayerManaMst(cpr());
        if (mst.currLayout() == ManaMst.Layout.ORBIT) {
            mst.changeLayout(ManaMst.Layout.REVOLUTION);
        } else {
            mst.changeLayout(ManaMst.Layout.ORBIT);
        }
        return super.onRightClick();
    }
    
    @Override
    public void onUnequip() {
        GlobalManaMst.ChangeLayout(ManaMst.Layout.REVOLUTION);
    }
}