package rs.wolf.theastray.actions.uniques;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import rs.lazymankits.actions.CustomDmgInfo;
import rs.lazymankits.actions.DamageSource;
import rs.wolf.theastray.abstracts.AstrayCard;
import rs.wolf.theastray.abstracts.AstrayGameAction;

import java.util.ArrayList;
import java.util.List;

public class MagicMissilesAction extends AstrayGameAction {
    private AstrayCard card;
    private int times;
    private int threshold;
    private boolean calculating;
    private int[] monsterInfo;
    private List<AbstractMonster> monsterList;
    private List<Runnable> actions;
    
    public MagicMissilesAction(AstrayCard card, AbstractCreature source, int times, int threshold, AttackEffect effect) {
        this.card = card;
        this.source = source;
        this.times = times;
        this.threshold = threshold;
        this.attackEffect = effect;
        actions = new ArrayList<>();
        calculating = true;
        actionType = ActionType.DAMAGE;
        duration = startDuration = Settings.ACTION_DUR_FAST;
    }
    
    @Override
    public void update() {
        if (calculating) {
            monsterList = getAllLivingMstrs();
            monsterInfo = new int[monsterList.size()];
            List<AbstractMonster> tmp = new ArrayList<>(monsterList);
            for (int i = 0; i < times; i++) {
                int slot = cardRandomRng().random(monsterList.size() - 1);
                AbstractMonster m = monsterList.get(slot);
                if (tmp.contains(m))
                    monsterInfo[slot]++;
                if (monsterInfo[slot] >= threshold)
                    tmp.remove(m);
                if (tmp.isEmpty()) break;
            }
            tmp.clear();
            calculating = false;
        } else if (monsterInfo != null && !monsterList.isEmpty()) {
            for (int i = 0; i < monsterList.size(); i++) {
                AbstractMonster m = monsterList.get(i);
                if (m != null) {
                    card.applyPowers();
                    card.calculateCardDamage(m);
                    int damage = card.damage;
                    int threshold = monsterInfo[i];
                    CustomDmgInfo info = crtDmgInfo(new DamageSource(source, card), damage, card.damageTypeForTurn);
                    for (int j = 0; j < threshold; j++) {
                        effectToList(new FlashAtkImgEffect(m.hb.cX, m.hb.cY, attackEffect));
                        actions.add(() -> m.damage(info));
                    }
                }
            }
            monsterList.clear();
            monsterInfo = null;
        }
        tickDuration();
        if (isDone && !actions.isEmpty()) {
            actions.forEach(Runnable::run);
        }
    }
}
