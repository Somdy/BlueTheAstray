package rs.wolf.theastray.abstracts;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.lazymankits.actions.common.NullableSrcDamageAction;
import rs.lazymankits.utils.LMSK;
import rs.wolf.theastray.utils.TAUtils;

public abstract class AstrayMonster extends AbstractMonster implements TAUtils {
    protected MonsterStrings strings;
    protected String NAME;
    protected String[] MOVES;
    protected String[] DIALOG;
    
    public AstrayMonster(String ID, int maxHealth, float hb_x, float hb_y, float hb_w, float hb_h, String imgUrl, float offsetX, float offsetY) {
        super(TAUtils.MonsterStrings(ID).NAME, ID, maxHealth, hb_x, hb_y, hb_w, hb_h, imgUrl, offsetX, offsetY);
        strings = TAUtils.MonsterStrings(ID);
        NAME = strings.NAME;
        MOVES = strings.MOVES;
        DIALOG = strings.DIALOG;
    }
    
    protected boolean hardTime(int lv) {
        return ascenLv() >= lv;
    }
    
    @Override
    public final void takeTurn() {
        selfTakeTurn();
    }
    
    protected abstract void selfTakeTurn();
    
    protected AbstractPlayer cpr() {
        return AbstractDungeon.player;
    }
    
    protected DamageAction DamageAction(AbstractCreature t, DamageInfo info, AbstractGameAction.AttackEffect effect) {
        return new DamageAction(t, info, effect);
    }
}
