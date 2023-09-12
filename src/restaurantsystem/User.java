package restaurantsystem;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;


@Data
public class User {
	private int userId;
	private String name;
	private String username;
	private String password;
	private String userRole;
	private String branch;
	private String address;
		
	public User (int userId, String name, String username, String password, String userRole, String branch, String address) {
		this.userId = userId;
		this.name = name;
		this.username = username;
		this.password =  password;
		this.userRole = userRole;
		this.branch = branch;
		this.address = address;
		
	}

	 public User(int userId, String username) {
        this.userId = userId;
        this.username = username;
    }
	 public User(String username) {
	        this.username = username;
	    }
	 
	 private static List<User> userList = new ArrayList<>();
	
	  public static User getUserByUsername(String username) {
	        for (User user : userList) {
	            if (user.getUsername().equals(username)) {
	                return user;
	            }
	        }
	        return null;
	    }
}
