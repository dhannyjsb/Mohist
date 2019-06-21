package red.mohist.util;

public class CauldronHooks {

    // Some mods such as Twilight Forest listen for specific events as their WorldProvider loads to hotload its dimension. This prevents this from happening so MV can create worlds using the same provider without issue.
    public static boolean craftWorldLoading = false;
}
