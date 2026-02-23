/*
As a part of the GDec algorithm, the ItmQt class represents an item and its quantity.
*/

public class ItmQt {

    public final String itm ; // Name of the item
    public int qt ; // Quantity of this item

    // Constructor for an item with an arbitrary quantity
    ItmQt (String item , int quantity) {
        this.itm = item ;
        this.qt = quantity ;
    }
    // Constructor for a singular item (with a quantity of 1)
    ItmQt (String item) {
        this.itm = item ;
        this.qt = 1 ;
    }

    // Custom toString function, so System.out.println doesn't just displays the object's memory address
    @Override
    public String toString () {
        return (qt + " * " + itm) ;
    }

    // Same as toString, but multiplies the quantity by n. Is only used for the function with the same name in the Recipe class
    public String toStringWithQt (int n) {
        return (n * qt + " * " + itm) ;
    }
    
}