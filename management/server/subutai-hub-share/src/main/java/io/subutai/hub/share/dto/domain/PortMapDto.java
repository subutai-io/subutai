package io.subutai.hub.share.dto.domain;


public class PortMapDto
{
    public enum Protocol
    {
        TCP, UDP, HTTP, HTTPS
    }


    public enum State
    {
        CREATING, RESERVED, USED, DESTROYING, ERROR
    }


    private String containerSSId;

    private Protocol protocol;

    private int internalPort;

    private int externalPort;

    private State state;

    private String stateDetails;

    private String domain;

    private String errorLog;


    public PortMapDto()
    {

    }


    public String getErrorLog()
    {
        return errorLog;
    }


    public void setErrorLog( final String errorLog )
    {
        this.errorLog = errorLog;
    }


    public String getContainerSSId()
    {
        return containerSSId;
    }


    public void setContainerSSId( final String containerSSId )
    {
        this.containerSSId = containerSSId;
    }


    public Protocol getProtocol()
    {
        return protocol;
    }


    public void setProtocol( final Protocol protocol )
    {
        this.protocol = protocol;
    }


    public int getInternalPort()
    {
        return internalPort;
    }


    public void setInternalPort( final int internalPort )
    {
        this.internalPort = internalPort;
    }


    public int getExternalPort()
    {
        return externalPort;
    }


    public void setExternalPort( final int externalPort )
    {
        this.externalPort = externalPort;
    }


    public State getState()
    {
        return state;
    }


    public void setState( final State state )
    {
        this.state = state;
    }


    public String getStateDetails()
    {
        return stateDetails;
    }


    public void setStateDetails( final String stateDetails )
    {
        this.stateDetails = stateDetails;
    }


    public String getDomain()
    {
        return domain;
    }


    public void setDomain( final String domain )
    {
        this.domain = domain;
    }
}
