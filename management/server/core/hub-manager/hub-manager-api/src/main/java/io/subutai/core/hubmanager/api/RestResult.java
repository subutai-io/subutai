package io.subutai.core.hubmanager.api;


import org.apache.http.HttpStatus;


public class RestResult<T>
{
    private final int status;

    private String error = "";

    private T entity;
    private String reasonPhrase;


    public RestResult( int status )
    {
        this.status = status;
    }


    public int getStatus()
    {
        return status;
    }


    public boolean isSuccess()
    {
        return status == HttpStatus.SC_OK || status == HttpStatus.SC_NO_CONTENT || status == HttpStatus.SC_CREATED;
    }


    public String getError()
    {
        return error;
    }


    public void setError( String error )
    {
        this.error = error;
    }


    public T getEntity()
    {
        return entity;
    }


    public void setEntity( T entity )
    {
        this.entity = entity;
    }


    public void setReasonPhrase( final String reasonPhrase )
    {
        this.reasonPhrase = reasonPhrase;
    }


    public String getReasonPhrase()
    {
        return reasonPhrase;
    }
}
