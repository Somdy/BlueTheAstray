package rs.wolf.theastray.powers.unique;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import rs.lazymankits.utils.LMSK;
import rs.wolf.theastray.abstracts.AstrayPower;
import rs.wolf.theastray.patches.TACardEnums;
import rs.wolf.theastray.utils.TAUtils;

import java.util.ArrayList;

public class InfWisdomPower extends AstrayPower {
    public static final String ID = TAUtils.MakeID("InfWisdomPower");
    
    public InfWisdomPower(int amount) {
        super(ID, "unawakened", PowerType.BUFF, LMSK.Player());
        setValues(amount);
        preloadString(s -> setAmtValue(0, this.amount));
        updateDescription();
    }
    
    @Override
    public AbstractPower makeCopy() {
        return new InfWisdomPower(amount);
    }
    
    @SpirePatch2(clz = AbstractDungeon.class, method = "getRewardCards")
    public static class InfWisdomPatch {
        @SpirePostfixPatch
        public static void Postfix(ArrayList<AbstractCard> __result) {
            AbstractPlayer p = LMSK.Player();
            if (p.hasPower(InfWisdomPower.ID)) {
                AbstractPower power = p.getPower(InfWisdomPower.ID);
                if (power != null) {
                    int extras = power.amount;
                    AbstractCard another = null;
                    for (int i = 0; i < extras; i++) {
                        AbstractCard.CardRarity rarity = AbstractDungeon.rollRarity();
                        another = getAnyOtherColorCard(rarity, (another != null ? another.cardID : null));
                        __result.add(another);
                    }
                }
            }
        }
        
        private static AbstractCard getAnyOtherColorCard(AbstractCard.CardRarity rarity, String except) {
            CardGroup cards = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            CardLibrary.cards.forEach((id, card) -> {
                if (card.rarity == rarity && card.color != TACardEnums.TA_CardColor && card.color != TACardEnums.TAE_CardColor
                        && card.type != AbstractCard.CardType.CURSE && card.type != AbstractCard.CardType.STATUS
                        && (!UnlockTracker.isCardLocked(id) || Settings.treatEverythingAsUnlocked())) {
                    if (except == null || !except.equals(card.cardID)) 
                        cards.addToBottom(card);
                }
            });
            return cards.getRandomCard(true).makeCopy();
        }
    }
}