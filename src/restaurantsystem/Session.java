package restaurantsystem;

public class Session {
    private static User currentUser; // Store the current user

    public static void setUser(User user) {
        currentUser = user;
    }

    public static User getUser() {
        return currentUser;
    }
}