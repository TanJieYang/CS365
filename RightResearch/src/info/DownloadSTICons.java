/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package info;

import eac.AttributeHolder;
import eac.HTTPDownloadRequest;
import eac.UIContext;
import eac.WebService;
import java.util.*;

/**
 *
 * @author loke.k
 */
public class DownloadSTICons implements WebService
{

    HTTPDownloadRequest httpRequest;
    AttributeHolder     resultHolder;
    
    final String URL = "http://www.sgx.com/JsonRead/JsonData?qryId=RSTIc&timeout=60&%20noCache=1454483705755.613214.9099238934";
    
        @Override
    public boolean On_Initialize(UIContext context) {
        
       httpRequest = new HTTPDownloadRequest();
       
       return httpRequest.BeginDownload(URL);
            
    }
    
    @Override
    public void On_DownloadData(UIContext context) 
    {
        while(!httpRequest.IsDone())
        {
            httpRequest.RetrieveData(null);
            float completeRatio = httpRequest.GetRetrieveRatio();
            int v = (int)(completeRatio * 85);
                    
            context.DisplayText("download in progress ...       - " + v + "%");
            context.SetProgressVal(v);
        }
    }

    @Override
    public void On_FormatData(UIContext context) 
    {
        String data = httpRequest.GetData().substring(4);
        
        resultHolder = new AttributeHolder();
        
        resultHolder.AddAttribute("N");
        resultHolder.AddAttribute("SIP");
        resultHolder.AddAttribute("NC");
        resultHolder.AddAttribute("R");
        resultHolder.AddAttribute("LT");
        resultHolder.AddAttribute("C");
        resultHolder.AddAttribute("P");
        resultHolder.AddAttribute("VL");
        resultHolder.AddAttribute("BV");
        resultHolder.AddAttribute("B");
        resultHolder.AddAttribute("S");
        resultHolder.AddAttribute("SV");
        resultHolder.AddAttribute("H");
        resultHolder.AddAttribute("L");
        resultHolder.AddAttribute("V");
        
        resultHolder.InitJSONArray(data, "items");
        
    }



    @Override
    public void On_SaveData(UIContext context, String fileDir, char fileType) 
    {
    
        ArrayList<String> stiHeader = new ArrayList<String>(Arrays.asList(
                "Counter Name", "SIP", "Code", "Rmk", "Last", "Chg",
                "Percent", "Vol", "B vol", "Buy", "Sell", "S vol", "High",
                "Low", "Value")) ;
        
        switch(fileType)
        {
            case 0:
                resultHolder.OutputCSV(fileDir+".csv", stiHeader);
                break;
            case 1:
                resultHolder.OutputTLD(fileDir+".tld", stiHeader);
                break;
            case 2:
                resultHolder.OutputXLS(fileDir+".xls", stiHeader);
                break;
        }
        
    }
    
}
