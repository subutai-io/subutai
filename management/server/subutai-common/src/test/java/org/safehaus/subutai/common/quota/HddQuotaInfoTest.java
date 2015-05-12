package org.safehaus.subutai.common.quota;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertNotNull;


@RunWith( MockitoJUnitRunner.class )
public class HddQuotaInfoTest
{
    private HddQuotaInfo hddQuotaInfo;
    private HddQuotaInfo hddQuotaInfo2;


    @Before
    public void setUp() throws Exception
    {
        hddQuotaInfo = new HddQuotaInfo( DiskPartition.HOME, "10-G" );
        hddQuotaInfo2 = new HddQuotaInfo( DiskPartition.HOME, "test" );
    }


    @Test
    public void testProperties() throws Exception
    {
        assertNotNull( hddQuotaInfo.getMemory() );
        assertNotNull( hddQuotaInfo2.getQuotaValue() );
        assertNotNull( hddQuotaInfo.getQuotaKey() );
        assertNotNull( hddQuotaInfo.getPartition() );
    }
}