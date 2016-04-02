/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eac;

/**
 *
 * @author loke.k
 */
public interface UIContext {
    
    public void AddDisplayText(String text);
    public void DisplayText(String optional);
    public void ClearDisplayText();
    
    public void SetProgressVal(int v);
    
    
}
