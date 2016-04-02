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
public class DownloadSectorSummary implements WebService{

    
    HTTPDownloadRequest httpRequest;
    AttributeHolder     resultHolder;
    
    final String URL = "http://www.sgx.com/JsonRead/JsonData?qryId=RSectorSummary&timeout=60";
    
    
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
                    //
            int v = (int)(completeRatio * 85);
            context.SetProgressVal(v);      
       }

    }

    @Override
    public void On_FormatData(UIContext context) 
    {
        
        String rawData = httpRequest.GetData().substring(4);
        
        resultHolder = new AttributeHolder();
        
        resultHolder.AddAttribute("SN");
        resultHolder.AddAttribute("VL");
        resultHolder.AddAttribute("V");
        resultHolder.AddAttribute("R");
        resultHolder.AddAttribute("F");
        resultHolder.AddAttribute("U");
        
        resultHolder.InitJSONArray(rawData, "items");
        
    }

    @Override
    public void On_SaveData(UIContext context, String fileDir, char fileType) 
    {
        ArrayList<String> sectorHeader = new ArrayList<>(Arrays.asList(
        "Sector", "Volume", "Values", "Rises", "Falls", "Unchanged")) ;
        
        switch(fileType)
        {
            case 0:
                resultHolder.OutputCSV(fileDir+".csv", sectorHeader);
                break;
            case 1:
                resultHolder.OutputTLD(fileDir+".tld", sectorHeader);
                break;
            case 2:
                resultHolder.OutputXLS(fileDir+".xls", sectorHeader);
                break;
        }
    }
    
}
