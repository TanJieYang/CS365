/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eac;

/**
 *
 * @author loke.k
 */
public interface WebService {
    
    
    
    public boolean  On_Initialize(UIContext context);
    
    public void     On_DownloadData(UIContext context);
    public void     On_FormatData(UIContext context);
    public void     On_SaveData(UIContext context, String fileDir, char fileType);
     
    
    
}
