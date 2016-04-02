/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package stockwatch;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alex.chiam
 */
public class StockWatch {
    private String m_companyName;
    private String m_symbol;
    private Date m_start;
    private Date m_end;
    
    public StockWatch() {}
    
    /*
    public StockWatch(String _companyName, String _symbol, Date _startdate,
                      Date _startTime, Date _endDate, Date _endTime) {
    m_companyName = _companyName;
    m_symbol = _symbol;
    m_startDate = _startdate;
    m_startTime = _startTime;
    m_endDate = _endDate;
    m_endTime = _endTime;
    }
    
    public String GetCompanyName() {
        return m_companyName;
    }
    
    public String GetSymbol() {
        return m_symbol;
    }
    
    public Date GetStartDate() {
        return m_startDate;
    }
    
    public Date GetStartTime() {
        return m_startTime;
    }
    
    public Date GetEndDate() {
        return m_endDate;
    }

    public Date GetEndTime() {
        return m_endTime;
    }
    
    public void SetCompanyName(String _companyName) {
        m_companyName = _companyName;
    }
    
    public void SetSymbol(String _symbol) {
        m_symbol = _symbol;
    }
    
    public void SetStartDate(Date _startDate) {
        m_startDate = _startDate;
    }
    
    public void SetStartTime(Date _startTime) {
        m_startTime = _startTime;
    }
    
    public void SetEndDate(Date _endDate) {
        m_endDate = _endDate;
    }

    public void SetEndTime(Date _endTime) {
        m_endTime = _endTime;
    }
    
    public boolean CheckIsExpired(Date _date, Date _time) {
        //equal zero if same, < 0 if less than _date, > 0 if more than _date
        if(m_startDate.compareTo(_date) <= 0) {
            if(m_startTime.compareTo(_time) <= 0) {
                return true;
            }
        }
        return false;
    }
    
    
     * */
    
    public boolean isValid() {
        if(m_companyName.isEmpty() || m_symbol.isEmpty() || m_start == null || m_end == null) {
            return false;
        }
           return true;
    }
    
    public String GetCompanyName() {
        return m_companyName;
    }
    
    public String GetSymbol() {
        return m_symbol;
    }
    
    public Date GetStart() {
        return m_start;
    }

    
    public Date GetEnd() {
        return m_end;
    }
    
    public void SetCompanyName(String arg)
    {
        m_companyName = arg;
    }
    
    public void SetSymbol(String arg)
    {
        m_symbol = arg;
    }
    
    public void SetStart(Date arg)
    {
        m_start = arg;
    }
    
    public void SetStart(String arg)
    {
        Date date = null;
        try {
            date = DateFormat.getDateInstance().parse(arg);
            //debug print
            System.out.println("SetStart(String) date: " + date.toString());
            //end debug print
                    } catch (ParseException ex) {
            Logger.getLogger(StockWatch.class.getName()).log(Level.SEVERE, null, ex);
        }
        SetStart(date);
    }
    
    
    public void SetEnd(Date arg)
    {
        m_end = arg;
    }
    
    public void SetEnd(String arg)
    {
        Date date = null;
        try {
            date = DateFormat.getDateInstance().parse(arg);
            //debug print
            System.out.println("SetEnd(String) date: " + date.toString());
            //end debug print
                    } catch (ParseException ex) {
            Logger.getLogger(StockWatch.class.getName()).log(Level.SEVERE, null, ex);
        }
        SetEnd(date);
    }
    
    
    public boolean CheckIsExpired(Date _date, Date _time) {
        //equal zero if same, < 0 if less than _date, > 0 if more than _date
        if(m_start.compareTo(_date) <= 0) {
            if(m_start.compareTo(_time) <= 0) {
                return true;
            }
        }
        return false;
    }
}


