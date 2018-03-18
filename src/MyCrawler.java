import java.util.ArrayList;

import java.util.regex.Pattern;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
//import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;




public class MyCrawler extends WebCrawler {
	
	ArrayList<Page> pages = new ArrayList<Page>();

    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g" 
                                                      + "|png|tiff?|mid|mp2|mp3|mp4"
                                                      + "|wav|avi|mov|mpeg|ram|m4v|pdf" 
                                                      + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

    /**
     * You should implement this function to specify whether
     * the given url should be crawled or not (based on your
     * crawling logic).
     */
    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
            String href = url.getURL().toLowerCase();
            return !FILTERS.matcher(href).matches() && href.startsWith("http://www.dcs.gla.ac.uk/~bjorn/sem20172018/ae2public/");
    }

    public Object getMyLocalData() {
		return pages;
	}
    
    /**
     * This function is called when a page is fetched and ready 
     * to be processed by your program.
     */
    @Override
    public void visit(Page page) {          
            //String url = page.getWebURL().getURL();
            // System.out.println("Crawling URL: " + url);
            pages.add(page);
    }
    
    protected ArrayList<Page> getPages() {
    	return pages;
    }
}
