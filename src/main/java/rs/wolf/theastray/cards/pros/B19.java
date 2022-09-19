package rs.wolf.theastray.cards.pros;

import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import org.jetbrains.annotations.NotNull;
import rs.lazymankits.actions.common.DrawExptCardAction;
import rs.lazymankits.interfaces.cards.UpgradeBranch;
import rs.wolf.theastray.abstracts.AstrayCard;
import rs.wolf.theastray.cards.AstrayProCard;
import rs.wolf.theastray.core.CardMst;
import rs.wolf.theastray.patches.TACardEnums;
import rs.wolf.theastray.utils.TAUtils;

import java.util.ArrayList;
import java.util.List;

public class B19 extends AstrayProCard {
    public B19() {
        super(19, 1, CardTarget.NONE);
        setMagicValue(1, true);
        setCanEnlighten(true);
        exhaust = true;
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        if (!upgraded) {
            if (cardRandomRng().randomBoolean()) draw();
            else give();
        } else if (finalBranch() == 0) {
            give();
        } else if (finalBranch() == 1) {
            draw();
        }
    }
    
    void give() {
        atbTmpAction(() -> {
            for (int i = 0; i < magicNumber; i++) {
                AbstractCard card = CardMst.ReturnRndMagicInCombat(this::isFreeMagicalCard);
                addToTop(new MakeTempCardInHandAction(card, 1));
            }
        });
    }
    
    void draw() {
        if (cpr().drawPile.group.stream().noneMatch(this::isFreeMagicalCard)) {
            addToBot(new TalkAction(cpr(), MSG[0], 1F, 1F));
        } else {
            addToBot(new DrawExptCardAction(magicNumber, this::isFreeMagicalCard).discardPileNotIncluded());
        }
    }
    
    boolean isFreeMagicalCard(@NotNull AbstractCard c) {
        return TAUtils.IsMagical(c) && c instanceof AstrayCard && (c.costForTurn == 0 || c.freeToPlay());
    }
    
    @Override
    public void selfUpgrade() {
        branchingUpgrade();
    }
    
    @Override
    protected List<UpgradeBranch> branches() {
        return new ArrayList<UpgradeBranch>() {{
            add(() -> {
                upgradeTexts();
                setMagical(true);
            });
            add(() -> {
                upgradeTexts(1);
                setMagical(true);
            });
        }};
    }
}
