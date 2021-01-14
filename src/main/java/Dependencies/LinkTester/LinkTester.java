package project.LinkTester;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;



/* GeckoDriver needs to be in path for the program to work */


public class LinkTester{ 
	WebDriver driver;
	int linkNum;
	String website;
	List<String> totalLinkList;		//Keeps track of already seen links
	FileWriter writer=null; 
	
		public LinkTester(String website){
			this.website = website;
			this.linkNum = 0;
			this.driver = new FirefoxDriver();
			this.totalLinkList = new ArrayList<String>();
			
			//Sets up the CSV file	
			try {
				this.writer = new FileWriter("linkTest.csv",true);
			}catch(IOException e) {
				e.printStackTrace();
			}
			
			StringBuilder sb = new StringBuilder();
		    sb.append("Root");
		    sb.append(',');
		    sb.append("Link");
		    sb.append('\n');
		    try {
		    	this.writer.append(sb.toString());
		    }catch(IOException e){
		    	e.printStackTrace();
		    }
	}
	
	public void checkWebPage(String Website){
		getPage(Website);
		List<WebElement> hrefElements = getAllHrefElements();
		List<String> hrefLinks = getHrefLinks(getAllHrefElements());
		
		for(int i=0;i<hrefLinks.size();i++){
			if(!(totalLinkList.contains(hrefLinks.get(i)))){
				hrefLinks.remove(i);
				hrefElements.remove(i);
				i--;
			}
			else {
				totalLinkList.add(hrefLinks.get(i));
			}
		}
		
		for(int i=0;i<hrefLinks.size();i++){
			if(linkCheck(hrefLinks.get(i))){
				if(hrefLinks.get(i).contains(this.website)){
					checkWebPage(hrefLinks.get(i));		//Recursive call
				}
			}
			else{
				recordBrokenLink(Website, hrefLinks.get(i),hrefElements.get(i));
			}
		}
		closeDriver();
	}

	public void recordBrokenLink(String root,String link, WebElement element){
		StringBuilder sb = new StringBuilder();
	    sb.append(root);
	    sb.append(',');
	    sb.append(link);
	    sb.append(",");
	    sb.append(element.getAttribute("innerHTML"));
	    sb.append('\n');	    
	    try {
	    	this.writer.write(sb.toString());
	    }catch(IOException e){
	    	e.printStackTrace();
	    }
	}
	
	public List<String> getHrefLinks(List<WebElement> hrefElements){
		List<String> hrefLinks = new ArrayList<String>();
		for(int i=0;i<hrefElements.size();i++) {
			hrefLinks.add(hrefElements.get(i).getAttribute("href"));
		}
		return hrefLinks;
	}
	
	// Get all elements that have the href attribute.
	public List<WebElement> getAllHrefElements(){
		List<WebElement> hrefElements =driver.findElements(By.xpath("//a[@href]"));
		return hrefElements;
	}
	

	public void getPage(String link){
		link = link.replaceAll("\\s+","");
		driver.get(link);		
	}
	
	public void closeDriver(){
		driver.close();
	}
	
	public boolean linkCheck(String link) {
		driver.get(link);
		if(driver.getTitle().contains("Server Not Found")) { //Firefox's response if domain does not exist.
			return false;
		}
		if(driver.getTitle().contains("Page Not Found")){	//Facebook's bad link response (Used as an example)
			return false;
		}
		return true;
	}
}
