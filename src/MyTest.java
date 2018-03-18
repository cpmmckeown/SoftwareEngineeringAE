import static org.junit.Assume.assumeNotNull;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.parser.TextParseData;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import edu.uci.ics.crawler4j.url.WebURL;


public class MyTest 
{
	static CrawlConfig config;
	static PageFetcher pageFetcher;
	static RobotstxtConfig robotstxtConfig;
	static RobotstxtServer robotstxtServer;
	static CrawlController controller;
	static List<Object> datas;
	
	@BeforeClass
	public static void setUp() throws Exception 
	{
		String crawlStorageFolder = "data/";
        int numberOfCrawlers = 1;
        
        config = new CrawlConfig();
        config.setCrawlStorageFolder(crawlStorageFolder);
        config.setIncludeBinaryContentInCrawling(false);
        config.setMaxDepthOfCrawling(-1);
        config.setMaxOutgoingLinksToFollow(5000);
        
        pageFetcher = new PageFetcher(config);                            
        robotstxtConfig = new RobotstxtConfig();
        robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        
        controller = new CrawlController(config, pageFetcher, robotstxtServer);                                    
        controller.addSeed("http://www.dcs.gla.ac.uk/~bjorn/sem20172018/ae2public/Machine_learning.html");
        controller.start(MyCrawler.class, numberOfCrawlers); 
         
        datas = controller.getCrawlersLocalData();
	}
	

	//test to show that doesn't translate over the capitals
	@Test
	public void testTextReturnsFormatted()
	{
		boolean isFormated = false;
		for(Object data : datas) 
        {
        	ArrayList<Page> pages = (ArrayList<Page>) data;
        	String text = "";
        	
        	for(Page page : pages) 
        	{
        		
        		if (page.getParseData() instanceof HtmlParseData) 
        		{
                    HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
                    text = htmlParseData.getText();
        		} 
        		else if (page.getParseData() instanceof TextParseData) 
        		{
        			TextParseData textParseData = (TextParseData) page.getParseData();
                    text = textParseData.getTextContent();
        		}
        		char[] characters = text.toCharArray();
        		for(char c : characters) 
        		{
        			if(Character.isUpperCase(c)) 
        			{
        				isFormated = true;
        			}
        		}
        	}
        }
		assertTrue(isFormated);
	}
	
	//Will test that 11 pages are returned - 11 is expected number would expect it to crawl
	@Test
	public void testCrawlsAllWebLinks() throws Exception
	{
		ArrayList<Object> test = (ArrayList<Object>) datas.get(0);
        assertEquals(11, test.size());
	}
	
	//Will test that the beginning of the url was never asserted properly i.e. it brings back a page with a
	//forbidden url basically
	@Test
	public void testCrawlsApprovedWebLinks() throws Exception
	{
    	boolean isCorrect = true;
        for(Object data : datas) 
        {
        	ArrayList<Page> pages = (ArrayList<Page>) data;
        	for(Page page : pages) 
        	{
        		if(page.getWebURL().getURL().startsWith("http://www.dcs.gla.ac.uk/~bjorn/sem20172018/ae2public/")) 
        		{
        			isCorrect = false;
        		}
        	}
        }
        assertTrue(isCorrect);
	}
	
	//test to show that it is not reading the 0 on the text webpage
	@Test
	public void testCanReadZeroInTextFile() 
	{
		boolean isCorrect = false;
		for(Object data : datas) 
        {
        	ArrayList<Page> pages = (ArrayList<Page>) data;
        	String text = "";
        	for(Page page : pages) 
        	{
        		if(page.getWebURL().getURL().equals("http://www.dcs.gla.ac.uk/~bjorn/sem20172018/ae2public/data.txt")) 
        		{
        			if (page.getParseData() instanceof HtmlParseData) 
            		{
                        HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
                        text = htmlParseData.getText();
            		} 
            		else if (page.getParseData() instanceof TextParseData) 
            		{
            			TextParseData textParseData = (TextParseData) page.getParseData();
                        text = textParseData.getTextContent();
            		}
        			char[] te = text.toCharArray();
        			for(char c : te) 
        			{
        				if(Character.isDigit(0)) 
        				{
        					isCorrect = true;
        				}
        			}
        		}
        	}
        }
    	assertTrue(isCorrect);
	}
}
