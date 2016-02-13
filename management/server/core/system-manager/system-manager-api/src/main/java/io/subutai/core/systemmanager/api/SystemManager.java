package io.subutai.core.systemmanager.api;


import io.subutai.core.systemmanager.api.pojo.NetworkSettings;
import io.subutai.core.systemmanager.api.pojo.KurjunSettings;
import io.subutai.core.systemmanager.api.pojo.PeerSettings;
import io.subutai.core.systemmanager.api.pojo.SecuritySettings;
import io.subutai.core.systemmanager.api.pojo.SystemInfo;


/**
 * Created by ermek on 2/6/16.
 */
public interface SystemManager
{
    public SecuritySettings getSecuritySettings();

    public KurjunSettings getKurjunSettings();

    public void setKurjunSettings( KurjunSettings settings );

    public NetworkSettings getNetworkSettings();

    public SystemInfo getSystemInfo();

    public void setPeerSettings();

    public PeerSettings getPeerSettings();

    void setNetworkSettings( String externalIpInterface, String openPort, String securePortX1, String securePortX2,
                             String securePortX3, String specialPortX1 );

    void setSecuritySettings( boolean encryptionEnabled, boolean restEncryptionEnabled, boolean integrationEnabled,
                              boolean keyTrustCheckEnabled );
}
