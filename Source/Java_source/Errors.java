/*
This class contains all the errors that can be thrown in the GDec algorithm.
They are encapsulated in void-returning functions so the code that uses them can be more readable.
*/

public class Errors {

    public static void noMain () {
        throw new Error ("No item was given to decompose !") ;
    }

    // This error is thrown when the first line of the config file is not a beacon
    // It will only be thrown if the user tinkers with said config file, or if a cosmic particle alters it.
    // If the error comes from the user, they are reminded to check the config file's syntax.
    public static void firstLineNotABeacon () {
        String e = "First line of the file should be a beacon !\n" +
        "Please read README.md for more information on the config file's syntax.\n" +
        "For a reminder, here are the two supported beacons :\n" +
        ":BASE\n" + ":RECIPE";
        throw new Error (e) ;
    }

    // This error is thrown when the desired item does not appear in the catalog.
    // If this happens, it means that the item was not in the list of bases, and that its recipe was not given.
    public static void goalNotInCata () {
        throw new Error ("Target item could not be found in bases or recipes, there could be mistakes in your config file") ;
    }

    // This error is thrown when the config file can not be loaded properly.
    // Reasons for this error being thrown can be :
    //  Config file has the wrong name
    //  Config file has an unexpected syntax error
    //  Config file is missing
    public static void loadError() {
        throw new Error ("Error while loading the recipes from the config file !") ;
    }

    // This error is thrown when an item is not a base, but its recipe is not given in the config file.
    public static void missingRecipe(String itm) {
        throw new Error ("Recipe for item " + itm + " is missing !") ;
    }
    
    // This error is not supposed to be thrown : if it is, either I messed up my code or the user gave an otherwordly wierd config file
    // It can only be thrown by the chStackToStr function in the Recipe class
    public static void chStackToStrWrongSize() {
        throw new Error ("The 'size' argument does not match the size of the given stack") ;
    }

    // This error is thrown when an invalid beacon is written in the config file.
    public static void invalidBeacon() {
        throw new Error ("The given beacon is invalid !\nAs a reminder, here are the correct beacons :\n:BASE\n:RECIPE") ;
    }

    // This is not an error, it is a function used for testing my code
    // I left it in case anyone wants to tinker with my program, as a way to check execution easily.
    public static void flag (int n) {
        System.out.println("Code execution has reached flag " + n + " !") ;
    }
}
