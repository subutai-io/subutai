package org.safehaus.subutai.plugin.mongodb.impl;


import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import org.safehaus.subutai.api.commandrunner.AgentResult;
import org.safehaus.subutai.api.commandrunner.Command;
import org.safehaus.subutai.api.commandrunner.CommandCallback;
import org.safehaus.subutai.api.manager.exception.EnvironmentBuildException;
import org.safehaus.subutai.api.manager.helper.Environment;
import org.safehaus.subutai.api.manager.helper.Node;
import org.safehaus.subutai.plugin.mongodb.api.MongoClusterConfig;
import org.safehaus.subutai.plugin.mongodb.api.NodeType;
import org.safehaus.subutai.plugin.mongodb.impl.common.CommandType;
import org.safehaus.subutai.plugin.mongodb.impl.common.Commands;
import org.safehaus.subutai.shared.operation.ProductOperation;
import org.safehaus.subutai.shared.protocol.Agent;
import org.safehaus.subutai.shared.protocol.ClusterSetupException;
import org.safehaus.subutai.shared.protocol.ClusterSetupStrategy;
import org.safehaus.subutai.shared.protocol.PlacementStrategy;
import org.safehaus.subutai.shared.protocol.Response;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Range;
import com.google.common.collect.Sets;


/**
 * This is a mongodb cluster setup strategy.
 */
public class MongoDbSetupStrategy implements ClusterSetupStrategy {

    private MongoImpl mongoManager;
    private ProductOperation po;
    private MongoClusterConfig config;


    public MongoDbSetupStrategy( MongoClusterConfig config, ProductOperation po, MongoImpl mongoManager ) {

        Preconditions.checkNotNull( config, "Cluster config is null" );
        Preconditions.checkNotNull( po, "Product operation tracker is null" );
        Preconditions.checkNotNull( mongoManager, "Mongo manager is null" );

        this.mongoManager = mongoManager;
        this.po = po;
        this.config = config;
    }


    public static PlacementStrategy getNodePlacementStrategyByNodeType( NodeType nodeType ) {
        switch ( nodeType ) {
            case CONFIG_NODE:
                return PlacementStrategy.MORE_RAM;
            case ROUTER_NODE:
                return PlacementStrategy.MORE_CPU;
            case DATA_NODE:
                return PlacementStrategy.MORE_HDD;
            default:
                return PlacementStrategy.ROUND_ROBIN;
        }
    }


    @Override
    public MongoClusterConfig setup() throws ClusterSetupException {

        if ( config == null ||
                Strings.isNullOrEmpty( config.getClusterName() ) ||
                Strings.isNullOrEmpty( config.getDomainName() ) ||
                Strings.isNullOrEmpty( config.getReplicaSetName() ) ||
                Strings.isNullOrEmpty( config.getTemplateName() ) ||
                !Sets.newHashSet( 1, 3 ).contains( config.getNumberOfConfigServers() ) ||
                !Range.closed( 1, 3 ).contains( config.getNumberOfRouters() ) ||
                !Sets.newHashSet( 3, 5, 7 ).contains( config.getNumberOfDataNodes() ) ||
                !Range.closed( 1024, 65535 ).contains( config.getCfgSrvPort() ) ||
                !Range.closed( 1024, 65535 ).contains( config.getRouterPort() ) ||
                !Range.closed( 1024, 65535 ).contains( config.getDataNodePort() ) ) {
            po.addLogFailed( "Malformed configuration\nMongoDB installation aborted" );
        }
        if ( mongoManager.getCluster( config.getClusterName() ) != null ) {
            throw new ClusterSetupException(
                    String.format( "Cluster with name '%s' already exists\nInstallation aborted",
                            config.getClusterName() ) );
        }
        //if no nodes are set, setup default environment
        if ( config.getAllNodes().isEmpty() ) {
            try {

                po.addLog( "Building environment..." );

                Environment env = mongoManager.getEnvironmentManager().buildEnvironmentAndReturn(
                        mongoManager.getDefaultEnvironmentBlueprint( config ) );

                Set<Agent> configServers = new HashSet<>();
                Set<Agent> routers = new HashSet<>();
                Set<Agent> dataNodes = new HashSet<>();
                for ( Node node : env.getNodes() ) {
                    if ( NodeType.CONFIG_NODE.name().equalsIgnoreCase( node.getNodeGroupName() ) ) {
                        configServers.add( node.getAgent() );
                    }
                    else if ( NodeType.ROUTER_NODE.name().equalsIgnoreCase( node.getNodeGroupName() ) ) {
                        routers.add( node.getAgent() );
                    }
                    else if ( NodeType.DATA_NODE.name().equalsIgnoreCase( node.getNodeGroupName() ) ) {
                        dataNodes.add( node.getAgent() );
                    }
                }

                if ( configServers.isEmpty() ) {
                    throw new ClusterSetupException( "Config servers are not created" );
                }
                if ( routers.isEmpty() ) {
                    throw new ClusterSetupException( "Routers are not created" );
                }
                if ( dataNodes.isEmpty() ) {
                    throw new ClusterSetupException( "Data nodes are not created" );
                }
                config.setConfigServers( configServers );
                config.setRouterServers( routers );
                config.setDataNodes( dataNodes );
            }
            catch ( EnvironmentBuildException e ) {
                throw new ClusterSetupException( String.format( "Error building environment: %s", e.getMessage() ) );
            }
        }
        else {

            //check if nodes are set
            if ( config.getConfigServers() == null || config.getConfigServers().isEmpty() ) {
                throw new ClusterSetupException( "No config servers are set" );
            }
            if ( config.getDataNodes() == null || config.getDataNodes().isEmpty() ) {
                throw new ClusterSetupException( "No data nodes are set" );
            }
            if ( config.getRouterServers() == null || config.getRouterServers().isEmpty() ) {
                throw new ClusterSetupException( "No routers are set" );
            }
        }

        installMongoCluster();

        po.addLog( "Saving cluster information to database..." );

        if ( mongoManager.getDbManager().saveInfo( MongoClusterConfig.PRODUCT_KEY, config.getClusterName(), config ) ) {
            po.addLog( "Cluster information saved to database" );
        }
        else {
            throw new ClusterSetupException( "Failed to save cluster information to database. Check logs" );
        }

        return config;
    }


    private void installMongoCluster() throws ClusterSetupException {

        List<Command> installationCommands = Commands.getInstallationCommands( config );

        for ( Command command : installationCommands ) {
            po.addLog( String.format( "Running command: %s", command.getDescription() ) );
            final AtomicBoolean commandOK = new AtomicBoolean();

            if ( command.getData() == CommandType.START_CONFIG_SERVERS || command.getData() == CommandType.START_ROUTERS
                    || command.getData() == CommandType.START_DATA_NODES ) {
                mongoManager.getCommandRunner().runCommand( command, new CommandCallback() {

                    @Override
                    public void onResponse( Response response, AgentResult agentResult, Command command ) {

                        int count = 0;
                        for ( AgentResult result : command.getResults().values() ) {
                            if ( result.getStdOut().contains( "child process started successfully, parent exiting" ) ) {
                                count++;
                            }
                        }
                        if ( command.getData() == CommandType.START_CONFIG_SERVERS ) {
                            if ( count == config.getConfigServers().size() ) {
                                commandOK.set( true );
                            }
                        }
                        else if ( command.getData() == CommandType.START_ROUTERS ) {
                            if ( count == config.getRouterServers().size() ) {
                                commandOK.set( true );
                            }
                        }
                        else if ( command.getData() == CommandType.START_DATA_NODES ) {
                            if ( count == config.getDataNodes().size() ) {
                                commandOK.set( true );
                            }
                        }
                        if ( commandOK.get() ) {
                            stop();
                        }
                    }
                } );
            }
            else {
                mongoManager.getCommandRunner().runCommand( command );
            }

            if ( command.hasSucceeded() || commandOK.get() ) {
                po.addLog( String.format( "Command %s succeeded", command.getDescription() ) );
            }
            else {
                throw new ClusterSetupException(
                        String.format( "Command %s failed: %s", command.getDescription(), command.getAllErrors() ) );
            }
        }
    }
}
