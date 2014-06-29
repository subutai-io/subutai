package org.safehaus.subutai.api.aptrepositorymanager;


/**
 * Created by dilshat on 6/23/14.
 */
public class PackageInfo {
    private String status;
    private String name;
    private String description;


    public PackageInfo( final String status, final String name, final String description ) {
        this.status = status;
        this.name = name;
        this.description = description;
    }


    public String getStatus() {
        return status;
    }


    public String getName() {
        return name;
    }


    public String getDescription() {
        return description;
    }


    @Override
    public String toString() {
        return "PackageInfo{" +
                "status='" + status + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
