import java.io.*;
import java.net.*;
import java.util.regex.*;

public class SotonName {
	private static Pattern linePattern = Pattern.compile("<title>"); // to match the correct html line
	private static Pattern namePattern = Pattern.compile(">(.*?) \\|"); // to extract the full name
	
	public static String getFullNameFromUsername(String username) throws Exception {
		String sotonProfileUrlString = "https://www.ecs.soton.ac.uk/people/" + username;
		
		// create a connection and a bufferedreader object to read from it
		URL sotonProfileUrl = new URL(sotonProfileUrlString);
		URLConnection sotonProfile = sotonProfileUrl.openConnection();
		BufferedReader urlReader = new BufferedReader(new InputStreamReader(sotonProfile.getInputStream()));
		
		String htmlLine, fullName = null;
		
		// read the source html line by line
		while ((htmlLine = urlReader.readLine()) != null) {
			Matcher lineMatcher = linePattern.matcher(htmlLine);
			
			if (lineMatcher.find()) {
				Matcher nameMatcher = namePattern.matcher(htmlLine);
				nameMatcher.find();
				fullName = nameMatcher.group(1);
				
				break;
			}
		}
		
		if (fullName != "People")
			return fullName;
		else
			return null;
	}
	
	public static void printUserInfo(String username) throws Exception {
		String fullName;
		
		if ((fullName = getFullNameFromUsername(username)) != null)
			System.out.println(username + " -> " + fullName);
		else
			System.out.println(username + " -> " + "User not found!");
	}
	
	public static void main(String[] args) throws Exception {
		// read from stdin if usernames are provided
		if (args.length > 0) {
			if (args.length > 1)
				System.out.println("User list: ");
			
			for (int i = 0; i < args.length; i++) {
				printUserInfo(args[i]);
			}
		}
		else {
			// create a bufferedreader object to read user input
			BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
			
			System.out.print("Enter a username: ");
			String username = inputReader.readLine();
			
			printUserInfo(username);
		}
	}
}
