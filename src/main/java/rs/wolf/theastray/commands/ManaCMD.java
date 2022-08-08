package rs.wolf.theastray.commands;

import basemod.DevConsole;
import basemod.devcommands.ConsoleCommand;
import basemod.helpers.ConvertHelper;
import rs.wolf.theastray.utils.GlobalManaMst;

import java.util.ArrayList;

public class ManaCMD extends ConsoleCommand {
    @Override
    protected void execute(String[] tokens, int depth) {
        if (tokens.length < 1) {
            cmdEnergyHelp();
            return;
        }
        if (tokens[1].equalsIgnoreCase("add") && tokens.length > 2) {
            GlobalManaMst.GainMana(ConvertHelper.tryParseInt(tokens[2], 0));
        } else if (tokens[1].equalsIgnoreCase("lose") && tokens.length > 2) {
            GlobalManaMst.LoseMana(ConvertHelper.tryParseInt(tokens[2], 0));
        } else {
            cmdEnergyHelp();
        }
    }
    
    @Override
    protected ArrayList<String> extraOptions(String[] tokens, int depth) {
        ArrayList<String> result = new ArrayList<>();
        result.add("add");
        result.add("lose");
        if (tokens[depth].equalsIgnoreCase("add") || tokens[depth].equalsIgnoreCase("lose")) {
            if (tokens.length > depth + 1 && tokens[depth + 1].matches("\\d+"))
                complete = true;
            result = smallNumbers();
        }
        return result;
    }
    
    @Override
    protected void errorMsg() {
        cmdEnergyHelp();
    }
    
    private static void cmdEnergyHelp() {
        DevConsole.couldNotParse();
        DevConsole.log("options are:");
        DevConsole.log("* add [amt]");
        DevConsole.log("* lose [amt]");
    }
}