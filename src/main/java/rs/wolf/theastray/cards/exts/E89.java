package rs.wolf.theastray.cards.exts;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import rs.lazymankits.actions.common.ApplyPowerToEnemiesAction;
import rs.lazymankits.enums.ApplyPowerParam;
import rs.wolf.theastray.cards.AstrayExtCard;

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
            addToBot(DamageAction(m, s, AbstractGameAction.AttackEffect.FIRE));
        });
    }
    
    @Override
    public void selfUpgrade() {
        upgradeTexts();
        upgradeDamage(2);
    }
}