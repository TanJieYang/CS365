package eac;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author alex.chiam
 * will be converting to Yahoo Finance API
 * still use code below for dividends output
*/
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Calendar;
import java.io.*;

import yahoofinance.Stock;

import yahoofinance.YahooFinance;

import yahoofinance.histquotes.*;

import java.util.Scanner;

public class YahooStockDownloader {

    private String m_CompanyName;
    private Stock m_stocks;
    private List<HistoricalQuote> m_dList;
    private List<HistoricalQuote> m_wList;
    private List<HistoricalQuote> m_mList;
    //for dividends
    private HTTPDownloadRequest request;
    private Scanner input;
    
    //constructor takes in name of company to get stocks
    public YahooStockDownloader(String _companyName) {
        m_CompanyName = _companyName;
    }
    
    //function to change name of company
    public void SetCompanyName(String _companyName) {
        m_CompanyName = _companyName;
    }
    
    //YahooFinance API to request download with company name
    public boolean GetYahooFinanceRequest() {
        try {
            m_stocks = YahooFinance.get(m_CompanyName, true);
            return true;
        }
        catch(Exception e) {
        return false;
        }
    }
    
    public boolean GetDividendRequest(GregorianCalendar _start, GregorianCalendar _end) {
        try {
            String url = "http://real-chart.finance.yahoo.com/table.csv?s=" + m_CompanyName +
                          "&a="  + _start.get(Calendar.MONTH) +
                          "&b="  + _start.get(Calendar.DAY_OF_MONTH) +
                          "&c="  + _start.get(Calendar.YEAR) +
                          "&d="  + _end.get(Calendar.MONTH) +
                          "&e="  + _end.get(Calendar.DAY_OF_MONTH) +
                          "&f="  + _end.get(Calendar.YEAR) +
                          "&g=v" +
                          "&ignore=.csv";
                
            request = new HTTPDownloadRequest();
            boolean valid = request.BeginDownload(url);
            if(!valid) {
                System.out.println("Dividends failed");
                return false;
            }
            while(!request.IsDone()) {
                request.RetrieveData("\n");
            }
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
    
    
    public boolean GetDailyHistorical(GregorianCalendar _start, GregorianCalendar _end) {
        try {
         m_dList = m_stocks.getHistory(_start, _end, Interval.DAILY);
         return true;
        }
        catch(Exception e) {
            return false;
        }
        
    }
    
    public boolean GetWeeklyHistorical(GregorianCalendar _start, GregorianCalendar _end) {
        try {
         m_wList = m_stocks.getHistory(_start, _end, Interval.WEEKLY);
         return true;
        }
        catch(Exception e) {
            return false;
        }
    }
        
    //parse stocks into 3 separate list
    public boolean GetMonthlyHistorical(GregorianCalendar _start, GregorianCalendar _end) {
        try {
         m_mList = m_stocks.getHistory(_start, _end, Interval.MONTHLY);
         return true;
        }
        catch(Exception e) {
            return false;
        }
    }
    
    public boolean GetDividendHistorical() {
        input = new Scanner(request.GetData());
        if(input.hasNext()) {
           input.nextLine();
        }
        return true;
    }
    
    public boolean PrintDailyQuotes() {
        try {
            File daily = new File(m_CompanyName +"-daily.csv");
            try (FileWriter fWriter = new FileWriter(daily)) {
                fWriter.write("Date,Open,High,Low,Close,Volume,Adj Close\n");
                int sz = m_dList.size();
                for(int i = 0; i < sz; ++i) {
                    fWriter.write(GetDates(i, m_dList) + ",");
                    fWriter.write(GetOpens(i, m_dList) + ",");
                    fWriter.write(GetHighs(i, m_dList) + ",");
                    fWriter.write(GetLows(i, m_dList) + ",");
                    fWriter.write(GetClose(i, m_dList) + ",");
                    fWriter.write(GetVolumes(i, m_dList).toString() + ",");
                    fWriter.write(GetAdjcloses(i, m_dList) + "\n");
                }
                fWriter.flush();
            }
            return true;
        }
        catch (Exception e)  {
            return false;
        }
    }
    
    public boolean PrintWeeklyQuotes() {
        try {
            File weekly = new File(m_CompanyName +"-weekly.csv");
            try (FileWriter fWriter = new FileWriter(weekly)) {
                fWriter.write("Date,Open,High,Low,Close,Volume,Adj Close\n");
                int sz = m_wList.size();
                for(int i = 0; i < sz; ++i) {
                    fWriter.write(GetDates(i, m_wList) + ",");
                    fWriter.write(GetOpens(i, m_wList) + ",");
                    fWriter.write(GetHighs(i, m_wList) + ",");
                    fWriter.write(GetLows(i, m_wList) + ",");
                    fWriter.write(GetClose(i, m_wList) + ",");
                    fWriter.write(GetVolumes(i, m_wList).toString() + ",");
                    fWriter.write(GetAdjcloses(i, m_wList) + "\n");
                }
                fWriter.flush();
            }
            return true;
        }
        catch (Exception e)  {
            return false;
        }
    }
        
    public boolean PrintMonthlyQuotes() {
        try {
            File monthly = new File(m_CompanyName +"-hist_prices-monthly.csv");
            try (FileWriter fWriter = new FileWriter(monthly)) {
                fWriter.write("Date,Open,High,Low,Close,Volume,Adj Close\n");
                int sz = m_mList.size();
                for(int i = 0; i < sz; ++i) {
                    fWriter.write(GetDates(i, m_mList) + ",");
                    fWriter.write(GetOpens(i, m_mList) + ",");
                    fWriter.write(GetHighs(i, m_mList) + ",");
                    fWriter.write(GetLows(i, m_mList) + ",");
                    fWriter.write(GetClose(i, m_mList) + ",");
                    fWriter.write(GetVolumes(i, m_mList).toString() + ",");
                    fWriter.write(GetAdjcloses(i, m_mList) + "\n");
                }
                fWriter.flush();
            }
            return true;
        }
        catch (Exception e)  {
            return false;
        }
    }
    
    public boolean PrintDividendsQuotes() {
        System.out.println("Printing Info...");;
        File file = new File(m_CompanyName +"-hist_prices-dividends.csv");
        try {
            FileWriter fWriter = new FileWriter(file);
            fWriter.write("Date,Dividends\n");
            while(input.hasNextLine()) {
               String line = input.nextLine();
               fWriter.write(line + "\n");
            }
            fWriter.flush();
            fWriter.close();
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
    
    //function to print all 3 files
    //If you want to break this function into smaller parts also can...
    public void PrintAllQuotes() {
        try {
            File daily = new File(m_CompanyName +"-daily.csv");
            File weekly = new File(m_CompanyName +"-weekly.csv");
            File monthly = new File(m_CompanyName +"-hist_prices-monthly.csv");

            FileWriter fWriter = new FileWriter(daily);
            fWriter.write("Date,Open,High,Low,Close,Volume,Adj Close\n");
            int sz = m_dList.size();
            for(int i = 0; i < sz; ++i) {
                fWriter.write(GetDates(i, m_dList) + ",");
                fWriter.write(GetOpens(i, m_dList) + ",");
                fWriter.write(GetHighs(i, m_dList) + ",");
                fWriter.write(GetLows(i, m_dList) + ",");
                fWriter.write(GetClose(i, m_dList) + ",");
                fWriter.write(GetVolumes(i, m_dList).toString() + ",");
                fWriter.write(GetAdjcloses(i, m_dList) + "\n");
            }
            fWriter.flush();
            fWriter.close();
            
            //for weekly file
            fWriter = new FileWriter(weekly);
            fWriter.write("Date,Open,High,Low,Close,Volume,Adj Close\n");
            sz = m_wList.size();
            for(int i = 0; i < sz; ++i) {
                fWriter.write(GetDates(i, m_wList) + ",");
                fWriter.write(GetOpens(i, m_wList) + ",");
                fWriter.write(GetHighs(i, m_wList) + ",");
                fWriter.write(GetLows(i, m_wList) + ",");
                fWriter.write(GetClose(i, m_wList) + ",");
                fWriter.write(GetVolumes(i, m_wList).toString() + ",");
                fWriter.write(GetAdjcloses(i, m_wList) + "\n");
            }
            fWriter.flush();
            fWriter.close();
            
            //for monthly file
            fWriter = new FileWriter(monthly);
            fWriter.write("Date,Open,High,Low,Close,Volume,Adj Close\n");
            sz = m_mList.size();
            for(int i = 0; i < sz; ++i) {
                fWriter.write(GetDates(i, m_mList) + ",");
                fWriter.write(GetOpens(i, m_mList) + ",");
                fWriter.write(GetHighs(i, m_mList) + ",");
                fWriter.write(GetLows(i, m_mList) + ",");
                fWriter.write(GetClose(i, m_mList) + ",");
                fWriter.write(GetVolumes(i, m_mList).toString() + ",");
                fWriter.write(GetAdjcloses(i, m_mList) + "\n");
            }
            fWriter.flush();
            fWriter.close();
        }
        catch(Exception e) {
        }
    }
    
    //I want to pass by reference though... or should I just create functions
    //for each list?
    private String GetDates(int _index, List<HistoricalQuote> _list) {
        return _list.get(_index).getDate().get(Calendar.YEAR) + "-" +
               _list.get(_index).getDate().get(Calendar.MONTH) + "-" +
               _list.get(_index).getDate().get(Calendar.DAY_OF_MONTH);
    }
    
    private String GetOpens(int _index, List<HistoricalQuote> _list) {
        return _list.get(_index).getOpen().toString();
    }
    
    private String GetHighs(int _index, List<HistoricalQuote> _list) {
        return _list.get(_index).getHigh().toString();
    }
    
    private String GetLows(int _index, List<HistoricalQuote> _list) {
        return _list.get(_index).getLow().toString();
    }
    
    private String GetClose(int _index, List<HistoricalQuote> _list) {
        return _list.get(_index).getClose().toString();
    }
        
    private Long GetVolumes(int _index, List<HistoricalQuote> _list) {
        return _list.get(_index).getVolume();
    }
    
    private String GetAdjcloses(int _index, List<HistoricalQuote> _list) {
        return _list.get(_index).getAdjClose().toString();
    }

}



              