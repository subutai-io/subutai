package io.subutai.common.protocol;


import org.codehaus.jackson.annotate.JsonProperty;


/**
 * P2P config
 */
public class P2PConfig
{
    @JsonProperty( "peerId" )
    private String peerId;
    @JsonProperty( "interfaceName" )
    private String interfaceName;
    @JsonProperty( "communityName" )
    private String communityName;
    @JsonProperty( "address" )
    private String address;
    @JsonProperty( "secretKey" )
    private String secretKey;
    @JsonProperty( "environmentId" )
    private String environmentId;
    @JsonProperty( "secretKeyTtlSec" )
    private long secretKeyTtlSec;


    public P2PConfig( @JsonProperty( "peerId" ) final String peerId,
                      @JsonProperty( "environmentId" ) final String environmentId,
                      @JsonProperty( "interfaceName" ) final String interfaceName,
                      @JsonProperty( "communityName" ) final String communityName,
                      @JsonProperty( "address" ) final String address,
                      @JsonProperty( "secretKey" ) final String secretKey,
                      @JsonProperty( "secretKeyTtlSec" ) final long secretKeyTtlSec )
    {
        this.peerId = peerId;
        this.environmentId = environmentId;
        this.interfaceName = interfaceName;
        this.communityName = communityName;
        this.address = address;
        this.secretKey = secretKey;
        this.secretKeyTtlSec = secretKeyTtlSec;
    }


    public long getSecretKeyTtlSec()
    {
        return secretKeyTtlSec;
    }


    public String getPeerId()
    {
        return peerId;
    }


    public void setPeerId( final String peerId )
    {
        this.peerId = peerId;
    }


    public String getInterfaceName()
    {
        return interfaceName;
    }


    public void setInterfaceName( final String interfaceName )
    {
        this.interfaceName = interfaceName;
    }


    public String getCommunityName()
    {
        return communityName;
    }


    public String getAddress()
    {
        return address;
    }


    public void setAddress( final String address )
    {
        this.address = address;
    }


    public String getSecretKey()
    {
        return secretKey;
    }


    @Override
    public boolean equals( final Object o )
    {
        if ( this == o )
        {
            return true;
        }
        if ( !( o instanceof P2PConfig ) )
        {
            return false;
        }

        final P2PConfig config = ( P2PConfig ) o;

        return address.equals( config.address );
    }


    @Override
    public int hashCode()
    {
        return address.hashCode();
    }


    public String getEnvironmentId()
    {
        return environmentId;
    }
}
