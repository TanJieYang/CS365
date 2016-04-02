/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package stockwatch;

import java.io.File;
import java.io.FileWriter;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;

/**
 *
 * @author loke.k
 */
public class CompanyNameUpdater implements CompanyWatcher, Runnable
{

    
    private CompanyNameManager m_compManager;
    
    private JButton m_addButton;
    private JList   m_companyList;
    
    
    private AtomicBoolean m_beginInform;
    private DefaultListModel m_list;
    
    
    final long SLEEP_TIME = 2000;
    
    public CompanyNameUpdater(JButton addButton, JList companyList)
    {
        
        m_addButton = addButton;
        m_companyList = companyList;
        
        m_compManager = new CompanyNameManager();

        
        m_beginInform = new AtomicBoolean();
        m_beginInform.set(false);
        
        
        m_list = new DefaultListModel();
        companyList.setModel(m_list);
        
        
        //m_companyList.getModel().
    }
    
    public void Begin()
    {
        m_compManager.RegisterWatcher(this);
        
        Thread jobThread = new Thread(this);
        jobThread.start();
        
        
    }
    
    
    @Override
    public void run() 
    {
        //check cache
        File cacheFile = new File("companycache.txt");
        if(cacheFile.isFile())
        {
            m_compManager.LoadCache("companycache.txt");
            m_compManager.PopulateValuesFrmCache(true);
        }
        else
        {
            
            while(!m_compManager.DownloadNewUpdates()){}
            
            m_compManager.FillCacheFromCurrent();
            m_compManager.PopulateValuesFrmCache(true);
            
            m_companyList.updateUI();
            
        }
        
        
        m_beginInform.set(true);
        
        while(true)
        {
            if(m_compManager.DownloadNewUpdates())
            {
                m_compManager.GetChangesAndSwap();
                m_compManager.SaveCache("companycache.txt");
                
            }
            
            try
            {
                Thread.sleep(SLEEP_TIME);
            }
            catch(Exception e){}
            
        }
        
        
        
    }
    
    public void SaveMsgToFile(String filename, String message)
    {
        java.util.Date date= new java.util.Date();
        Timestamp time = new Timestamp(date.getTime());
        
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hr = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);
        int sec = cal.get(Calendar.SECOND);
        int milli = cal.get(Calendar.MILLISECOND);
        
        
        try
        {
           FileWriter fileWriter = new FileWriter(filename + "-" + year + ""+month+""+day+""+hr+""+min+""+sec+""+milli + ".txt");
           fileWriter.write(message + " - " +time.toString()+ System.lineSeparator());
           fileWriter.flush();
           fileWriter.close();
        }
        catch(Exception e){e.printStackTrace();}
    }
    
    
    @Override
    public void OnCompanyRemove(String name, boolean inform) {
        
       
       
       synchronized(this)
       {
           m_list.removeElement(name);
       }
        
       if(m_beginInform.get())
       {
           String msg = "A company entry: " + name + " has been removed from SGX";
           JOptionPane.showMessageDialog(null, msg, "Removed Company Entry", JOptionPane.PLAIN_MESSAGE);
           SaveMsgToFile("Company Remove Notice", msg);
           
       }
       
        
        
        
        
    }

    @Override
    public void OnNewCompany(String name, boolean inform) {

        
        
        synchronized(this)
        {
            m_list.addElement(name);
        }

        
       if(m_beginInform.get())
       {
           String msg = "A new entry: " + name + " has been added to SGX";
           JOptionPane.showMessageDialog(null, msg, "New Company Entry", JOptionPane.PLAIN_MESSAGE);
           SaveMsgToFile("New Company Added Notice", msg);
       }
        
        
    }
    
}
