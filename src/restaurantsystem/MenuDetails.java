package restaurantsystem;

import lombok.Data;

@Data
public class MenuDetails {
	private int id;
    private String food_id;
    private String foodName;
    private String itemDetails;
    private int quantity;
    private double price;
    private FoodType type;

    public MenuDetails(int id, String food_id, String foodName, String itemDetails, int quantity, double price, FoodType type) {
        this.id = id;
        this.food_id = food_id;
        this.foodName = foodName;
        this.itemDetails = itemDetails;
        this.quantity = quantity;
        this.price = price;
        this.type = type;
    }
	

    public MenuDetails(String food_id2, String foodName2, String details, int quantity2, double price2, FoodType foodType) {
        this.food_id = food_id2;
        this.foodName = foodName2;
        this.itemDetails = details;
        this.quantity = quantity2;
        this.price = price2;
        this.type = foodType;
    }



	public String generateFoodId(int nextFoodId) {
        return String.format("f%04d", nextFoodId);
    }
	
	 public enum FoodType {
	        TEA,
	        COFFEE,
	        SHAN_FOOD,
	        INDIAN,
	        BURMESE,
	        CHINESE,
	        FAST, 
	        WATER_AND_DRINKS,
	        UNKNOWN
	    }
	 
	 
	 public static void main(String[] args) {
		 System.out.println("Hello Menu");
	 }

	
}
