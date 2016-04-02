package eac;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.net.*;
import java.io.*;
/**
 *
 * @author loke.k
 */
public class HTTPDownloadRequest {
    
    public final static char IS_OKAY    = 0x01;
    public final static char IS_DL      = 0x02; 
    public final static char IS_NON     = 0x04; //not connected state
    public final static char IS_READY   = 0x08;
    
    
    char                m_currentState;
    
    
    URL                 m_urlObject;
    HttpURLConnection   m_connection;
    
    long                m_contentSize;
    
    long                m_minRange;
    long                m_maxRange;
    
    boolean             m_setRange;
    boolean             m_prevLineFeed;
    StringBuilder       m_dataSB;
    
    public HTTPDownloadRequest()
    {
        m_currentState  = IS_NON;
        
        m_urlObject     = null;
        m_connection    = null;
        
        m_contentSize   = -1;
        
        
        m_minRange      = 0;
        m_maxRange      = -1;
        m_setRange      = false;
        m_prevLineFeed  = false;
        
        m_dataSB = new StringBuilder();
        
    }
    
    
    public void SetRange(long min, long max)
    {
        m_setRange = true;
        m_minRange = min;
        m_maxRange = max;
    }
    
    
    public void Reset()
    {
        m_currentState = IS_NON;
        m_urlObject     = null;
        m_connection    = null;
        
        m_contentSize   = -1;
        m_minRange      = 0;
        m_maxRange      = -1;
        m_setRange      = false;
        m_prevLineFeed  = false;
        
        //m_dataSB.delete(0, m_dataSB.length());
        m_dataSB.setLength(0);
        
    }
    
    public String GetData()
    {
        return m_dataSB.toString();
    }
    
    
    public boolean IsDone()
    {
        return m_currentState == IS_OKAY;
    }
    
    public long GetExpectedContentSize()
    {
        return m_contentSize;
    }
    
    public boolean IsUndetermined()
    {
        if(m_contentSize == -1)
            return true;
        
        return false;
    }
    
    public float GetRetrieveRatio()
    {
        if(IsUndetermined())
            return 0.5f;
        
        float ratio = (float)(m_dataSB.length()) / m_contentSize;
        if(ratio > 1.f)
            ratio = 1.f;
        return ratio;
    }
    
    public boolean RetrieveData(String eol)
    {
        if(m_currentState == IS_NON || m_currentState == IS_OKAY)
            return true;
        
        

        try
        {
            //////////////////////////////////////////////////
            if(m_currentState == IS_READY)
            {
                 //initiate GET request
                m_connection = (HttpURLConnection) m_urlObject.openConnection();
                
                //set any range if valid
                if(m_setRange)
                {
                    if(m_maxRange == -1)
                        m_connection.setRequestProperty("Range", "bytes="+m_minRange + "-");
                    else
                        m_connection.setRequestProperty("Range", "bytes="+m_minRange + "-" + m_maxRange);
                }
                
                //connect
                m_connection.connect();
                
                m_currentState = IS_DL;
            }
            /////////////////////////////////////////////////////
            
            InputStream inputStream = m_connection.getInputStream();
            int b;

            if((b = inputStream.read()) != -1)
            {
                char c = (char)b;
                
                
                if(c == '\n' || c == '\r')
                {
                    if(!m_prevLineFeed)
                    {
                         if(eol != null)
                            m_dataSB.append(eol);
                         m_prevLineFeed = true;
                    }
                       
                }
                else
                {
                    m_dataSB.append( c );
                    m_prevLineFeed = false;
                }
                    
                
                return false;
            }
            else
            {
                //we're done
                m_connection.disconnect();
                
                m_currentState = IS_OKAY;
            }
            
            return true;
            
        }
        catch(Exception e){}
        
        
        return true;
    }

    
    //grab header info first
    public boolean BeginDownload(String url)
    {
        if(m_currentState == IS_OKAY)
            return false;
        
        try
        {
            m_urlObject = new URL(url);
            
            //try to make a head request so that we can determine the content length if possible
            m_connection =(HttpURLConnection) m_urlObject.openConnection();
            m_connection.setRequestMethod("HEAD");
            m_connection.connect();
            
            if(m_connection.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                //query content size expectancy
                m_contentSize = m_connection.getContentLengthLong();
                
                m_connection.disconnect();
                
               

                //set state to success downloaded
                m_currentState = IS_READY;
                
                return true;
            }
            
            return false;
        }
        catch(Exception e) 
        {
            e.printStackTrace();
            return false;
        }
    }
    
    
    
    
}
