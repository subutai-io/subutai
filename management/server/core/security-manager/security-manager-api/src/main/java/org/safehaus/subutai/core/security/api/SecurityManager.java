package org.safehaus.subutai.core.security.api;


import java.util.Set;

import org.safehaus.subutai.common.peer.ContainerHost;


public interface SecurityManager
{
    public org.apache.shiro.mgt.SecurityManager getSecurityManager();

    public void configSshOnAgents( Set<ContainerHost> containers ) throws SecurityManagerException;

    public void addSshKeyToAuthorizedKeys( String sshKey, Set<ContainerHost> containerHosts )
            throws SecurityManagerException;

    public void configSshOnAgents( Set<ContainerHost> containerHosts, ContainerHost containerHost )
            throws SecurityManagerException;

    public void configHostsOnAgents( Set<ContainerHost> containerHosts, String domainName )
            throws SecurityManagerException;
}

