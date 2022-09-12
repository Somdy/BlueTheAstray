package rs.wolf.theastray.cards.pros;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.lazymankits.actions.tools.GridCardManipulator;
import rs.lazymankits.actions.utility.SimpleGridCardSelectBuilder;
import rs.lazymankits.interfaces.cards.BranchableUpgradeCard;
import rs.lazymankits.interfaces.cards.RUM;
import rs.lazymankits.interfaces.cards.UpgradeBranch;
import rs.wolf.theastray.abstracts.AstrayCard;
import rs.wolf.theastray.cards.AstrayProCard;
import rs.wolf.theastray.powers.unique.MindBlossomPower;

import java.util.ArrayList;
import java.util.List;

public class B73 extends AstrayProCard {
    public B73() {
        super(73, 1, CardTarget.SELF);
        setCanEnlighten(true);
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        if (!upgraded || finalBranch() == 0) {
            atbTmpAction(() -> {
                List<AbstractCard> tmp = new ArrayList<>();
                List<CardGroup> groups = listFromObjs(cpr().drawPile, cpr().hand, cpr().discardPile);
                groups.forEach(g -> g.group.stream()
                        .filter(c -> c instanceof AstrayCard && ((AstrayCard) c).isEnlightenCard() && !c.upgraded)
                        .forEach(tmp::add));
                if (!tmp.isEmpty()) {
                    tmp.forEach(c -> {
                        if (c instanceof BranchableUpgradeCard) {
                            int branch = ((BranchableUpgradeCard) c).getBranchForRandomUpgrading(RUM.APOTHEOSIS);
                            ((BranchableUpgradeCard) c).setChosenBranch(branch);
                            if (inHand(c)) c.superFlash();
                            c.upgrade();
                        }
                    });
                }
            });
            if (upgraded) {
                addToBot(ApplyPower(s, s, new MindBlossomPower()));
            }
        } else if (finalBranch() == 1) {
            atbTmpAction(() -> {
                List<AbstractCard> tmp = new ArrayList<>();
                List<CardGroup> groups = listFromObjs(cpr().drawPile, cpr().hand, cpr().discardPile);
                groups.forEach(g -> g.group.stream()
                        .filter(c -> c instanceof AstrayCard && ((AstrayCard) c).isEnlightenCard() && !c.upgraded)
                        .forEach(tmp::add));
                if (!tmp.isEmpty()) {
                    tmp.forEach(c -> {
                        if (c instanceof AstrayCard) {
                            ((AstrayCard) c).setFakeRestroom(true);
                            CardGroup single = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                            single.addToTop(c);
                            addToTop(new SimpleGridCardSelectBuilder(card-> true)
                                    .setAmount(1).setCardGroup(single).setMsg(String.format(MSG[0], c.name))
                                    .setAnyNumber(false).setCanCancel(false)
                                    .setManipulator(new GridCardManipulator() {
                                        @Override
                                        public boolean manipulate(AbstractCard card, int i, CardGroup cardGroup) {
                                            card.upgrade();
                                            return false;
                                        }
                                    }));
                        }
                    });
                }
            });
        }
    }
    
    @Override
    public void selfUpgrade() {
        branchingUpgrade();
    }
    
    @Override
    protected List<UpgradeBranch> branches() {
        return new ArrayList<UpgradeBranch>(){{
            add(() -> upgradeTexts());
            add(() -> upgradeTexts(1));
        }};
    }
}