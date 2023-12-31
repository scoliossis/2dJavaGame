package scoliosis.Options;


public class Configs {

    @Property(type = Property.Type.BOOLEAN, name = "no sprint mode", description = "when in levels, you will be UNABLE to use sprint (is possible to do the campaign through \"bugs\", hf)")
    public static boolean noSprint = false;
    @Property(type = Property.Type.BOOLEAN, name = "no jump mode", description = "when in levels, you will be UNABLE to use jump (gl ;3 idk if this is possible for the campaign)")
    public static boolean noJump = false;

    @Property(type = Property.Type.BOOLEAN, name = "hard mode", description = "no gaining lives after level completion in campaign")
    public static boolean hardMode = false;

    @Property(type = Property.Type.BOOLEAN, name = "hardcore", description = "1 life, no gaining lives after level completion in campaign")
    public static boolean hardcore = false;

    @Property(type = Property.Type.BOOLEAN, name = "collector", description = "to complete the level all 3 stars are required AND touching the podium kills you :trol:")
    public static boolean collector = false;
}
