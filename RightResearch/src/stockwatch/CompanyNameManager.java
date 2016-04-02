/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package stockwatch;

import eac.AttributeHolder;
import eac.URLDownloader;
import java.util.ArrayList;

/**
 *
 * @author loke.k
 */
public class CompanyNameManager 
{
    String url = "http://www.sgx.com/proxy/SgxDominoHttpProxy?timeout=100&dominoHost=http%3A%2F%2Finfofeed.sgx.com%2FApps%3FA%3DCow_CorporateInformation_Content%26B%3DCorpDistributionByExDate%26C_T%3D-1";
    
    NameCache       m_current;
    
    NameCache       m_cache;
    
    ArrayList<CompanyWatcher> m_watchers;
    
    
    public CompanyNameManager()
    {
        m_current = new NameCache();
        m_watchers = new ArrayList<>();
        m_cache = null;
    }
    
    
    
    public boolean Initialize() //if cache is null, sets
    {
        URLDownloader urlDownloader = new URLDownloader(url);
        urlDownloader.DownloadData(null);
        String rawData = urlDownloader.GetInputData();

        
        //delimiting the initial spaces
        String data = rawData.substring(4);
        
        AttributeHolder corpAction = new AttributeHolder();
        corpAction.AddAttribute("CompanyName");
        
        boolean success = corpAction.InitJSONArray(data, "items");
        if(!success)
            return false;
        ArrayList<String> companyNames = corpAction.GetElements("CompanyName");
        
        for(String elem : companyNames)
            m_current.AddElement(elem.replace("\"", ""));
        
        
        if(m_cache == null)
        {
            m_cache = m_current;
        }
        
        
        return true;
    }
    
    public void CreateNewCache()
    {
        m_cache = new NameCache();
    }
    
    
    
    public boolean DownloadNewUpdates()
    {
        m_current.Clear();
        
        URLDownloader urlDownloader = new URLDownloader(url);
        urlDownloader.DownloadData(null);
        String rawData = urlDownloader.GetInputData();

        
        //delimiting the initial spaces
        String data = rawData.substring(4);
        
        AttributeHolder corpAction = new AttributeHolder();
        corpAction.AddAttribute("CompanyName");
        
        boolean success = corpAction.InitJSONArray(data, "items");
        if(!success)
            return false;
        
        ArrayList<String> companyNames = corpAction.GetElements("CompanyName");
        
        for(String elem : companyNames)
            m_current.AddElement(elem.replace("\"", ""));
        
        return true;
        
    }
    
    public void GetChangesAndSwap()
    {
        ArrayList<String> add = new ArrayList<>();
        ArrayList<String> del = new ArrayList<>();
        
        //get list of elements for the cache to add
        m_cache.GetAdd(m_current.GetSet(), add);
        //get list of elements for the cache to remove
        m_cache.GetRemove(m_current.GetSet(), del);
        
        m_cache = m_current;
        
        //inform the watchers
        for(CompanyWatcher watcher : m_watchers)
        {
            for(String addElem : add)
                watcher.OnNewCompany(addElem, true);
            
            for(String delElem : del)
                watcher.OnCompanyRemove(delElem, true);
        } 
    }
    
    
    public void FillCacheFromCurrent()
    {
        if(m_cache == null)
            m_cache = new NameCache();
        
        for(String addElem : m_current.GetSet())
            m_cache.AddElement(addElem);
    }
    
    public void PopulateValuesFrmCache(boolean inform)
    {
        if(m_cache == null)
        {
            m_cache = new NameCache();
        }
        
        for(String addElem : m_cache.GetSet())
        {
            if(inform)
                for(CompanyWatcher watcher : m_watchers)
                    watcher.OnNewCompany(addElem, false);
        }
    }
    
    NameCache GetCurrent()
    {
        return m_current;
    }
    
    public void RegisterWatcher(CompanyWatcher watcher)
    {
        m_watchers.add(watcher);
    }
    
    
    public void LoadCache(String file)
    {
        m_cache = new NameCache();
        m_cache.Load(file);
    }
    
    public void SaveCache(String file)
    {
        if(m_cache != null)
            m_cache.Write(file);
    }
    
}
