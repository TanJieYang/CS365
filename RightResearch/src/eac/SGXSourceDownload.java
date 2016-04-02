/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eac;


import java.util.*;


/**
 *
 * @author loke.k
 */
public class SGXSourceDownload {
    
    
    public static boolean INIT_DOWNLOAD_SECTOR_SUMMARY(HTTPDownloadRequest request)
    {
        return request.BeginDownload("http://www.sgx.com/JsonRead/JsonData?qryId=RSectorSummary&timeout=60");
    }
    
    public static AttributeHolder FORMAT_SECTOR_SUMMARY_DATA(String rawData)
    {

        String data = rawData.substring(4);
        
        AttributeHolder secSummary = new AttributeHolder();
        secSummary.AddAttribute("SN");
        secSummary.AddAttribute("VL");
        secSummary.AddAttribute("V");
        secSummary.AddAttribute("R");
        secSummary.AddAttribute("F");
        secSummary.AddAttribute("U");
        
        secSummary.InitJSONArray(data, "items");
        
        
        return secSummary;
        
    }
    
    public static void SAVE_SECTOR_SUMMARY_DATA(AttributeHolder resultHolder, String dir, char fileType)
    {
        
        ArrayList<String> sectorHeader = new ArrayList<String>(Arrays.asList(
        "Sector", "Volume", "Values", "Rises", "Falls", "Unchanged")) ;
        
        switch(fileType)
        {
            case 0:
                resultHolder.OutputCSV(dir+".csv", sectorHeader);
                break;
            case 1:
                resultHolder.OutputTLD(dir+".tld", sectorHeader);
                break;
            case 2:
                resultHolder.OutputXLS(dir+".xls", sectorHeader);
                break;
        }
    }
    
    
    public static void DOWNLOAD_SECTOR_SUMMARY(String dir, char fileType)
    {
        
        String url = "http://www.sgx.com/JsonRead/JsonData?qryId=RSectorSummary&timeout=60";
        
        ArrayList<String> sectorHeader = new ArrayList<String>(Arrays.asList(
        "Sector", "Volume", "Values", "Rises", "Falls", "Unchanged")) ;
         
        URLDownloader urlDownloader = new URLDownloader(url);
        urlDownloader.DownloadData(null);
        String rawData = urlDownloader.GetInputData();
        
        //delimiting the initial spaces
        String data = rawData.substring(4);
        
        AttributeHolder secSummary = new AttributeHolder();
        secSummary.AddAttribute("SN");
        secSummary.AddAttribute("VL");
        secSummary.AddAttribute("V");
        secSummary.AddAttribute("R");
        secSummary.AddAttribute("F");
        secSummary.AddAttribute("U");
        
        secSummary.InitJSONArray(data, "items");
        
        switch(fileType)
        {
            case 0:
                secSummary.OutputCSV(dir+".csv", sectorHeader);
                break;
            case 1:
                secSummary.OutputTLD(dir+".tld", sectorHeader);
                break;
            case 2:
                secSummary.OutputXLS(dir+".xls", sectorHeader);
                break;
                
        }
        
    }
    
    public static boolean INIT_DOWNLOAD_INDICES(HTTPDownloadRequest request)
    {
            return request.BeginDownload("http://www.sgx.com/JsonRead/JsonData?qryId=NTP.INDICES");
        
    }
    
    public static AttributeHolder FORMAT_INDICES(String rawData)
    {
        //delimiting the initial spaces
        String data = rawData.substring(4);
        
        
        AttributeHolder indices = new AttributeHolder();
        indices.AddAttribute("N");
        indices.AddAttribute("TG");
        indices.AddAttribute("OP");
        indices.AddAttribute("PC");
        indices.AddAttribute("L");
        indices.AddAttribute("H");
        indices.AddAttribute("YL");
        indices.AddAttribute("YH");
        
        
        indices.InitJSONArray(data, "items");
        
        return indices;
    }
    
    public static void SAVE_INDICES(AttributeHolder result, String dir, char fileType)
    {
        ArrayList<String> indicesHeader = new ArrayList<String>(Arrays.asList(
                "Index Name", "Index Provider%TG@4@-", "Asset Class%TG@0@-", "Country%TG@1@-", "Open%OP", 
                "Previous Close%PC", "Day Range%L%-%H", "52-Week Range%YL%-%YH")) ;
        
        
        switch(fileType)
        {
            case 0:
                result.OutputCSV(dir+".csv", indicesHeader);
                break;
            case 1:
                result.OutputTLD(dir+".tld", indicesHeader);
                break;
            case 2:
                result.OutputXLS(dir+".xls", indicesHeader);
                break;
        }
    }
    
    public static void DOWNLOAD_INDICES(String dir, char fileType)
    {
        String url = "http://www.sgx.com/JsonRead/JsonData?qryId=NTP.INDICES";
        
        ArrayList<String> indicesHeader = new ArrayList<String>(Arrays.asList(
                "Index Name", "Index Provider%TG@4@-", "Asset Class%TG@0@-", "Country%TG@1@-", "Open%OP", 
                "Previous Close%PC", "Day Range%L%-%H", "52-Week Range%YL%-%YH")) ;
         
        URLDownloader urlDownloader = new URLDownloader(url);
        urlDownloader.DownloadData(null);
        String rawData = urlDownloader.GetInputData();
        
        //delimiting the initial spaces
        String data = rawData.substring(4);
        
        
        AttributeHolder indices = new AttributeHolder();
        indices.AddAttribute("N");
        indices.AddAttribute("TG");
        indices.AddAttribute("OP");
        indices.AddAttribute("PC");
        indices.AddAttribute("L");
        indices.AddAttribute("H");
        indices.AddAttribute("YL");
        indices.AddAttribute("YH");
        
        
        indices.InitJSONArray(data, "items");
        
        switch(fileType)
        {
            case 0:
                indices.OutputCSV(dir+".csv", indicesHeader);
                break;
            case 1:
                indices.OutputTLD(dir+".tld", indicesHeader);
                break;
            case 2:
                indices.OutputXLS(dir+".xls", indicesHeader);
                break;
        }
        
        
    }
    
    
    public static boolean INIT_DOWNLOAD_STI_CONS(HTTPDownloadRequest request)
    {
        return request.BeginDownload("http://www.sgx.com/JsonRead/JsonData?qryId=RSTIc&timeout=60&%20noCache=1454483705755.613214.9099238934");
    }
    
    public static AttributeHolder FORMAT_STI_CONS(String rawData)
    {
        //delimiting the initial spaces
        String data = rawData.substring(4);
        
        AttributeHolder stiConstituents = new AttributeHolder();
        stiConstituents.AddAttribute("N");
        stiConstituents.AddAttribute("SIP");
        stiConstituents.AddAttribute("NC");
        stiConstituents.AddAttribute("R");
        stiConstituents.AddAttribute("LT");
        stiConstituents.AddAttribute("C");
        stiConstituents.AddAttribute("P");
        stiConstituents.AddAttribute("VL");
        stiConstituents.AddAttribute("BV");
        stiConstituents.AddAttribute("B");
        stiConstituents.AddAttribute("S");
        stiConstituents.AddAttribute("SV");
        stiConstituents.AddAttribute("H");
        stiConstituents.AddAttribute("L");
        stiConstituents.AddAttribute("V");
        
        stiConstituents.InitJSONArray(data, "items");
        
        return stiConstituents;
    }
    
    public static void SAVE_STI_CONS(AttributeHolder result, String dir, char fileType)
    {
         ArrayList<String> stiHeader = new ArrayList<String>(Arrays.asList(
                "Counter Name", "SIP", "Code", "Rmk", "Last", "Chg",
                "Percent", "Vol", "B vol", "Buy", "Sell", "S vol", "High",
                "Low", "Value")) ;
         
         switch(fileType)
        {
            case 0:
                result.OutputCSV(dir+".csv", stiHeader);
                break;
            case 1:
                result.OutputTLD(dir+".tld", stiHeader);
                break;
            case 2:
                result.OutputXLS(dir+".xls", stiHeader);
                break;
        }
    }
    
    public static void DOWNLOAD_STI_CONS(String dir, char fileType)
    {
        String url = "http://www.sgx.com/JsonRead/JsonData?qryId=RSTIc&timeout=60&%20noCache=1454483705755.613214.9099238934";
        
        ArrayList<String> stiHeader = new ArrayList<String>(Arrays.asList(
                "Counter Name", "SIP", "Code", "Rmk", "Last", "Chg",
                "Percent", "Vol", "B vol", "Buy", "Sell", "S vol", "High",
                "Low", "Value")) ;
         
        URLDownloader urlDownloader = new URLDownloader(url);
        urlDownloader.DownloadData(null);
        String rawData = urlDownloader.GetInputData();

        //delimiting the initial spaces
        String data = rawData.substring(4);
        
        AttributeHolder stiConstituents = new AttributeHolder();
        stiConstituents.AddAttribute("N");
        stiConstituents.AddAttribute("SIP");
        stiConstituents.AddAttribute("NC");
        stiConstituents.AddAttribute("R");
        stiConstituents.AddAttribute("LT");
        stiConstituents.AddAttribute("C");
        stiConstituents.AddAttribute("P");
        stiConstituents.AddAttribute("VL");
        stiConstituents.AddAttribute("BV");
        stiConstituents.AddAttribute("B");
        stiConstituents.AddAttribute("S");
        stiConstituents.AddAttribute("SV");
        stiConstituents.AddAttribute("H");
        stiConstituents.AddAttribute("L");
        stiConstituents.AddAttribute("V");
        
        stiConstituents.InitJSONArray(data, "items");
        
        switch(fileType)
        {
            case 0:
                stiConstituents.OutputCSV(dir+".csv", stiHeader);
                break;
            case 1:
                stiConstituents.OutputTLD(dir+".tld", stiHeader);
                break;
            case 2:
                stiConstituents.OutputXLS(dir+".xls", stiHeader);
                break;
        }
    }
        
    
    public static boolean INIT_CORP_ACTION(HTTPDownloadRequest request)
    {
        String url = "http://www.sgx.com/proxy/SgxDominoHttpProxy?timeout=100&dominoHost=http%3A%2F%2Finfofeed.sgx.com%2FApps%3FA%3DCow_CorporateInformation_Content%26B%3DCorpDistributionByExDate%26C_T%3D-1";
        
        return request.BeginDownload(url);
        
        
    }
    
    public static AttributeHolder FORMAT_CORP_ACTION(String rawData)
    {
        //delimiting the initial spaces
        String data = rawData.substring(4);
        
        AttributeHolder corpAction = new AttributeHolder();
        corpAction.AddAttribute("CompanyName");
        corpAction.AddAttribute("Annc_Type");
        corpAction.AddAttribute("Ex_Date");
        corpAction.AddAttribute("Record_Date");
        corpAction.AddAttribute("DatePaid_Payable");
        corpAction.AddAttribute("Particulars");
        
        corpAction.InitJSONArray(data, "items");
        
        return corpAction;
    }
    
    public static void SAVE_CORP_ACTION(AttributeHolder corpAction, String dir, char fileType)
    {
         ArrayList<String> corporateHeader = new ArrayList<String>(Arrays.asList(
                "Company Name", "Type", "Ex-Date", "Record Date", "Date Paid/Payable", "Particulars")) ;
         
         switch(fileType)
        {
            case 0:
                corpAction.OutputCSV(dir+".csv", corporateHeader);
                break;
            case 1:
                corpAction.OutputTLD(dir+".tld", corporateHeader);
                break;
            case 2:
                corpAction.OutputXLS(dir+".xls", corporateHeader);
                break;
        }
    }
    
    public static void DOWNLOAD_CORP_ACTION(String dir, char fileType)
    {
        String url = "http://www.sgx.com/proxy/SgxDominoHttpProxy?timeout=100&dominoHost=http%3A%2F%2Finfofeed.sgx.com%2FApps%3FA%3DCow_CorporateInformation_Content%26B%3DCorpDistributionByExDate%26C_T%3D-1";
        
        ArrayList<String> corporateHeader = new ArrayList<String>(Arrays.asList(
                "Company Name", "Type", "Ex-Date", "Record Date", "Date Paid/Payable", "Particulars")) ;
         
        URLDownloader urlDownloader = new URLDownloader(url);
        urlDownloader.DownloadData(null);
        String rawData = urlDownloader.GetInputData();

        
        //delimiting the initial spaces
        String data = rawData.substring(4);
        
        AttributeHolder corpAction = new AttributeHolder();
        corpAction.AddAttribute("CompanyName");
        corpAction.AddAttribute("Annc_Type");
        corpAction.AddAttribute("Ex_Date");
        corpAction.AddAttribute("Record_Date");
        corpAction.AddAttribute("DatePaid_Payable");
        corpAction.AddAttribute("Particulars");
        
        corpAction.InitJSONArray(data, "items");
        
        switch(fileType)
        {
            case 0:
                corpAction.OutputCSV(dir+".csv", corporateHeader);
                break;
            case 1:
                corpAction.OutputTLD(dir+".tld", corporateHeader);
                break;
            case 2:
                corpAction.OutputXLS(dir+".xls", corporateHeader);
                break;
        }
    }
            
    public static void DOWNLOAD_STOCKSINFO(String dir, char fileType)
    {
       String url = "http://sgx.com/proxy/SgxDominoHttpProxy?timeout=3600&dominoHost=http%3A%2F%2Finfofeed.sgx.com%2FApps%3FA%3DCOW_App_DB%26B%3DStocksNameByInitial%26R_C%3D%26C_T%3D-1";
        
       ArrayList<String> stockHeader = new ArrayList<String>(Arrays.asList(
       "Full Name", "Market", "Sector (SSIC Standard)", "CPFIS", "Status")) ;
         
       URLDownloader urlDownloader = new URLDownloader(url);
       urlDownloader.DownloadData(null);
       String rawData = urlDownloader.GetInputData();
        
        //delimiting the initial spaces
       String data = rawData.substring(4);
       
       AttributeHolder stockList = new AttributeHolder();
        stockList.AddAttribute("Full Name");
        stockList.AddAttribute("Market");
        stockList.AddAttribute("Sector (SSIC Standard)");
        stockList.AddAttribute("CPFIS");
        stockList.AddAttribute("Status");
        
       stockList.InitJSONArray(data, "items");
        
        switch(fileType)
        {
            case 0:
                stockList.OutputCSV(dir+".csv", stockHeader);
                break;
            case 1:
                stockList.OutputTLD(dir+".tld", stockHeader);
                break;
            case 2:
                stockList.OutputXLS(dir+".xls", stockHeader);
                break;
        }
    }
                
    public static void DOWNLOAD_ISININFO(String dir, char fileType)
    {
        String url = "http://sgx.com/proxy/SgxDominoHttpProxy?timeout=2&dominoHost=http%3A%2F%2Finfofeed.sgx.com%2FApps%3FA%3DCOW_App_DB%26B%3Disincodedownload";
        //download all the links
        URLDownloader urlDownloader = new URLDownloader(url);
        urlDownloader.DownloadData(null);
        
        String targetLink = "http://infopub.sgx.com/Apps?A=COW_App_DB";
        
        String rawData = urlDownloader.GetInputData();
        String data = rawData.substring(4);
        
        AttributeHolder linkHolder = new AttributeHolder();
        linkHolder.AddAttribute("ISIN Code Link");
        
        linkHolder.InitJSONArray(data, "items");
        
        ArrayList<String> links = linkHolder.GetElements("ISIN Code Link");
        if(links != null)
        {
            
            for(String targetDB : links)
            {
                String downloadDB = targetLink + targetDB.replaceAll("\"", "");
                System.out.println(downloadDB);
                URLDownloader dbInfoDownload = new URLDownloader(downloadDB);
                dbInfoDownload.DownloadData("\n");
                
                //download DB info into one big blob
                String rawDBData = dbInfoDownload.GetInputData();
                
                
                
                String[] items = rawDBData.split("\n");
                
                AttributeHolder holder = new AttributeHolder();
                holder.AddAttribute("NAME");
                holder.AddAttribute("STATUS");
                holder.AddAttribute("ISIN CODE");
                holder.AddAttribute("CODE");
                holder.AddAttribute("SHORT NAME");
                
                
                ArrayList<String> sectorHeader = new ArrayList<String>(Arrays.asList(
                "Name", "Status", "ISIN Code", "Code", "Short Name"));
                
                for(int i = 1; i < items.length; ++i)
                {
                    String itemStr = items[i];
                    String[] itemCollect = itemStr.split("(   )+");
                    if(itemCollect.length > 4)
                    {
                        holder.AddElementToAttribute("NAME", itemCollect[0].trim());
                        holder.AddElementToAttribute("STATUS", itemCollect[1].trim());
                        holder.AddElementToAttribute("ISIN CODE", itemCollect[2].trim());
                        holder.AddElementToAttribute("CODE", itemCollect[3].trim());
                        holder.AddElementToAttribute("SHORT NAME", itemCollect[4].trim());
                    }
                    else
                    {

                        holder.AddElementToAttribute("NAME", itemCollect[0].trim());
                        holder.AddElementToAttribute("STATUS", " ");
                        holder.AddElementToAttribute("ISIN CODE", itemCollect[1].trim());
                        holder.AddElementToAttribute("CODE", itemCollect[2].trim());
                        holder.AddElementToAttribute("SHORT NAME", itemCollect[3].trim());
                    }
                }
                
                
                switch(fileType)
                {
                    case 0:
                        holder.OutputCSV(dir+".csv", sectorHeader);
                        break;
                    case 1:
                        holder.OutputTLD(dir+".tld", sectorHeader);
                        break;
                    case 2:
                        holder.OutputXLS(dir+".xls", sectorHeader);
                        break;
                }
                
                
            }
        }
    }
    

    
}
