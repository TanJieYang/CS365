/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package info;

import eac.AttributeHolder;
import eac.HTTPDownloadRequest;
import eac.UIContext;
import eac.WebService;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

/**
 *
 * @author loke.k
 */
public class DownloadCompanyInfo implements WebService{

    
    public static final String url = "http://www.sgx.com/proxy/SgxDominoHttpProxy?timeout=18000&dominoHost=http%3A%2F%2Finfofeed.sgx.com%2FApps%3FA%3DCow_CorporateInformation_Content%26B%3DCorpInfoCompanyNameList%26C_T%3D-1";
    
    
    HTTPDownloadRequest httpRequest;
    
    AttributeHolder     corporateInfo;
    
    int primary = 25;
    int secondary = 75;
    
    @Override
    public boolean On_Initialize(UIContext context) {
        httpRequest = new HTTPDownloadRequest();
        return httpRequest.BeginDownload(url);
    }
    
    @Override
    public void On_DownloadData(UIContext context) {
        
        //download list first
        while(!httpRequest.IsDone())
        {
            httpRequest.RetrieveData(null);
            float ratio = httpRequest.GetRetrieveRatio();
            int v = (int)(ratio * primary);
            context.DisplayText("download in progress ...       - " + v + "%");
            context.SetProgressVal(v);
        }
        String rawData = httpRequest.GetData();
        String data = rawData.substring(4);
        
        
        ArrayList<String> corporateInfoHeader = new ArrayList<String>(Arrays.asList(
                "Company Name", "Document Link")) ;
        
        corporateInfo = new AttributeHolder();
        corporateInfo.AddAttribute("Company Name");
        corporateInfo.AddAttribute("Doc Link");
        
        corporateInfo.InitJSONArray(data, "items");
        
        
        
        
        
    }

    @Override
    public void On_FormatData(UIContext context) {
        
    }

   

    @Override
    public void On_SaveData(UIContext context, String directoryName, char fileType) {
        String link = "http://infopub.sgx.com/Apps?A=Cow_CorporateInformation_Content";
        String join;
        String name;
        PrintWriter outputFile;
        
        corporateInfo.CreateFolder(directoryName);
        
        try {
            for(int i = 1; i < corporateInfo.GetMaxElem(); ++i)
            {
            
                name = corporateInfo.GetData(0,i);
                join = corporateInfo.GetData(1,i);
                
                String urlDownloadLink = link + join;

                String targetDir = directoryName + File.separator + name + ".html";

                outputFile = new PrintWriter(targetDir);
                
                HTTPDownloadRequest request = new HTTPDownloadRequest();
                request.BeginDownload(urlDownloadLink);
                
                float baseRatio = (float)i / corporateInfo.GetMaxElem();
                
                while(!request.IsDone())
                {
                    request.RetrieveData(null);

                    int v = (int)(primary + (baseRatio * secondary));
                    context.DisplayText("download in progress ...       - " + v + "%");
                    context.SetProgressVal(v);
                }
                
                outputFile.print(request.GetData());
                outputFile.close();
            }
            
            context.SetProgressVal(100);
            
        }
        catch (Exception e) {
        }
    }
    
}
