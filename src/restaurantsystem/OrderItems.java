package restaurantsystem;

import java.sql.Date;
import lombok.Data;

@Data
public class OrderItems {
	private int id;
	private String orderId;
	private String foodId;
	private String foodName;
	private double foodPrice;
	private int quantity;
	private boolean isEdited;
	private Date orderDate;
	private String editedBy;
	
	
	public OrderItems(String orderId, String foodId, String foodName, double foodPrice, int quantity,
			boolean isEdited, Date orderDate, String editedBy) {
		this.orderId = orderId;
		this.foodId = foodId;
		this.foodName = foodName;
		this.foodPrice = foodPrice;
		this.quantity = quantity;
		this.isEdited = isEdited;
		this.orderDate = orderDate;
		this.editedBy = editedBy;
	}
	
	 public boolean hasFoodId() {
	        
	        return false; 
	    }

	    public boolean hasFoodName() {
	       
	        return false; 
	    }
 public boolean hasFoodType() {
	        
	        return false; 
	    }

	    public boolean hasQuantity() {
	       
	        return false; 
	    }
 public boolean hasPrice() {
	        
	        return false; 
	    }
 
 public double getTotalAmount() {
	  
	    return quantity * foodPrice;
	}

	
}
