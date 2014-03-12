/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.safehaus.kiskis.mgmt.impl;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import java.util.ArrayList;
import java.util.List;
import org.safehaus.kiskis.mgmt.api.SomeApi;
import org.safehaus.kiskis.mgmt.api.dbmanager.DbManager;

/**
 *
 * @author bahadyr
 */
public class SomeImpl implements SomeApi {

    private final DbManager dbManager;

    public SomeImpl(DbManager dbManager) {
        this.dbManager = dbManager;
    }

    @Override
    public String sayHello(String name) {
        return name;
    }

    @Override
    public boolean install(String program) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean start(String serviceName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean stop(String serviceName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean status(String serviceName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean purge(String serviceName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean runCommand(String program) {
        return true;
    }

    @Override
    public boolean writeLog(String log) {
        String cql = "insert into logs (id, log) values (?,?)";
        try {
            dbManager.executeUpdate(cql, System.currentTimeMillis() + "", log);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public List<String> getLogs() {
        List<String> list = new ArrayList<String>();
        String cql = "select * from logs";
        ResultSet results = dbManager.executeQuery(cql);
        for (Row row : results) {
            String data = row.getString("log");
            list.add(data);
        }
        return list;
    }

}
