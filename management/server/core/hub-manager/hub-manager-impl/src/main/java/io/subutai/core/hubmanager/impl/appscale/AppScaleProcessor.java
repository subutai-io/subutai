package io.subutai.core.hubmanager.impl.appscale;


import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.http.HttpStatus;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;

import io.subutai.core.hubmanager.api.RestResult;
import io.subutai.core.hubmanager.api.StateLinkProcessor;
import io.subutai.core.hubmanager.impl.http.HubRestClient;
import io.subutai.hub.share.dto.AppScaleConfigDto;


public class AppScaleProcessor implements StateLinkProcessor
{
    private final Logger log = LoggerFactory.getLogger( getClass() );

    private final ExecutorService executor = Executors.newFixedThreadPool( 3 );

    private final Set<String> processLinks = Sets.newConcurrentHashSet();

    private final AppScaleManager appScaleManager;

    private final HubRestClient restClient;


    public AppScaleProcessor( AppScaleManager appScaleManager, HubRestClient restClient )
    {
        this.appScaleManager = appScaleManager;
        this.restClient = restClient;
    }


    @Override
    public boolean processStateLinks( final Set<String> stateLinks )
    {
        for ( String stateLink : stateLinks )
        {
            processLink( stateLink );
        }

        return false;
    }


    private void processLink( final String stateLink )
    {
        if ( !stateLink.contains( "appscale" ) )
        {
            return;
        }

        if ( processLinks.contains( stateLink ) )
        {
            log.debug( "AppScale installation for this link is in progress" );

            return;
        }

        final AppScaleConfigDto config = getData( stateLink );

        Preconditions.checkNotNull( config );


        log.debug( "config: {}", config );


        executor.execute( new Runnable()
        {
            @Override
            public void run()
            {
                processLinks.add( stateLink );

                update( stateLink, "INSTALLING" );

                try
                {
                    appScaleManager.installCluster( config );

                    update( stateLink, "INSTALLED" );
                }
                catch ( Exception e )
                {
                    log.error( "Error to install AppScale cluster: ", e );

                    update( stateLink, e.getMessage() );
                }
                finally
                {
                    processLinks.remove( stateLink );
                }
            }
        } );
    }


    private void update( String link, String state )
    {
        log.debug( "Sending state: {}", state );

        try
        {
            RestResult<Object> restResult = restClient.post( link, state );

            log.debug( "Response: HTTP {} - {}", restResult.getStatus(), restResult.getReasonPhrase() );
        }
        catch ( Exception e )
        {
            log.error( "Error to update AppScale data to Hub: ", e );
        }
    }


    private AppScaleConfigDto getData( String link )
    {
        log.debug( "Getting AppScale data from Hub: {}", link );

        try
        {
            RestResult<AppScaleConfigDto> restResult = restClient.get( link, AppScaleConfigDto.class );

            log.debug( "Response: HTTP {} - {}", restResult.getStatus(), restResult.getReasonPhrase() );

            if ( restResult.getStatus() != HttpStatus.SC_OK )
            {
                log.error( "Error to get AppScale data from Hub: HTTP {} - {}", restResult.getStatus(),
                        restResult.getError() );

                return null;
            }

            return restResult.getEntity();
        }
        catch ( Exception e )
        {
            log.error( "Error to get AppScale data from Hub: ", e );

            return null;
        }
    }
}
