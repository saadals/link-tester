package Dependencies.LinkTester;


public class App 
{

    // For testing
    public static void main( String[] args )
    {
        System.out.println("Enter the website you want tested (format: example.com):");
		Scanner reader = new Scanner(System.in);
		String website = reader.nextLine();
		reader.close();
		LinkTester ex = new LinkTester("www."+website);
		ex.checkWebPage("www."+website);
		ex.closeDriver();
    }
}
