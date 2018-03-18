import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import edu.uci.ics.crawler4j.crawler.*;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.parser.TextParseData;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import edu.uci.ics.crawler4j.url.WebURL;

public class MyCrawlerController {
		
    public static void main(String[] args) throws Exception {
            String crawlStorageFolder = "data/";
            int numberOfCrawlers = 1; // @SE(M) Glasgow: you do not need to change this to detect the bugs

            CrawlConfig config = new CrawlConfig();
            config.setCrawlStorageFolder(crawlStorageFolder);
                        
            /*
             * Do you want crawler4j to crawl also binary data ?
             * example: the contents of pdf, or the metadata of images etc
             */
            config.setIncludeBinaryContentInCrawling(false); //SE(M) Glasgow, you do not need to change this to observe the bugs
            
            /*
             * You can set the maximum crawl depth here. The default value is -1 for
             * unlimited depth
             */
            config.setMaxDepthOfCrawling(-1); // SE(M) Glasgow, you do not need to change this to observe the bugs
            
            /*
             * Max number of outgoing links which are processed from a page (default 5000)
             */
            config.setMaxOutgoingLinksToFollow(5000);
            
            /*
             * Instantiate the controller for this crawl.
             */
            PageFetcher pageFetcher = new PageFetcher(config);                            
            RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
            RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
            CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
                                                            
            /*
             * For each crawl, you need to add some seed urls. These are the first
             * URLs that are fetched and then the crawler starts following links
             * which are found in these pages
             */
            controller.addSeed("http://www.dcs.gla.ac.uk/~bjorn/sem20172018/ae2public/Machine_learning.html");            

            /*
             * Start the crawl. This is a blocking operation, meaning that your code
             * will reach the line after this only when crawling is finished.
             */
            controller.start(MyCrawler.class, numberOfCrawlers); 
             
            List<Object> datas = controller.getCrawlersLocalData();
            
            /*
             * Let's inspect the pages and content received...
             */                               
            for (Object data: datas) {            	
            	try {
	            	@SuppressWarnings("unchecked")
					ArrayList<Page> pages = (ArrayList<Page>) data;
	            	
	            	for (Page page: pages) {    
	            		if (page.getParseData() instanceof HtmlParseData) {
	                        HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
	                        String text = htmlParseData.getText();
	                        String html = htmlParseData.getHtml();
	                        Set<WebURL> links = htmlParseData.getOutgoingUrls();

	                        System.out.println("--------------------------------------------");
	                        System.out.println("URL: " + page.getWebURL().toString());
	                        System.out.println("Text length: " + text.length());
	                        System.out.println("Html length: " + html.length());	                        
	                        System.out.println("Number of outgoing links: " + links.size());
	                        System.out.println("Content Type: " + page.getContentType());	                       
	                        //System.out.println("Page Content as test: " + text);	  
	                        System.out.println("Page Content as html:\n" + html);
	                        System.out.println("--------------------------------------------");
	            		} 
	            		else if (page.getParseData() instanceof TextParseData) {
	            			TextParseData textParseData = (TextParseData) page.getParseData();
	                        String text = textParseData.getTextContent();	                        
	                        Set<WebURL> links = textParseData.getOutgoingUrls();

	                        System.out.println("--------------------------------------------");
	                        System.out.println("URL: " + page.getWebURL().toString());
	                        System.out.println("Text length: " + text.length());
	                        System.out.println("Number of outgoing links: " + links.size());
	                        System.out.println("Content Type: " + page.getContentType());	                       
	                        System.out.println("Content:");	  
	                        System.out.println(text);
	                        //System.out.println(text.toString());
	                        System.out.println("--------------------------------------------");
	            		}
	            	}	            	
            	} catch (ClassCastException e) {
	            	System.out.println("Unable to extract page data.");
	            }	            	            	
            }                            
    }
}
