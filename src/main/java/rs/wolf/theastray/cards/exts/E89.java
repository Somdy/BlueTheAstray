package rs.wolf.theastray.cards.exts;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.wolf.theastray.cards.AstrayExtCard;
import rs.wolf.theastray.vfx.combat.FallingStarEffect;

public class E89 extends AstrayExtCard {
    public E89() {
        super(89, 0, 0, CardTarget.ALL_ENEMY);
        setDamageValue(3, true); // 设置初始伤害为 3
        setPromosValue(1, true); // 设置初始晋升为 1
        setMagicalDerivative(true); // 将该牌设为衍生魔法
        exhaust = true;
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        attTmpAction(() -> {
            AbstractMonster m = AbstractDungeon.getRandomMonster();
            if (m != null) {
                addToBot(new VFXAction(new FallingStarEffect(m)));
                addToBot(DamageAction(m, s, damage, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.FIRE));
            }
        });
    }
    
    @Override
    public void selfUpgrade() {
        upgradeTexts();
        upgradeDamage(2);
    }
}