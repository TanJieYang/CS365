/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package stockwatch;

/**
 *
 * @author loke.k
 */
public interface CompanyWatcher {
    
    
    public void OnNewCompany(String name, boolean inform);
    public void OnCompanyRemove(String name, boolean inform);
    
}
