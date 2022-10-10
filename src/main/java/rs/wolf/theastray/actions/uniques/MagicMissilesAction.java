package rs.wolf.theastray.actions.uniques;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import rs.lazymankits.actions.CustomDmgInfo;
import rs.lazymankits.actions.DamageSource;
import rs.wolf.theastray.abstracts.AstrayCard;
import rs.wolf.theastray.abstracts.AstrayGameAction;
import rs.wolf.theastray.commands.Cheat;
import rs.wolf.theastray.core.Leader;

import java.util.ArrayList;
import java.util.Arrays;
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
            if (monsterList.isEmpty()) {
                isDone = true;
                return;
            }
            monsterInfo = new int[monsterList.size()];
            Arrays.fill(monsterInfo, 0);
            List<AbstractMonster> tmp = new ArrayList<>(monsterList);
            for (int i = 0; i < times; i++) {
                AbstractMonster m = tmp.get(cardRandomRng().random(tmp.size() - 1));
                int slot = monsterList.indexOf(m);
                monsterInfo[slot]++;
                if (monsterInfo[slot] >= threshold) {
                    tmp.remove(m);
                }
                if (tmp.isEmpty()) break;
            }
            tmp.clear();
            calculating = false;
            if (Cheat.IsCheating(Cheat.SSD)) {
                Leader.devLog("DISPLAYING [" + card.name + "] CALCULATED RESULT:");
                monsterList.forEach(m -> {
                    int index = monsterList.indexOf(m);
                    int attackTimes = monsterInfo[index];
                    Leader.devLog("attacking [" + index + "] {" + m.name + "} [" + attackTimes + "] times");
                });
            }
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
