package dk.cphsoftdev.app.controller;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class SenderController
{
    private String bankExchange;
    private String queueName;
    private String replyToQueueName;
    private String hostname;
    private String username;
    private ConnectionFactory factory;
    private Connection connection;
    Channel channel;

    public SenderController( String bankExchange, String queueName, String replyToQueueName, String hostname, String username )
    {
        this.bankExchange = bankExchange;
        this.queueName = queueName;
        this.replyToQueueName = replyToQueueName;
        this.hostname = hostname;
        this.username = username;

        connect();
    }

    public SenderController( String bankExchange, String queueName, String replyToQueueName )
    {
        this.bankExchange = bankExchange;
        this.queueName = queueName;
        this.replyToQueueName = replyToQueueName;
        this.hostname = "datdb.cphbusiness.dk";
        this.username = "guest";

        connect();
    }


    /**
     * Send message
     *
     * @param message String
     */
    public void sendMessage( String message )
    {
        String response = "Message could not be sent!";

        try
        {
            response = basicPublish( message );
        }
        catch( IOException e )
        {
            e.printStackTrace();
        }

        System.out.println( "\nSending..." );
        System.out.println( "====================================================" );
        System.out.println( response );
    }

    /**
     * Create Factory, connection and channel
     *
     * @return boolean
     */
    public boolean connect()
    {
        try
        {
            return createFactory() && newConnection() && createChannel();
        }
        catch( IOException | TimeoutException e )
        {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Close channel and connection
     *
     * @return boolean
     */
    public boolean close()
    {
        try
        {
            channel.close();
            connection.close();

            return true;
        }
        catch( IOException e )
        {
            e.printStackTrace();
        }
        catch( TimeoutException e )
        {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Declare queue and publish message to channel
     *
     * @param message String
     * @return String
     * @throws IOException
     */
    private String basicPublish( String message ) throws IOException
    {
        channel.queueDeclare( queueName, false, false, false, null );

        AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().replyTo( replyToQueueName )
                                                                    .contentType( "text/plain" )
                                                                    .priority( 1 )
                                                                    .deliveryMode( 2 )
                                                                    .build();

        channel.basicPublish( bankExchange, queueName, properties, message.getBytes() );

        return "[Sent] --> '" + message + "'";
    }

    /**
     * Create and set Factory properties
     *
     * @return boolean
     */
    private boolean createFactory()
    {
        if( factory == null )
            factory = new ConnectionFactory();

        // datdb.cphbusiness.dk or localhost
        factory.setHost( hostname );

        // student or guest
        factory.setUsername( username );

        // cph or guest
        // factory.setPassword( "cph" );

        // 5672 if local else 15672
        // factory.setPort( 15672 );

        return factory.getHost().equals( hostname );
    }

    /**
     * Create Connection
     *
     * @return boolean
     * @throws IOException
     * @throws TimeoutException
     */
    private boolean newConnection() throws IOException, TimeoutException
    {
        if( connection == null )
            connection = factory.newConnection();

        return connection.isOpen();
    }

    /**
     * Create Channel
     *
     * @return boolean
     * @throws IOException
     * @throws TimeoutException
     */
    private boolean createChannel() throws IOException, TimeoutException
    {
        if( channel == null )
            channel = connection.createChannel();

        return channel.isOpen();
    }
}
