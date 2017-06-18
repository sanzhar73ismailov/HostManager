/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lab.servlet;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import util.DatabaseConnectionPool;

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
        instrument.InstrumentIndicator.getInstance().stopAll();
        try {
            DatabaseConnectionPool.getInstance().destroy();
        } catch (SQLException ex) {
            Logger.getLogger(ContainerContextClosedHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
