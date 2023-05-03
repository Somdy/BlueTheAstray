package rs.wolf.theastray.commands;

import basemod.DevConsole;
import basemod.devcommands.ConsoleCommand;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.FrozenEye;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import rs.lazymankits.utils.LMSK;
import rs.wolf.theastray.abstracts.AstrayCard;
import rs.wolf.theastray.actions.commons.ModifyManaAction;
import rs.wolf.theastray.cards.curses.C85;
import rs.wolf.theastray.cards.exts.E100;
import rs.wolf.theastray.core.CardMst;
import rs.wolf.theastray.powers.MagicPower;
import rs.wolf.theastray.utils.GlobalManaMst;
import rs.wolf.theastray.utils.TAUtils;

public class CheatCMD extends ConsoleCommand {
    @Override
    protected void execute(String[] tokens, int i) {
        if (tokens.length < 1) {
            cmdEnergyHelp();
            return;
        }
        if (tokens[1].equalsIgnoreCase("Enlightened") && tokens.length > 2) {
            boolean enabled = Boolean.parseBoolean(tokens[2]);
            Cheat.SetCheat(Cheat.IEL, enabled);
            DevConsole.log("enlightened: " + Cheat.IsCheating(Cheat.IEL));
        } else if (tokens[1].equalsIgnoreCase("ManaFree") && tokens.length > 2) {
            boolean enabled = Boolean.parseBoolean(tokens[2]);
            Cheat.SetCheat(Cheat.IMC, enabled);
            DevConsole.log("mana free: " + Cheat.IsCheating(Cheat.IMC));
        } else if (tokens[1].equalsIgnoreCase("ShowSecretDetails") && tokens.length > 2) {
            boolean enable = Boolean.parseBoolean(tokens[2]);
            Cheat.SetCheat(Cheat.SSD, enable);
            DevConsole.log("show secret details: " + Cheat.IsCheating(Cheat.SSD));
        } else if (tokens[1].equalsIgnoreCase("presettest")) {
            if (TAUtils.RoomChecker(AbstractRoom.RoomPhase.COMBAT)) {
                Cheat.SetCheat(Cheat.IEL, true);
                Cheat.SetCheat(Cheat.SSD, true);
//                LMSK.AddToBot(new ModifyManaAction(2));
                LMSK.AddToBot(new MakeTempCardInHandAction(CardMst.GetCard("引火"), 2));
                LMSK.AddToBot(new MakeTempCardInHandAction(CardMst.GetCard("开幕礼花"), 2));
            }
        }
        else {
            cmdEnergyHelp();
        }
    }
    
    @Override
    protected void errorMsg() {
        cmdEnergyHelp();
    }
    
    private static void cmdEnergyHelp() {
        DevConsole.couldNotParse();
        DevConsole.log("If you don't know how to cheat, don't cheat.");
    }
}