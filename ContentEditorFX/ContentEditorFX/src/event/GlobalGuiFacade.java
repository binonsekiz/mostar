package event;

public class GlobalGuiFacade {

	public static void loginEvent(String username, String password){
		System.out.println("Login pressed");
		System.out.println("Username: " + username);
		System.out.println("Password: " + password);
	}
	
}
