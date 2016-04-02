/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package stockwatch;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.ListIterator;

/**
 *
 * @author alex.chiam
 */
public class WatchlistManager {
    
    public void Register(StockWatcher watcher)
    {
        m_stockWatchers.add(watcher);
    }
    
    //helper function to check if index is valid
    private boolean IndexIsValid(int _index) {
        if(_index < 0 || _index > m_StockWatchList.size()) {
            return false;
        }
        return true;
    }
    //add new watch to list
    public boolean AddStockWatch(StockWatch _newWatch) {
        if(!_newWatch.isValid())
            return false;
        m_StockWatchList.add(_newWatch);
        return true;
    }
    //delete watch from list
    public boolean DeleteStockWatch(int _index) {
        if(!IndexIsValid(_index)) {
            return false;
        }
        m_StockWatchList.remove(_index);
        return true;
    }
    //only update watch by company name
    public boolean UpdateStockWatchCompany(int _index, String _companyName, String _symbol) {
        if(!IndexIsValid(_index)) {
            return false;
        }
        m_StockWatchList.get(_index).SetCompanyName(_companyName);
        m_StockWatchList.get(_index).SetSymbol(_symbol);
        return true;
    }
    
    public boolean UpdateStockWatchDate(int _index, Date _startDate, Date _endDate) {
        if(!IndexIsValid(_index)) {
            return false;
        }
        
       m_StockWatchList.get(_index).SetStart(_startDate);
       m_StockWatchList.get(_index).SetEnd(_endDate);
       return true;
    }
    

    
    public boolean UpdateStockWatch(int _index, String _companyName, String _symbol, 
                                    Date _startDate, Date _startTime, Date _endDate, Date _endTime) {
        if(!IndexIsValid(_index)) {
            return false;
        }
        
        if(!UpdateStockWatchCompany(_index, _companyName, _symbol)) {
            return false;
        }
        
        if(!UpdateStockWatchDate(_index,_startDate, _endDate)) {
            return false;
        }
                
        
            return true;
    }
        //to initialize your list
    public void Initialize(String fileDir)
    {
        StockCache.Retrieve(m_StockWatchList, fileDir);
        
       
       
        //read data from cache
            //populate stock watch list
    }
    
    public void PollWatchlist() {    
        //need time and date to check if stock watch has expired
        
        Iterator<StockWatch> i = m_StockWatchList.iterator();
        
        ListIterator<StockWatch> il = m_StockWatchList.listIterator();
        
        int index = 0;
        while(il.hasNext())
        {
            StockWatch elem = il.next();
            if(elem.CheckIsExpired(null, null))
            {
                for(StockWatcher watcher : m_stockWatchers)
                    watcher.OnChange(index);
                
                il.remove();
            }
            ++index;
            
        }

    }
    
    ArrayList<StockWatch> m_StockWatchList;
    ArrayList<StockWatcher> m_stockWatchers;
}
