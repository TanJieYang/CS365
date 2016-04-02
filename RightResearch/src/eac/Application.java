package eac;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author loke.k
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;


public class Application {
    

    public static final String url = "http://www.sgx.com/proxy/SgxDominoHttpProxy?timeout=18000&dominoHost=http%3A%2F%2Finfofeed.sgx.com%2FApps%3FA%3DCow_CorporateInformation_Content%26B%3DCorpInfoCompanyNameList%26C_T%3D-1";
    //http://real-chart.finance.yahoo.com/table.csv?s=IBM&a=00&b=2&c=1962&d=02&e=22&f=2016&g=v&ignore=.csv
    public static void main4545(String[] args)
    {
               URLDownloader urlDownloader = new URLDownloader(url);
        //format: year, month, day. Month starts from zero, i.e. january is 0
        //GregorianCalendar start = new GregorianCalendar(2010, 1, 18);
        //GregorianCalendar end = new GregorianCalendar(2016, 1, 18);
        //format: company, start, end, type. type has daily, weekly, monthly, dividend
        //String companyname = "IBM";
        //String filename = companyname + "HistoricalQuotes.csv";
        //YahooStockDownloader financeDownloader = new YahooStockDownloader(companyname, start, end, "d", filename);
        System.out.println("Downloading Corporate Information Data");
        urlDownloader.DownloadData(null);
        System.out.println("Formatting output");
        //getting the raw data
        String rawData = urlDownloader.GetInputData();
        
        //delimiting the initial spaces
        String data = rawData.substring(4);
        
        ArrayList<String> corporateInfoHeader = new ArrayList<String>(Arrays.asList(
                "Company Name", "Document Link")) ;
        
        AttributeHolder corporateInfo = new AttributeHolder();
        corporateInfo.AddAttribute("Company Name");
        corporateInfo.AddAttribute("Doc Link");
        
        //get input data
        corporateInfo.InitJSONArray(data, "items");
        
        //ouutput data
        //corporateInfo.OutputCSV("corporateInfo.csv", corporateInfoHeader);
        //corporateInfo.OutputTLD("corporateInfo.tld", corporateInfoHeader);
        //corporateInfo.OutputXLS("corporateInfo.xls", corporateInfoHeader);
        
        
        //corporateInfo.OutputHTML();
        
        String link = "http://infopub.sgx.com/Apps?A=Cow_CorporateInformation_Content";
        String join;
        String name;
        PrintWriter outputFile;
        String directoryName = "Corporate Info";
        
        corporateInfo.CreateFolder(directoryName);
        
        try {
            for(int i = 1; i < corporateInfo.m_MaxElem; ++i){
            
                name = corporateInfo.GetData(0,i);
                join = corporateInfo.GetData(1,i);
                
                String urlDownloadLink = link + join;

                String targetDir = directoryName + File.separator + name + ".html";
                //System.out.println("writing-> " + targetDir);
                //outputFile = new PrintWriter("corporateInfo"+i+".html");
                
                outputFile = new PrintWriter(targetDir);
                
                HTTPDownloadRequest request = new HTTPDownloadRequest();
                request.BeginDownload(urlDownloadLink);
                
                while(!request.IsDone())
                    request.RetrieveData(null);
                
                outputFile.print(request.GetData());
                
                
                outputFile.close();
                
                
                //System.out.println("success-> " + targetDir);
            }
            
        }
        catch (Exception e) {
            //e.printStackTrace();
              //System.out.println("Failed Parsing HTML");
        }

    }
    
}
