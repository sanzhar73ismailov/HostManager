/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.biostat.lishostmanager.web.lab.servlet;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import kz.biostat.lishostmanager.comport.util.DatabaseConnectionPool;

@WebListener()
public class ContainerContextClosedHandler implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("********** Starting Host Manager application");
       
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("********** Stoping Host Manager application");
        Enumeration<java.sql.Driver> drivers = DriverManager.getDrivers();

        Driver driver = null;

        // clear drivers
        while (drivers.hasMoreElements()) {
            try {
                driver = drivers.nextElement();
                DriverManager.deregisterDriver(driver);

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        kz.biostat.lishostmanager.comport.instrument.InstrumentIndicator.getInstance().stopAll();
        try {
            DatabaseConnectionPool.getInstance().destroy();
        } catch (SQLException ex) {
            Logger.getLogger(ContainerContextClosedHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
