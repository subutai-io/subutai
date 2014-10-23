package org.safehaus.subutai.plugin.presto.impl;


import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.sql.DataSource;

import org.safehaus.subutai.core.agent.api.AgentManager;
import org.safehaus.subutai.core.command.api.CommandRunner;
import org.safehaus.subutai.core.container.api.container.ContainerManager;
import org.safehaus.subutai.core.environment.api.EnvironmentManager;
import org.safehaus.subutai.core.tracker.api.Tracker;
import org.safehaus.subutai.plugin.common.PluginDaoNew;
import org.safehaus.subutai.plugin.hadoop.api.Hadoop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class PrestoBase
{

    private static final Logger LOG = LoggerFactory.getLogger( PrestoBase.class.getName() );
    CommandRunner commandRunner;
    AgentManager agentManager;
    Tracker tracker;
    Hadoop hadoopManager;
    EnvironmentManager environmentManager;
    ContainerManager containerManager;
    ExecutorService executor;
    PluginDaoNew pluginDAO;
    Commands commands;
    public DataSource dataSource;


    public void init()
    {
        try
        {
            this.pluginDAO = new PluginDaoNew( dataSource );
        }
        catch ( SQLException e )
        {
            LOG.error( e.getMessage(), e );
        }
        this.commands = new Commands( commandRunner );

        executor = Executors.newCachedThreadPool();
    }


    public Commands getCommands()
    {
        return commands;
    }


    public void destroy()
    {
        executor.shutdown();
    }


    public CommandRunner getCommandRunner()
    {
        return commandRunner;
    }


    public void setCommandRunner( CommandRunner commandRunner )
    {
        this.commandRunner = commandRunner;
    }


    public AgentManager getAgentManager()
    {
        return agentManager;
    }


    public void setAgentManager( AgentManager agentManager )
    {
        this.agentManager = agentManager;
    }


    public Tracker getTracker()
    {
        return tracker;
    }


    public void setTracker( Tracker tracker )
    {
        this.tracker = tracker;
    }


    public Hadoop getHadoopManager()
    {
        return hadoopManager;
    }


    public void setHadoopManager( Hadoop hadoopManager )
    {
        this.hadoopManager = hadoopManager;
    }


    public EnvironmentManager getEnvironmentManager()
    {
        return environmentManager;
    }


    public void setEnvironmentManager( EnvironmentManager environmentManager )
    {
        this.environmentManager = environmentManager;
    }


    public void setExecutor( final ExecutorService executor )
    {
        this.executor = executor;
    }


    public ContainerManager getContainerManager()
    {
        return containerManager;
    }


    public void setContainerManager( ContainerManager containerManager )
    {
        this.containerManager = containerManager;
    }


    public PluginDaoNew getPluginDAO()
    {
        return pluginDAO;
    }


    public void setPluginDAO( PluginDaoNew pluginDAO )
    {
        this.pluginDAO = pluginDAO;
    }
}
