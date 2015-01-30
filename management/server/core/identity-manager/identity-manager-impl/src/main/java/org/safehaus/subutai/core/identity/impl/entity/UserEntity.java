package org.safehaus.subutai.core.identity.impl.entity;


import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.safehaus.subutai.core.identity.api.User;


/**
 * Implementation of User interface. Used for storing user information.
 */
@Entity
@Table( name = "subutai_user" )
@Access( AccessType.FIELD )
public class UserEntity implements User
{
    @Id
    @GeneratedValue
    @Column( name = "user_id" )
    private Long id;

    @ManyToMany( fetch = FetchType.EAGER, cascade = CascadeType.ALL )
    @JoinTable( name = "subutai_users_roles", joinColumns = @JoinColumn( name = "user_name" ), inverseJoinColumns =
    @JoinColumn( name = "role_name" ) )
    Set<RoleEntity> roles = new HashSet();

    @Column( name = "user_name" )
    private String username;

    @Column( name = "full_name" )
    private String fullname;

    @Column( name = "password" )
    private String password;

    @Column( name = "salt" )
    private String salt;

    @Column( name = "permissions" )
    private String permissions;

    @Column( name = "e_mail" )
    private String email;


    @Column( name = "key" )
    private String key = "Empty key: not implemented yet.";


    @Override
    public Long getId()
    {
        return id;
    }


    @Override
    public String getPassword()
    {
        return password;
    }


    @Override
    public List<String> getPermissions()
    {
        return Arrays.asList( permissions.split( ";" ) );
    }


    public void setId( final Long id )
    {
        this.id = id;
    }


    //    public void setRoles( final List<Role> roles )
    //    {
    //        this.roles = roles;
    //    }


    @Override
    public void setPassword( final String password )
    {
        this.password = password;
    }


    public String getUsername()
    {
        return username;
    }


    @Override
    public void setUsername( final String username )
    {
        this.username = username;
    }


    public String getSalt()
    {
        return salt;
    }


    public void setSalt( final String salt )
    {
        this.salt = salt;
    }


    public void setPermissions( final String permissions )
    {
        this.permissions = permissions;
    }


    public void addRole( final RoleEntity roleEntity )
    {
        roles.add( roleEntity );
    }


    public void setFullname( final String fullname )
    {
        this.fullname = fullname;
    }


    public void setEmail( final String email )
    {
        this.email = email;
    }


    @Override
    public String getFullname()
    {
        return fullname;
    }


    @Override
    public String getEmail()
    {
        return email;
    }


    @Override
    public String getKey()
    {
        return this.key;
    }


    @Override
    public void setKey( final String key )
    {
        this.key = key;
    }
}
