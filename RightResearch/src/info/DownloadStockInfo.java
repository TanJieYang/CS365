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
public class DownloadStockInfo implements WebService{

    HTTPDownloadRequest httpRequest;
    AttributeHolder     resultHolder;
    
    final String URL = "http://sgx.com/proxy/SgxDominoHttpProxy?timeout=3600&dominoHost=http%3A%2F%2Finfofeed.sgx.com%2FApps%3FA%3DCOW_App_DB%26B%3DStocksNameByInitial%26R_C%3D%26C_T%3D-1";
     
    @Override
    public boolean On_Initialize(UIContext context) {
        httpRequest = new HTTPDownloadRequest();
        return httpRequest.BeginDownload(URL);
    }
    
    @Override
    public void On_DownloadData(UIContext context) {
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
        resultHolder.AddAttribute("Full Name");
        resultHolder.AddAttribute("Market");
        resultHolder.AddAttribute("Sector (SSIC Standard)");
        resultHolder.AddAttribute("CPFIS");
        resultHolder.AddAttribute("Status");
        
        resultHolder.InitJSONArray(data, "items");
    }

    @Override
    public void On_SaveData(UIContext context, String fileDir, char fileType) 
    {
         ArrayList<String> stockHeader = new ArrayList<String>(Arrays.asList(
         "Full Name", "Market", "Sector (SSIC Standard)", "CPFIS", "Status")) ;
         
         switch(fileType)
        {
            case 0:
                resultHolder.OutputCSV(fileDir+".csv", stockHeader);
                break;
            case 1:
                resultHolder.OutputTLD(fileDir+".tld", stockHeader);
                break;
            case 2:
                resultHolder.OutputXLS(fileDir+".xls", stockHeader);
                break;
        }
         
    }
    
}
