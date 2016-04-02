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
public class DownloadCorpAction implements WebService
{

    HTTPDownloadRequest httpRequest;
    AttributeHolder     resultHolder;
    
    final String        URL = "http://www.sgx.com/proxy/SgxDominoHttpProxy?timeout=100&dominoHost=http%3A%2F%2Finfofeed.sgx.com%2FApps%3FA%3DCow_CorporateInformation_Content%26B%3DCorpDistributionByExDate%26C_T%3D-1";
    
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
        resultHolder.AddAttribute("CompanyName");
        resultHolder.AddAttribute("Annc_Type");
        resultHolder.AddAttribute("Ex_Date");
        resultHolder.AddAttribute("Record_Date");
        resultHolder.AddAttribute("DatePaid_Payable");
        resultHolder.AddAttribute("Particulars");
        
        resultHolder.InitJSONArray(data, "items");
        
    }



    @Override
    public void On_SaveData(UIContext context, String fileDir, char fileType) 
    {
       ArrayList<String> corporateHeader = new ArrayList<String>(Arrays.asList(
                "Company Name", "Type", "Ex-Date", "Record Date", "Date Paid/Payable", "Particulars")) ;
         
         switch(fileType)
        {
            case 0:
                resultHolder.OutputCSV(fileDir+".csv", corporateHeader);
                break;
            case 1:
                resultHolder.OutputTLD(fileDir+".tld", corporateHeader);
                break;
            case 2:
                resultHolder.OutputXLS(fileDir+".xls", corporateHeader);
                break;
        }
    }
    
}
