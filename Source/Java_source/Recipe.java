import java.util.*;

/*
As a part of the GDec algorithm, this class implements an item's recipe
*/

public class Recipe {

    public final String name ;  // The item's name
    // The list of ingredients for that item, as well as their quantities (ItmQt objects)
    // A base will be represented by a Recipe with null as its ingredients array
    public ItmQt[] ingredients ;

    // Constructor for a recipe, given its name and the list of its ingredients
    public Recipe (String name , ItmQt[] ingredients) {
        this.name = name ;
        this.ingredients = ingredients ;
    }
    
    // Custom toString function, preventing System.out.println from printing the Recipe's address
    @Override
    public String toString () {
        if (this.ingredients == null) {return this.name + " is a base !" ;}

        String out = "To obtain " + this.name + " you need :\n" ;
        for (int i = 0 ; i < this.ingredients.length ; i++) {
            out += (this.ingredients[i].qt!=0) ? this.ingredients[i].toString() + "\n" : "" ;
        }
        return out ;
    }

    // Prints the recipe for n times the Recipe object that called this function
    public String toStringWithQt (int n) {

        if (this.ingredients == null) {return this.name + " is a base !" ;}
        String q = (n != 1) ? n + " " : "" ;
        String out = "To obtain " + q + this.name + " you need :\n" ;

        if (n == 0) {return out + "Nothing !" ;}

        for (int i = 0 ; i < this.ingredients.length ; i++) {
            if (this.ingredients[i].qt != 0) {
                out += this.ingredients[i].toStringWithQt(n) + "\n" ;
            }
        }
        
        return out ;
    }

    // This function takes a Stack of characters as its argument, as well as its length
    // It outputs a String, starting with the deepest character from the stack
    // The stack is emptied after the function's execution
    private static String chStackToStr (Stack<Character> s , int size) {
        try {
            char[] tempName = new char[size] ;  // Creates an array of the size given in the arguments
            // Each character from the stack is popped and added to the array, startinng from the end
            for (int j = size - 1 ; j >= 0 ; j--) {
                tempName[j] = s.pop() ;
            }
            // The char array is converted to a String
            return new String(tempName) ;
        } catch (Exception e) {
            Errors.chStackToStrWrongSize() ;
            return "" ;
        }
    }
    // This function takes a single, well-formatted line from the :RECIPE section of the config file
    // It outputs a Recipe object corresponding to the recipe described
    // The line "FOO=5BAR+3BAZ" will return a Recipe with name FOO and ingredients {(itm = "BAR" , qt = 5) ; ( itm = "BAZ" , qt = 3) }
    // (Please note that the notation for the ItmQt objects was a simple representation)
    public static Recipe readRecipe (String line) {
        int len = line.length() ; // Reads the length of the line to prevent the reading head from accessing an invalid address
        Stack<Character> reader = new Stack<>() ; // Will store the character as they appear
        int readerSize = 0 ; // Stores the size of the reader Stack, to use in the chStack function
        Stack<ItmQt> ingStack = new Stack<>() ; // Will store all the ingredients listed in the recipe
        int ingSize = 0 ; // Stores the size of the ingredients Stack, to later turn it into an array for the creation of a Recipe object
        int i = 0 ; // Represents the position of the reading head
        char ch = line.charAt(0) ; // Character being read
        // Stacks all the characters from the item's name
        while (ch != '=') {
            reader.push(ch) ;
            readerSize++ ;
            i++ ;
            ch = line.charAt(i) ;
        }

        // Skips the '='
        i++ ;
        // The item's name is obtained from the reader Stack, which is emptied
        String name = chStackToStr(reader, readerSize) ;
        readerSize = 0 ;
        int qt ; // For each item in the ingredients, will store its quantity (which is written first)
        String itm ; // For each item in the ingredients, will store its name (which is written second)

        // Reads all items from the recipe until the end of the line is reached.
        // This loop will be repeated once for each item
        while(i < len) {
            ch = line.charAt(i) ;
            int chAscii = (int) ch ; // The current character's ASCII value is used to assess its type (number or letter)
            
            // This loop reads the quantity of the current item being read
            // It stops when the next character is a *, indicating the beginning of the item's name
            while (chAscii >= 48 && chAscii <= 57) {
                reader.push(ch) ;
                readerSize++ ;
                i++ ;
                ch = (i < len) ? line.charAt(i) : ch ;
                chAscii = (int) ch ;
            }

            // This part sets the value of qt to the quantity that has been read
            if (readerSize == 0) {
                qt = 1 ;
            } else {
                qt = Integer.valueOf(chStackToStr(reader , readerSize)) ;
                // Skips the '*' if there was one (if there was a quantity)
                i++ ;
                ch = (i < len) ? line.charAt(i) : ch ;
                chAscii = (int) ch ;
            }
            readerSize = 0 ;
            
            // This part reads the item's name, stopping either after reaching the end of the line, or a '+', indicating a new item
            while (((chAscii >= 65 && chAscii <= 90) || (chAscii >= 97 && chAscii <= 122) || chAscii == 95 || (chAscii >= 48 && chAscii <= 57)) && i < len) {
                reader.push(ch) ;
                readerSize++ ;
                i++ ;
                ch = (i < len) ? line.charAt(i) : ch ;
                chAscii = (int) ch ;
            }

            // Sets the value of itm to the item's name that has just been read
            itm = chStackToStr(reader , readerSize) ;
            readerSize = 0 ;
            
            // The described ItmQt object is then pushed ontho the ingredients stack.
            ingStack.push(new ItmQt(itm, qt)) ;
            ingSize++ ;
            
            // Skips the '+'
            i++ ;
        }

        // The ingredients stack is then turned into an array, and a Recipe object with the given name and list of ingredients is returned
        ItmQt[] ingredients = new ItmQt[ingSize] ;
        for (int j = ingSize - 1 ; j >= 0 ; j--) {
            ingredients[j] = ingStack.pop() ;
        }
        return new Recipe(name, ingredients) ;
    }

    // This function recieves an item's name, as well as a catalog (an array of Recipe objects)
    // If the item's recipe is available in the catalog, it is returned, otherwise an error is raised
    public static Recipe getRecipe (String itm , Recipe[] catalog) {
        for (int i = 0 ; i < catalog.length ; i++) {
            if (itm.equals(catalog[i].name)) {
                return catalog[i] ;
            }
        }
        Errors.missingRecipe(itm) ;
        return null ;
    }
}