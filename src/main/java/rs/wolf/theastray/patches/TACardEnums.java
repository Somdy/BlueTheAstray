package rs.wolf.theastray.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.CardLibrary;

public class TACardEnums {
    @SpireEnum(name = "theastray_color")
    public static AbstractCard.CardColor TA_CardColor;
    @SpireEnum(name = "theastray_color")
    public static CardLibrary.LibraryType TA_LibrType;
    
    @SpireEnum(name = "theastray_ex_color")
    public static AbstractCard.CardColor TAE_CardColor;
    @SpireEnum(name = "theastray_ex_color")
    public static CardLibrary.LibraryType TAE_LibrType;
    
    @SpireEnum(name = "theastray_character")
    public static AbstractPlayer.PlayerClass BlueTheAstray;
    
    @SpireEnum(name = "theastray_magical_tag")
    public static AbstractCard.CardTags MAGICAL;
    @SpireEnum(name = "theastray_magical_derivative_tag")
    public static AbstractCard.CardTags DE_MAGICAL;
    @SpireEnum(name = "theastray_storage_tag")
    public static AbstractCard.CardTags STORAGE;
    @SpireEnum(name = "theastray_illusion_tag")
    public static AbstractCard.CardTags ILLUSION;
    @SpireEnum(name = "theastray_return_to_hand_tag")
    public static AbstractCard.CardTags RETURN_TO_HAND;
}