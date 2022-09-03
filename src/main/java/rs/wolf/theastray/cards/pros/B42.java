package rs.wolf.theastray.cards.pros;

import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import rs.lazymankits.actions.tools.GridCardManipulator;
import rs.lazymankits.actions.utility.SimpleGridCardSelectBuilder;
import rs.lazymankits.interfaces.cards.UpgradeBranch;
import rs.wolf.theastray.cards.AstrayProCard;
import rs.wolf.theastray.patches.TACardEnums;

import java.util.ArrayList;
import java.util.List;

public class B42 extends AstrayProCard {
    public B42() {
        super(42, 0, CardTarget.SELF);
        setMagicValue(8, true);
        setExtraMagicValue(12, true);
        setCanEnlighten(true);
        exhaust = true;
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new LoseHPAction(s, s, magicNumber));
        attTmpAction(() -> {
            CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            List<AbstractCard> tmp = new ArrayList<>();
            CardLibrary.cards.forEach((id, card) -> {
                if (isValidCard(card)) {
                    AbstractCard copy = card.makeCopy();
                    if (this.upgraded && finalBranch() == 0)
                        copy.upgrade();
                    tmp.add(copy);
                }
            });
            if (!tmp.isEmpty()) {
                int num = Math.min(tmp.size(), getExtraMagic());
                for (int i = 0; i < num; i++) {
                    AbstractCard card = tmp.remove(cardRandomRng().random(tmp.size() - 1));
                    group.addToRandomSpot(card);
                }
            }
            log("Empty card group? " + group.isEmpty());
            if (!group.isEmpty()) {
                log("Empty card group? " + group.isEmpty());
                addToTop(new SimpleGridCardSelectBuilder(c -> true)
                        .setMsg(MSG[0]).setCardGroup(group).setAmount(1)
                        .setCanCancel(false).setAnyNumber(false)
                        .setManipulator(new GridCardManipulator() {
                            @Override
                            public boolean manipulate(AbstractCard card, int i, CardGroup cardGroup) {
                                addToTop(new NewQueueCardAction(card, true, true, true));
                                return false;
                            }
                        }));
            }
        });
    }
    
    private boolean isValidCard(AbstractCard card) {
        boolean otherColor = card.color != this.color && card.color != TACardEnums.TAE_CardColor;
        boolean isPower = isCardTypeOf(card, CardType.POWER);
        return isPower && otherColor;
    }
    
    @Override
    public void selfUpgrade() {
        branchingUpgrade();
    }
    
    @Override
    protected List<UpgradeBranch> branches() {
        return new ArrayList<UpgradeBranch>() {{
            add(() -> upgradeTexts());
            add(() -> {
                upgradeTexts(1);
                upgradeMagicNumber(-7);
            });
        }};
    }
}