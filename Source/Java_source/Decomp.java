import java.io.BufferedReader ;
import java.io.FileReader ;
import java.util.Stack ;

/*
This is the main class for the GDec algorithm.
Its basic way of functioning is described in the comments of the main function
*/
public class Decomp {

    // This variable stores all the recipes read from the config file.
    public static Recipe[] catalog ;
    // This variable stores the recipee for the desired item
    public static Recipe mainRecipe ;

    // This function loads all the recipes from the config file into the catalog
    public static void cataLoad(String fileName) {
        try {

            // This stack stores all the recipes that have been read from the file
            // The recipeCount variable keeps track of the stack's size
            Stack<Recipe> tempRecipes = new Stack<>() ;
            int recipeCount = 0 ;

            // The variable br stores the config file, while strReader reads each line
            BufferedReader br = new BufferedReader(new FileReader(fileName)) ;
            String strReader = br.readLine() ;
            Line.Beaconed current = Line.Beaconed.NIL ;

            // While the reader has not reached the end of the file, it creates a Line object from each line
            // Each non-beacon line's content (of type Recipe) is pushed onto the stack.
            while (strReader != null) {
                Line reader = new Line (strReader , current) ;
                if (reader.isBeacon) {
                    current = Line.readBeacon(strReader) ;
                } else {
                    if (!reader.isEmpty) {
                        tempRecipes.push(reader.contents) ;
                        recipeCount++ ;
                    }
                }
                strReader = br.readLine() ;
            }

            // The stack is then emptied into a table of corresponding size, stored into the catalog variable.
            catalog = new Recipe[recipeCount] ;
            for (int i = recipeCount - 1 ; i >= 0 ; i--) {
                catalog[i] = tempRecipes.pop() ;
            }
            // Closes the file after it is read.
            br.close();

        } catch (Exception e) {
            Errors.loadError();
        }
    }

    // This function checks the recipe for the main item and returns true if all ingredients are bases
    public static boolean based () {
        ItmQt[] ing = mainRecipe.ingredients ;
        if (ing == null) {
            return true ;
        }
        for (int i = 0 ; i < ing.length ; i++) {
            if (Recipe.getRecipe(ing[i].itm, catalog).ingredients != null) {
                return false ;
            }
        }
        return true ;
    }

    // This function decomposes all non-base ingredients in the main item's recipe into their own ingredients.
    public static void refine () {
        
        // This stack stores all the ingredients decomposed from the main item's ingredients
        // Bases are their own decomposition
        // The maxNew variable stores the amount of items that have been added
        // An item can appear two times ! (This problem is solved when the stack is emptied later in the function)
        Stack<ItmQt> tempIng = new Stack<>() ;
        int maxNew = 0 ;

        // Each item in the main recipe is checked
        //  If it is a base, it is added to tempIng
        //  If it is not a base, each item in its recipe sees its quantity multiplied by the item's quantity, and is pushed onto tempIng
        for (ItmQt ing : mainRecipe.ingredients) {
            ItmQt[] dec = Recipe.getRecipe(ing.itm , catalog).ingredients ;
            if (dec == null) {
                tempIng.push(ing) ;
                maxNew++ ;
            } else {
                for (ItmQt subIng : dec) {
                    tempIng.push(new ItmQt(subIng.itm , ing.qt * subIng.qt)) ;
                    maxNew++ ;
                }
            }
        }

        // Here, the stack emptied into a table.
        // Because of the doubles, we still have no idea how many different ingredients will be in the recipe.
        // We start by creating a table that could fit the maximum number of ingredients (which is the size of the stack)
    
        ItmQt[] tempIngTab = new ItmQt[maxNew];
        int write = 0 ;
        boolean placed ;

        // While emptying the stack, we add items to the table and merge the doubles, additioning their quantities.
        while (!tempIng.empty()) {
            ItmQt ing = tempIng.pop() ;
            placed = false ;
            for (int i = 0 ; i < write ; i++) {
                if ((!placed) && ing.itm.equals(tempIngTab[i].itm)) {
                    tempIngTab[i].qt += ing.qt ;
                    placed = true ;
                }
            }
            if (!placed) {
                    tempIngTab[write] = ing ;
                    write++ ;
            }
        }
        // However, we still have no idea how many different ingredients will be in the recipe.

        // Now knowing how many different items are in the table, we create a new one that will have the right length.
        ItmQt[] nextItems = new ItmQt[write] ;
        for (int i = 0 ; i < write ; i++) {
            nextItems[i] = tempIngTab[i] ;
        }
        // The main recipe is updated with the new ingredients ('closer' to bases)
        mainRecipe.ingredients = nextItems ;
        
    }

    public static void main (String[] args) {

        if (args.length == 0) {Errors.noMain() ;}

        // The first argument is the item to decompose
        String goal = args[0] ;

        // The second argument is the quantity of this item. By default, it is 1
        int mainQt = (args.length < 2) ? 1 : (Integer.valueOf(args[1])) ;

        // The second argument can be the config file that should be used. By default, the name is "config.gdec"
        // Note that for this argument to be used, the previous one needs to be filled too, even if it is 1.
        String filePath = (args.length < 3) ? "config.gdec" : args[2] ;

        // Loads all recipes from the config file into the catalog
        cataLoad(filePath) ;

        // Finds the recipe for the main item
        mainRecipe = Recipe.getRecipe(goal, catalog) ;
        
        // The main item's recipe is refined until all its ingredients are bases.
        while (!based()) {
            refine() ;
        }

        // Prints the recipe for the required quantity of the right item
        System.out.println(mainRecipe.toStringWithQt(mainQt)) ;
    }
}