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
public class DownloadISINInfo implements WebService{

    HTTPDownloadRequest mainRequest;
    AttributeHolder     linkHolder;
    
    final String mainURL = "http://sgx.com/proxy/SgxDominoHttpProxy?timeout=2&dominoHost=http%3A%2F%2Finfofeed.sgx.com%2FApps%3FA%3DCOW_App_DB%26B%3Disincodedownload";
    final String targetLink = "http://infopub.sgx.com/Apps?A=COW_App_DB";
    
    ArrayList<String[]>        itemsList;
    ArrayList<AttributeHolder> subHolders;
    
    @Override
    public boolean On_Initialize(UIContext context) 
    {
        mainRequest = new HTTPDownloadRequest();
        return mainRequest.BeginDownload(mainURL);
    }

    
    @Override
    public void On_DownloadData(UIContext context) 
    {
        int primary = 30;
        int secondary = 55;
        
        while(!mainRequest.IsDone())
        {
            mainRequest.RetrieveData(null);
            float completeRatio = mainRequest.GetRetrieveRatio();
            int v = (int)(completeRatio * primary);
                    
            context.DisplayText("download in progress ...       - " + v + "%");
            context.SetProgressVal(v);
        }
        
        linkHolder = new AttributeHolder();
        
        String mainHeaderData = mainRequest.GetData().substring(4);
        linkHolder.AddAttribute("ISIN Code Link");
        linkHolder.InitJSONArray(mainHeaderData, "items");
        
        
        
        //grab all the links
        ArrayList<String> links = linkHolder.GetElements("ISIN Code Link");
        
        subHolders = new ArrayList<AttributeHolder>();
        itemsList = new ArrayList<String[]>();
        
        
        if(links == null)
        {
            return;
        }
        


        for(int i = 0; i < links.size(); ++i)
        {
            String targetDB = links.get(i);
            String downloadDB = targetLink + targetDB.replaceAll("\"", "");
            
            HTTPDownloadRequest dbInfoDownload = new HTTPDownloadRequest();
            if(dbInfoDownload.BeginDownload(downloadDB))
            {
                //download info and update display first
                while(!dbInfoDownload.IsDone())
                {
                    dbInfoDownload.RetrieveData("\n");
                    float completeRatio = mainRequest.GetRetrieveRatio();
                    float baseRatio = ((float)(i+1)) / links.size();
                    
                    int v = (int)(primary + ((completeRatio * baseRatio) * secondary));
                    context.DisplayText("download in progress ...       - " + v + "%");
                    context.SetProgressVal(v);
                }
                
                String[] items = dbInfoDownload.GetData().split("\n");
                itemsList.add(items);
            }
            
            context.DisplayText("download in progress ...       - 85%");
            
        }
        

       
        
        
        
    }

    @Override
    public void On_FormatData(UIContext context) 
    {
        
        
        
        for(int i = 0; i < itemsList.size(); ++i)
        {
            String[] items = itemsList.get(i);
            
            AttributeHolder holder = new AttributeHolder();
                holder.AddAttribute("NAME");
                holder.AddAttribute("STATUS");
                holder.AddAttribute("ISIN CODE");
                holder.AddAttribute("CODE");
                holder.AddAttribute("SHORT NAME");
            
            for(int j = 1; j < items.length; ++j)
            {
                String itemStr = items[j];
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
            
            subHolders.add(holder);
        }
    }


    @Override
    public void On_SaveData(UIContext context, String fileDir, char fileType) 
    {
        
        ArrayList<String> sectorHeader = new ArrayList<String>(Arrays.asList(
        "Name", "Status", "ISIN Code", "Code", "Short Name"));
        
        for(int i = 0; i < subHolders.size(); ++i)
        {
            AttributeHolder holder = subHolders.get(i);
            switch(fileType)
            {
                case 0:
                    holder.OutputCSV(fileDir+".csv", sectorHeader);
                    break;
                case 1:
                    holder.OutputTLD(fileDir+".tld", sectorHeader);
                    break;
                case 2:
                    holder.OutputXLS(fileDir+".xls", sectorHeader);
                    break;
            }
        }

    }
    
}
