package io.subutai.common.util;


import java.util.Random;
import java.util.Set;

import io.subutai.common.protocol.Tunnels;


/**
 * P2P utils.
 */
public abstract class P2PUtil
{
    public static String P2P_SUBNET_MASK = "255.255.255.0";
    public static final String P2P_INTERFACE_IP_PATTERN = "^10\\..*";


    public static String findFreeP2PSubnet( final Set<String> excludedNetworks )
    {
        String result = null;
        int i = 11;
        int j = 0;

        while ( result == null && i < 200 )
        {
            String s = String.format( "10.%d.%d.0", i, j );
            if ( !excludedNetworks.contains( s ) )
            {
                result = s;
            }

            j++;
            if ( j > 254 )
            {
                i++;
                j = 0;
            }
        }

        return result;
    }


    public static String findFreeContainerSubnet( final Set<String> excludedNetworks )
    {
        String result = null;
        int i = 168;
        int j = 1;

        while ( result == null && i < 255 )
        {
            String s = String.format( "192.%d.%d.0", i, j );
            if ( !excludedNetworks.contains( s ) )
            {
                result = s;
            }

            j++;
            if ( j > 254 )
            {
                i++;
                j = 0;
            }
        }

        return result;
    }


    public static String generateHash( final String envId )
    {
        return String.format( "swarm-%s", envId );
    }


    public static String generateInterfaceName( final int vlan )
    {
        return String.format( "p2p-%d", vlan );
    }


    public static String generateTunnelName( Tunnels tunnels )
    {
        int maxIterations = 10000;
        int currentIteration = 0;
        String name;

        Random rnd = new Random();

        do
        {
            int n = 10000 + rnd.nextInt( 90000 );
            name = String.format( "tunnel-%d", n );
            currentIteration++;
        }
        while ( tunnels.findByName( name ) != null && currentIteration < maxIterations );

        if ( tunnels.findByName( name ) != null )
        {
            return null;
        }

        return name;
    }
}
