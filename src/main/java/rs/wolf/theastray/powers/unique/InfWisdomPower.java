package rs.wolf.theastray.powers.unique;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.screens.CombatRewardScreen;
import com.megacrit.cardcrawl.ui.buttons.ProceedButton;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import javassist.CtBehavior;
import rs.lazymankits.utils.LMSK;
import rs.wolf.theastray.abstracts.AstrayPower;
import rs.wolf.theastray.patches.TACardEnums;
import rs.wolf.theastray.utils.TAUtils;

import java.util.ArrayList;
import java.util.Arrays;

import static rs.wolf.theastray.powers.unique.InfWisdomPower.CombatRewardScreenPatch.getOtherColorCards;

public class InfWisdomPower extends AstrayPower {
    public static final String ID = TAUtils.MakeID("InfWisdomPower");
    protected final ArrayList<RewardItem> rewardItems = new ArrayList<>();
    
    public InfWisdomPower(int amount) {
        super(ID, "unawakened", PowerType.BUFF, LMSK.Player());
        setValues(amount);
        preloadString(s -> setAmtValue(0, this.amount));
        updateDescription();
    }
    
    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        if (stackAmount > 0 && owner.isPlayer) {
            AbstractCard.CardColor color = LMSK.Player().getCardColor();
            AbstractCard.CardColor[] colors = color == TACardEnums.TA_CardColor ? 
                    new AbstractCard.CardColor[]{color, TACardEnums.TAE_CardColor} : new AbstractCard.CardColor[]{color};
            for (int i = 0; i < stackAmount; i++) {
                rewardItems.add(new RewardItem(){{
                    cards = getOtherColorCards(3, colors);
                }});
            }
        }
    }
    
    @Override
    public void onInitialApplication() {
        if (owner.isPlayer) {
            rewardItems.clear();
            AbstractCard.CardColor color = LMSK.Player().getCardColor();
            AbstractCard.CardColor[] colors = color == TACardEnums.TA_CardColor ?
                    new AbstractCard.CardColor[]{color, TACardEnums.TAE_CardColor} : new AbstractCard.CardColor[]{color};
            rewardItems.add(new RewardItem(){{
                cards = getOtherColorCards(3, colors);
            }});
        }
    }
    
    @Override
    public AbstractPower makeCopy() {
        return new InfWisdomPower(amount);
    }
    
//    @SpirePatch2(clz = AbstractDungeon.class, method = "getRewardCards")
//    public static class InfWisdomPatch {
//        @SpirePostfixPatch
//        public static void Postfix(ArrayList<AbstractCard> __result) {
//            AbstractPlayer p = LMSK.Player();
//            if (p.hasPower(InfWisdomPower.ID)) {
//                AbstractPower power = p.getPower(InfWisdomPower.ID);
//                if (power != null) {
//                    int extras = power.amount;
//                    AbstractCard another = null;
//                    for (int i = 0; i < extras; i++) {
//                        AbstractCard.CardRarity rarity = AbstractDungeon.rollRarity();
//                        another = getAnyOtherColorCard(rarity, (another != null ? another.cardID : null));
//                        another.upgrade();
//                        __result.add(another);
//                    }
//                }
//            }
//        }
//    }
    @SpirePatch2(clz = CombatRewardScreen.class, method = "setupItemReward")
    public static class CombatRewardScreenPatch {
        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(CombatRewardScreen __instance) {
            AbstractPlayer p = LMSK.Player();
            if (p.hasPower(InfWisdomPower.ID)) {
                AbstractPower power = p.getPower(InfWisdomPower.ID);
                if (power != null) {
                    ArrayList<RewardItem> rewards = __instance.rewards;
                    rewards.addAll(((InfWisdomPower) power).rewardItems);
                }
            }
        }
        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.MethodCallMatcher matcher = new Matcher.MethodCallMatcher(ProceedButton.class, "show");
                return LineFinder.findInOrder(ctBehavior, matcher);
            }
        }
        protected static ArrayList<AbstractCard> getOtherColorCards(int amount, AbstractCard.CardColor... colors) {
            for (AbstractRelic r : LMSK.Player().relics) {
                amount = r.changeNumberOfCardsInReward(amount);
            }
            ArrayList<AbstractCard> result = new ArrayList<>();
            AbstractCard reward = null;
            for (int i = 0; i < amount; i++) {
                AbstractCard.CardRarity rarity = AbstractDungeon.rollRarity();
                reward = getAnyOtherColorCard(rarity, (reward != null ? reward.cardID : null), colors);
                float upgradeChance = ReflectionHacks.getPrivate(null, AbstractDungeon.class, "cardUpgradedChance");
                if (reward.rarity != AbstractCard.CardRarity.RARE && AbstractDungeon.cardRng.randomBoolean(upgradeChance)
                        && reward.canUpgrade()) {
                    reward.upgrade();
                }
                for (AbstractRelic r : LMSK.Player().relics) {
                    r.onPreviewObtainCard(reward);
                }
                result.add(reward);
            }
            return result;
        }
    }
    
    private static AbstractCard getAnyOtherColorCard(AbstractCard.CardRarity rarity, String except, AbstractCard.CardColor... colors) {
        CardGroup cards = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        CardLibrary.cards.forEach((id, card) -> {
            if (card.rarity == rarity && Arrays.stream(colors).noneMatch(c -> c == card.color)
                    && card.type != AbstractCard.CardType.CURSE && card.type != AbstractCard.CardType.STATUS
                    && (!UnlockTracker.isCardLocked(id) || Settings.treatEverythingAsUnlocked())) {
                if (except == null || !except.equals(card.cardID))
                    cards.addToBottom(card);
            }
        });
        return cards.getRandomCard(true).makeCopy();
    }
}