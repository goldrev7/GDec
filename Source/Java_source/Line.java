/*
As a part of the GDec algorithm, this classe's objects are the lines of the config file
*/

public class Line {
    
    // This enum type characterizes the two states of non-beacon lines
    // The NIL option is used to read the first line of the file (which should always be a beacon)
    public enum Beaconed {BASE , RECIPE , NIL}

    // Used to describe a line :
    //  Empty lines are skipped
    //  Beacon lines are read separately by the readBeacon function
    //  For non-beacon lines, the recipe being read by the read function is stored in the 'contents' field
    public boolean isEmpty ;
    public boolean isBeacon ;
    public Recipe contents ;

    // The constructor for the Line class
    // It creates a Line object from a line of the config file (with type String), while taking into account its status (the beacon placed above it)
    public Line (String line , Beaconed b) {
        if (line.length() == 0 || line.charAt(0) == '#'){
            this.isEmpty = true ;
            this.isBeacon = false ;
            this.contents = null ;
        } else {
            this.isEmpty = false ;
            if (line.charAt(0) == ':') {
                this.isBeacon = true ;
                this.contents = null ;
            } else {
                this.isBeacon = false ;
                this.contents = read(line , b) ;
            }
        }
    }

    // This function reads a beacon line and returns a beacon object, which will be used as the status for all the following non-beacon lines
    public static Beaconed readBeacon (String line) {
        if (line.equals(":BASE")) {return Beaconed.BASE ;}
        if (line.equals(":RECIPE")) {return Beaconed.RECIPE ;}
        Errors.invalidBeacon() ;
        return Beaconed.NIL ;   // Will never be return because an error is thrown before
    }
    // This function reads a non-beacon line according to its status.
    // If no beacon has been encountered yet (no status for the line), an error is thrown.
    private static Recipe read (String l , Beaconed b) {
        if (b == Beaconed.NIL) {
            Errors.firstLineNotABeacon() ;
        }
        if (b == Beaconed.BASE) {
            return new Recipe (l , null) ;
        }
        return Recipe.readRecipe(l) ;
    }
}
