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
public class DownloadIndices implements WebService{

    HTTPDownloadRequest httpRequest;
    AttributeHolder     resultHolder;
    
    final String        URL = "http://www.sgx.com/JsonRead/JsonData?qryId=NTP.INDICES";
    
    @Override
    public boolean On_Initialize(UIContext context) 
    {
    
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
        resultHolder.AddAttribute("TG");
        resultHolder.AddAttribute("OP");
        resultHolder.AddAttribute("PC");
        resultHolder.AddAttribute("L");
        resultHolder.AddAttribute("H");
        resultHolder.AddAttribute("YL");
        resultHolder.AddAttribute("YH");
        
        resultHolder.InitJSONArray(data, "items");
    }

    @Override
    public void On_SaveData(UIContext context, String fileDir, char fileType) 
    {
        ArrayList<String> indicesHeader = new ArrayList<String>(Arrays.asList(
                "Index Name", "Index Provider%TG@4@-", "Asset Class%TG@0@-", "Country%TG@1@-", "Open%OP", 
                "Previous Close%PC", "Day Range%L%-%H", "52-Week Range%YL%-%YH")) ;
        
        
        switch(fileType)
        {
            case 0:
                resultHolder.OutputCSV(fileDir+".csv", indicesHeader);
                break;
            case 1:
                resultHolder.OutputTLD(fileDir+".tld", indicesHeader);
                break;
            case 2:
                resultHolder.OutputXLS(fileDir+".xls", indicesHeader);
                break;
        }
    }
    
}
