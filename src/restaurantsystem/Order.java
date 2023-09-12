package restaurantsystem;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Order {
    private int id;
    private String orderId;
    private static int orderCount = 10001; 
    private List<MenuDetails> items;
    private Date date;
    private User user;
    private boolean isEdited;
    private String createBy;
    private double totalBill;
    
    
    public Order(String createdBy,double totalBill,boolean isEdited,Date date, String orderId) {
        this.orderId = generateOrderId();
        this.items = new ArrayList<>();
        this.date = date;
        this.isEdited = false;
        this.createBy = createdBy;
        this.totalBill = totalBill;
        
    }

    

    public String generateOrderId() {
        String orderId = "O" + orderCount;
        orderCount = orderCount+ 1; 
        return orderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public List<MenuDetails> getItems() {
        return items;
    }

    public void addItem(MenuDetails item) {
        items.add(item);
        
    }

    public void removeItem(MenuDetails item) {
        items.remove(item);
    }
    
    

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Order ID: ").append(id).append("\n");
        sb.append("Items:\n");
        for (MenuDetails item : items) {
            sb.append("- ").append(item.getFoodName()).append("\n");
        }
        return sb.toString();
    }

    public double calculateSubtotal() {
        double subtotal = 0.0;
        for (MenuDetails item : items) {
            subtotal += item.getPrice() * item.getQuantity();
        }
        return subtotal;
    }




	public double calculateTotal(double subtotal) {
	    double taxRate = 0.08;
	    double taxAmount = subtotal * taxRate;
	    double total = subtotal + taxAmount;
	    return total;
	}



	public MenuDetails getItemByDetails(String searchTerm) {
	    for (MenuDetails item : items) {
	        if (item.getItemDetails().equalsIgnoreCase(searchTerm)) {
	            return item; // Found a matching item
	        }
	    }
	    return null; // Item not found
	}
	public List<MenuDetails> getOrderItems() {	    
	    return items; 
	}




	public void removeItem(int selectedIndex) {
	    if (selectedIndex >= 0 && selectedIndex < items.size()) {
	        items.remove(selectedIndex);
	    }
	}


	 public boolean containsItem(MenuDetails item) {
	        // Loop through the items in the order and check if the item is already present
	        for (MenuDetails orderItem : items) {
	            if (orderItem.equals(item)) {
	                return true; // Item is already in the order
	            }
	        }
	        return false; 
	    }

	 public void updateQuantity(MenuDetails item, int quantity) {
		    // Loop through the items in the order and find the item to update
		    for (MenuDetails orderItem : items) {
		        if (orderItem.equals(item)) {
		            orderItem.getQuantity();
		            orderItem.setQuantity(quantity); 
		            double newPrice = quantity * orderItem.getPrice();

		            // Update the price in the item itself
		            orderItem.setPrice(newPrice);

		            // Recalculate the total
		            double subtotal = calculateSubtotal();
		            calculateTotal(subtotal);
		            return;
		        }
		    }
		}



	 public String getCreateBy() {
	        return createBy;
	    }

	    
	public void setCreateBy(String createBy) {
	        this.createBy = createBy;
	    }

	 
}
