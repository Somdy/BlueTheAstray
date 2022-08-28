package rs.wolf.theastray.core;

import basemod.AutoAdd;
import basemod.BaseMod;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.colorless.Madness;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import org.jetbrains.annotations.NotNull;
import rs.lazymankits.utils.LMSK;
import rs.wolf.theastray.abstracts.AstrayCard;
import rs.wolf.theastray.cards.AstrayColorlessCard;
import rs.wolf.theastray.cards.AstrayExtCard;
import rs.wolf.theastray.cards.AstrayProCard;
import rs.wolf.theastray.cards.colorless.C88;
import rs.wolf.theastray.cards.exts.E89;
import rs.wolf.theastray.cards.pros.StrikeTA;
import rs.wolf.theastray.data.CardData;
import rs.wolf.theastray.data.DataMst;
import rs.wolf.theastray.patches.TACardEnums;
import rs.wolf.theastray.utils.TAUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class CardMst {
    private static final List<String> PROS = new ArrayList<>();
    private static final List<String> EXTS = new ArrayList<>();
    private static final List<String> MAGICS = new ArrayList<>();
    private static final List<String> MISC = new ArrayList<>();
    private static final Map<CardData, AstrayCard> CARD_MAP = new HashMap<>();
    
    public static void RegisterCards() {
        new AutoAdd(Leader.MOD_ID)
                .packageFilter(StrikeTA.class)
                .any(AstrayProCard.class, (i, c) -> {
                    addProCard(c);
                    registerCard(c);
                });
        new AutoAdd(Leader.MOD_ID)
                .packageFilter(E89.class)
                .any(AstrayExtCard.class, (i, c) -> {
                    addExtCard(c);
                    registerCard(c);
                });
        new AutoAdd(Leader.MOD_ID)
                .packageFilter(C88.class)
                .any(AstrayColorlessCard.class, (i, c) -> {
                    addMiscCard(c);
                    registerCard(c);
                });
    }
    
    private static void registerCard(@NotNull AbstractCard card) {
        BaseMod.addCard(card.makeCopy());
        TAUtils.Log("[" + card.name + "] added");
    }
    
    private static void addProCard(@NotNull AstrayProCard card) {
        PROS.add(card.data.getInternalID());
        CARD_MAP.put(card.data, card);
        if (card.hasTag(TACardEnums.MAGICAL)) MAGICS.add(card.data.getInternalID());
        UnlockTracker.unlockCard(card.cardID);
    }
    
    private static void addExtCard(@NotNull AstrayExtCard card) {
        EXTS.add(card.data.getInternalID());
        CARD_MAP.put(card.data, card);
        if (card.hasTag(TACardEnums.MAGICAL)) MAGICS.add(card.data.getInternalID());
        UnlockTracker.unlockCard(card.cardID);
    }
    
    private static void addMiscCard(@NotNull AstrayCard card) {
        MISC.add(card.data.getInternalID());
        CARD_MAP.put(card.data, card);
        if (card.hasTag(TACardEnums.MAGICAL)) MAGICS.add(card.data.getInternalID());
        UnlockTracker.unlockCard(card.cardID);
    }
    
    @NotNull
    public static List<AstrayCard> GetAllCards(BiPredicate<CardData, AstrayCard> filter) {
        List<AstrayCard> tmp = new ArrayList<>();
        CARD_MAP.forEach(((data, card) -> {
            if (filter.test(data, card)) tmp.add((AstrayCard) card.makeCopy());
        }));
        return tmp;
    }
    
    @NotNull
    public static List<AstrayCard> GetAllCards(Predicate<AbstractCard> expt) {
        return GetAllCards((data, card) -> expt.test(card));
    }
    
    @NotNull
    public static List<AstrayCard> GetAllCards() {
        return GetAllCards((d, c) -> true);
    }
    
    public static AbstractCard GetCard(int index) {
        CardData data = DataMst.GetCardData(index);
        if (CARD_MAP.containsKey(data))
            return CARD_MAP.get(data).makeCopy();
        return new Madness();
    }
    
    public static AbstractCard GetCard(String localname) {
        CardData data = DataMst.GetCardData(localname);
        if (CARD_MAP.containsKey(data))
            return CARD_MAP.get(data).makeCopy();
        return new Madness();
    }
    
    public static AbstractCard ReturnRndMagicInCombat(@NotNull Random rng, boolean extIncluded, Predicate<AbstractCard> expt) {
        List<AstrayCard> tmp = GetAllCards((d, c) -> c.hasTag(TACardEnums.MAGICAL) 
                && ((extIncluded && c.canSpawnInCombat()) || !EXTS.contains(d.getInternalID())));
        tmp.removeIf(c -> !expt.test(c));
        if (tmp.isEmpty()) return new Madness();
        int index = rng.random(tmp.size() - 1);
        return tmp.get(index);
    }
    
    public static AbstractCard ReturnRndMagicInCombat(Predicate<AbstractCard> expt) {
        return ReturnRndMagicInCombat(LMSK.CardRandomRng(), true, expt);
    }
    
    public static void RegisterColors() {
        BaseMod.addColor(TACardEnums.TA_CardColor, Leader.TAColor,
                cardui("bg_attack_512"), cardui("bg_skill_512"), cardui("bg_power_512"),
                cardui("card_cost"),
                cardui("bg_attack_1024"), cardui("bg_skill_1024"), cardui("bg_power_1024"),
                cardui("card_orb"), cardui("energy_icon"));
        BaseMod.addColor(TACardEnums.TAE_CardColor, Leader.TAColor,
                cardui("bg_attack_512"), cardui("bg_skill_512"), cardui("bg_power_512"),
                cardui("card_cost"),
                cardui("bg_attack_1024"), cardui("bg_skill_1024"), cardui("bg_power_1024"),
                cardui("card_orb"), cardui("energy_icon"));
    }
    
    @NotNull
    private static String cardui(String filename) {
        return "AstrayAssets/images/cardui/" + filename + ".png";
    }
}