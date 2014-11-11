package org.safehaus.subutai.plugin.lucene.impl.handler;

import org.safehaus.subutai.common.exception.CommandException;
import org.safehaus.subutai.common.protocol.AbstractOperationHandler;
import org.safehaus.subutai.common.protocol.CommandResult;
import org.safehaus.subutai.common.protocol.RequestBuilder;
import org.safehaus.subutai.common.settings.Common;
import org.safehaus.subutai.core.environment.api.helper.Environment;
import org.safehaus.subutai.core.peer.api.ContainerHost;
import org.safehaus.subutai.plugin.common.api.NodeOperationType;
import org.safehaus.subutai.plugin.lucene.api.LuceneConfig;
import org.safehaus.subutai.plugin.lucene.impl.Commands;
import org.safehaus.subutai.plugin.lucene.impl.LuceneImpl;

import java.util.Iterator;

/**
 * Created by ebru on 09.11.2014.
 */
public class NodeOperationHandler extends AbstractOperationHandler<LuceneImpl, LuceneConfig> {

    private String clusterName;
    private String hostname;
    private NodeOperationType operationType;

    public NodeOperationHandler( final LuceneImpl manager, String clusterName, final String hostname,
                                 NodeOperationType operationType )
    {
        super( manager, manager.getCluster( clusterName ) );
        this.hostname = hostname;
        this.clusterName = clusterName;
        this.operationType = operationType;
        this.trackerOperation = manager.getTracker()
                .createTrackerOperation( LuceneConfig.PRODUCT_KEY,
                        String.format( "Creating %s tracker object...", clusterName ) );
    }


    @Override
    public void run()
    {
        LuceneConfig config = manager.getCluster( clusterName );
        if ( config == null )
        {
            trackerOperation.addLogFailed( String.format( "Cluster with name %s does not exist", clusterName ) );
            return;
        }

        Environment environment = manager.getEnvironmentManager().getEnvironmentByUUID( config.getEnvironmentId() );
        Iterator iterator = environment.getContainers().iterator();
        ContainerHost host = null;
        while ( iterator.hasNext() )
        {
            host = ( ContainerHost ) iterator.next();
            if ( host.getHostname().equals( hostname ) )
            {
                break;
            }
        }

        if ( host == null )
        {
            trackerOperation.addLogFailed( String.format( "No Container with ID %s", hostname ) );
            return;
        }


        CommandResult result = null;
        switch ( operationType )
        {
            case INSTALL:
                result = installProductOnNode( host );
                break;
            case UNINSTALL:
                result = uninstallProductOnNode( host );
        }



    }
    private CommandResult installProductOnNode( ContainerHost host )
    {
        CommandResult result = null;
        try
        {
            result = host.execute( new RequestBuilder(
                    Commands.installCommand ) );
            if ( result.hasSucceeded() )
            {
                config.getNodes().add( host.getId() );
                manager.getPluginDao().saveInfo( LuceneConfig.PRODUCT_KEY, config.getClusterName(), config );
                trackerOperation.addLog(
                        LuceneConfig.PRODUCT_KEY + " is installed on node " + host.getHostname() + " successfully." );
            }
            else
            {
                trackerOperation.addLogFailed( "Could not install " + LuceneConfig.PRODUCT_KEY + " to node " + hostname );
            }
        }
        catch ( CommandException e )
        {
            e.printStackTrace();
        }
        return result;
    }


    private CommandResult uninstallProductOnNode( ContainerHost host )
    {
        CommandResult result = null;
        try
        {
            result = host.execute( new RequestBuilder(
                    Commands.uninstallCommand ) );
            if ( result.hasSucceeded() )
            {
                config.getNodes().remove( host.getId() );
                manager.getPluginDao().saveInfo( LuceneConfig.PRODUCT_KEY, config.getClusterName(), config );
                trackerOperation.addLog(
                        LuceneConfig.PRODUCT_KEY + " is uninstalled from node " + host.getHostname() + " successfully." );
            }
            else
            {
                trackerOperation
                        .addLogFailed( "Could not uninstall " + LuceneConfig.PRODUCT_KEY + " from node " + hostname );
            }
        }
        catch ( CommandException e )
        {
            e.printStackTrace();
        }
        return result;
    }

}
