/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package stockwatch;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.List;

/**
 *
 * @author loke.k
 */
public class NameCache 

{
    private HashSet<String> m_names;
    
    
    public NameCache()
    {
        m_names = new HashSet<>();
    }
    
    public boolean Load(String fileName)
    {
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            
            String input = null;
            
            while( (input = br.readLine()) != null )
            {
                m_names.add(input);
            }
            
            br.close();
            return true;
            
        }catch(Exception e)
        {e.printStackTrace(); return false;}

    }
    
    public void AddElement(String name)
    {
        if(!m_names.contains(name))
            m_names.add(name);
    }
    
    public void RemoveElement(String name)
    {
        m_names.remove(name);
    }
    
    public boolean Write(String fileName)
    {
        try
        {
            FileWriter fWriter = new FileWriter(fileName);
            //save the whole cache
            for(String elem : m_names)
            {
                fWriter.write(elem + System.lineSeparator());
            }
            fWriter.flush();
            fWriter.close();
            
            return true;
        }
        catch(Exception io)
        {
            io.printStackTrace();
            return false;
        }
        
        
    }
    
    public void Clear()
    {
        m_names.clear();
    }
    
    public HashSet<String> GetSet()
    {
        return m_names;
    }
    
    public void GetAdd(HashSet<String> objects, List<String> results)
    {
        for(String elem : objects)
        {
            if(!m_names.contains(elem))
                results.add(elem);
        }
        
    }
    
    public void GetRemove(HashSet<String> objects, List<String> results)
    {
        for(String elem : m_names)
        {
            if(!objects.contains(elem))
            {
                results.add(elem);
            }
        }
    }
    
    public void debugPrint()
    {
        System.out.println(m_names);
    }
    

}
