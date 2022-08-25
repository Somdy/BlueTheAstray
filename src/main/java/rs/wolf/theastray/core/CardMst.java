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
import rs.wolf.theastray.cards.AstrayExtCard;
import rs.wolf.theastray.cards.AstrayProCard;
import rs.wolf.theastray.cards.exts.E89;
import rs.wolf.theastray.cards.pros.StrikeTA;
import rs.wolf.theastray.patches.TACardEnums;
import rs.wolf.theastray.utils.TAUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class CardMst {
    public static final List<AstrayCard> PROS = new ArrayList<>();
    public static final List<AstrayCard> EXTS = new ArrayList<>();
    public static final List<AstrayCard> MAGICS = new ArrayList<>();
    
    public static void RegisterCards() {
        new AutoAdd(Leader.MOD_ID)
                .packageFilter(StrikeTA.class)
                .any(AstrayProCard.class, (i, c) -> {
                    addProCard(c);
                    BaseMod.addCard(c.makeCopy());
                    TAUtils.Log("[" + c.name + "] added");
                });
        new AutoAdd(Leader.MOD_ID)
                .packageFilter(E89.class)
                .any(AstrayExtCard.class, (i, c) -> {
                    addExtCard(c);
                    BaseMod.addCard(c.makeCopy());
                    TAUtils.Log("[" + c.name + "] added");
                });
    }
    
    private static void addProCard(AstrayProCard card) {
        PROS.add(card);
        if (card.hasTag(TACardEnums.MAGICAL))
            MAGICS.add(card);
        UnlockTracker.unlockCard(card.cardID);
    }
    
    private static void addExtCard(AstrayExtCard card) {
        EXTS.add(card);
        if (card.hasTag(TACardEnums.MAGICAL))
            MAGICS.add(card);
        UnlockTracker.unlockCard(card.cardID);
    }
    
    public static AbstractCard ReturnRndMagicInCombat(@NotNull Random rng, boolean extIncluded, Predicate<AbstractCard> expt) {
        List<AstrayCard> tmp = new ArrayList<>();
        MAGICS.stream().filter(c -> (extIncluded && c.canSpawnInCombat()) || !EXTS.contains(c)).forEach(tmp::add);
        if (tmp.isEmpty()) return new Madness();
        int index = rng.random(tmp.size() - 1);
        return tmp.get(index).makeCopy();
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