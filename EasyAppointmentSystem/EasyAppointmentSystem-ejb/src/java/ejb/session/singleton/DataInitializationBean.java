/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.AdminEntitySessionBeanRemote;
import entity.AdminEntity;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;

/**
 *
 * @author Bikram
 */
@Singleton
@LocalBean
@Startup

public class DataInitializationBean {


    public DataInitializationBean(){}
    
    @PostConstruct
    public void postConstruct()
    {
        initializeData();
    }
    
    
    
    private void initializeData()
    {
        
        
    }
    
}
