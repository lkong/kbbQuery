package kbbTheft.theft;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.*;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Scanner;
import java.applet.*;
import java.awt.*;

public class HttpQuery_apache extends Applet {
    public void init(){}
    public void stop(){}
    public void paint(Graphics g)
    {
		HashMap<String,String> dict=new HashMap<String,String>();
		dict.put("year", getParameter("year"));
		dict.put("model", getParameter("model"));
		dict.put("mileage", getParameter("mileage"));
		dict.put("trim", getParameter("trim"));
		dict.put("make", getParameter("make"));
		dict.put("host", "http://www.kbb.com");
		try {
			g.drawString(findPriceFromKBB("www.kbb.com",dict), 100, 100);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    	
    }
	private static String hostUrl = "www.kbb.com";
	public static String LinkMaker(HashMap dict)
	{
		//String host=new String("http://www.kbb.com");
	    String link=new String();
	    //link+=host;
	    link+="/";
	    link+=dict.get("make");// ["make"]
	    link+="/";
	    link+=dict.get("model");//["model"]
	    link+="/";
	    String templink=new String();
	    templink=dict.get("year")+"-"+dict.get("make")+"-"+dict.get("model");
 	    link+=templink;
 	    link+="/";
 	    link+=dict.get("trim");
	    link+="/";	
		return link;
	} 
	public static String findPriceFromKBB(String host,HashMap<String,String> dict) throws URISyntaxException
	{

		String Link=HttpQuery_apache.LinkMaker(dict);
		URIBuilder builder = new URIBuilder();    
		builder.setScheme("http").setHost(hostUrl).setPath(Link)
		    .setParameter("intent", "trade-in-sell")
		    .setParameter("mileage", (String)dict.get("mileage"))
		    .setParameter("pricetype", "trade-in")
		    .setParameter("oq", "");
		URI uri = builder.build();
		HttpGet httpget = new HttpGet(uri);
		System.out.println("requesting  "+httpget.getURI());
		HttpClient httpclient = new DefaultHttpClient();
		//httpclient.getParams().setParameter(
		 //       ClientPNames.COOKIE_POLICY, CookiePolicy.RFC_2965);
	  try {
		  HttpResponse response = httpclient.execute(httpget);
		  HttpEntity entity = response.getEntity();
		  if (entity != null) 
		  	{
			  InputStream instream = entity.getContent();
			  Scanner findPrice=new Scanner(instream);
			  while (findPrice.hasNextLine())
			  {
				  String currentLine=findPrice.nextLine();
				  if (currentLine.contains("Excellent:"))
				  {
					  if (findPrice.hasNextLine())
					  {
						  currentLine=findPrice.nextLine();
						  int moneySign=currentLine.indexOf('$');
						  String price=currentLine.substring(moneySign+1);
						  int moneySignEnd=price.indexOf('<');
					      price=price.substring(0,moneySignEnd);
						  System.out.println(price);
						  return price;
					  }
				  }
			  }
			  
		  	}
	      }
	  
	      catch (Exception  e)
	      {
	    	  System.out.println(e.toString());
	    	  return null;
	      }
		return null;
	}
  public static void main(String[] args) throws ClientProtocolException, IOException, URISyntaxException {
		HashMap<String,String> dict=new HashMap<String,String>();
		dict.put("year", "2007");
		dict.put("model", "aspen");
		dict.put("mileage", "21070");
		dict.put("trim", "limited-sport-utility-4d");
		dict.put("make", "chrysler");
		dict.put("host", "http://www.kbb.com");
		findPriceFromKBB("www.kbb.com",dict);  
  }
}