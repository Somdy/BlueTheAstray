package rs.wolf.theastray.localizations;

import org.jetbrains.annotations.NotNull;

public class TACardLocals {
    protected boolean DEPRECATED = false;
    public String ID;
    public String NAME;
    public String DESCRIPTION;
    public String[] UPGRADED_DESC;
    public String[] UPDATED_DESC;
    public String[] MSG;
    
    @NotNull
    public static TACardLocals GetMockingLocals(String ID) {
        TACardLocals locals = new TACardLocals();
        locals.ID = ID;
        locals.NAME = "Missing [" + ID + "] Name";
        locals.DESCRIPTION = "Missing [" + ID + "] Description";
        locals.UPGRADED_DESC = new String[]{"Missing_Upgrade_0", "Missing_Upgrade_1"};
        locals.UPDATED_DESC = new String[]{"Missing_0", "Missing_1", "Missing_2"};
        locals.MSG = new String[]{"Missing_0", "Missing_1", "Missing_2"};
        return locals;
    }
    
    @NotNull
    public static TACardLocals GetMockingLocals() {
        return GetMockingLocals("UNDEFINED");
    }
}