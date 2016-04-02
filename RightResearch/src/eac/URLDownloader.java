package eac;

import java.net.*;
import java.io.*;

/**
 *
 * @author loke.k
 */
public class URLDownloader {
    
    private boolean m_okay;
    
    URL                 m_urlObject;
    HttpURLConnection   m_urlConnection;
    
    StringBuilder       m_dataSb;
    
    public URLDownloader(String url)
    {
        m_okay  = true;
        m_dataSb = new StringBuilder();
        
        try
        {
            m_urlObject = new URL(url);
  
            m_urlConnection = (HttpURLConnection)m_urlObject.openConnection();
            m_urlConnection.setRequestMethod("GET");
            //m_urlConnection.setRequestMethod("HEAD");
            //m_urlConnection.setRequestMethod("POST");
            
            //m_urlConnection.setRequestProperty("range", "bytes=" + 0 + "-" + 3900000);
            //m_urlConnection.setRequestProperty("range", "bytes=" + 0 + "-" + 24);
            
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            m_okay = false;
        }
        
    }
    
    public void SetRangeRequest(long min, long max)
    {
        
        m_urlConnection.setRequestProperty("Range", "bytes=" + min + "-" + max);
        
    }
    
    public void SetURL(String url)
    {
        System.out.println("Set URL!!");
        try
        {
            m_urlObject = new URL(url);
            m_urlConnection = (HttpURLConnection)m_urlObject.openConnection();
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            m_okay = false;
        }
    }
    
    public void DownloadData(String eol)
    {
        m_dataSb.delete(0, m_dataSb.length());
        m_okay = true;
        
        
        try
        {
            m_urlConnection.connect();

            /*
            BufferedReader in = new BufferedReader(new InputStreamReader(m_urlConnection.getInputStream()));
            String input;
            
            while( (input = in.readLine()) != null )
            {
                m_dataSb.append(input);
                if(eol != null)
                    m_dataSb.append(eol);
            }
            in.close();
             * 
             */
            
            InputStream inputStream = m_urlConnection.getInputStream();
            int b;
            boolean m_prevLF = false;
            while( (b = inputStream.read()) != -1 )
            {
                char c = (char)b;
                if(c == '\n' || c == '\r')
                {
                    if(!m_prevLF)
                    {
                        m_prevLF = true;
                        if(eol != null)
                            m_dataSb.append(eol);
                        
                    }
                }
                else
                {
                    m_dataSb.append( c );
                    m_prevLF = false;
                }

            }
            
        }
        catch(Exception e)
        {
            m_okay = false;
        }
        
        System.out.println(m_dataSb.length());
    }
    
    public String GetInputData()
    {
        return m_dataSb.toString();
    }
    
    public boolean IsOkay()
    {
        return m_okay;
    }
    
}
