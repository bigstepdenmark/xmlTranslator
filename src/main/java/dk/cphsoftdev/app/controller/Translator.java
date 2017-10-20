package dk.cphsoftdev.app.controller;

import org.joda.time.DateTime;
import java.text.SimpleDateFormat;

public class Translator
{
    private String data;

    public Translator( String data )
    {
        this.data = data;
    }

    /**
     * Convert request data to XML formatted string.
     *
     * @return String
     */
    public String toXML()
    {
        return build( data.split( "," ) ).toString();
    }

    /**
     * Build request data to XML format
     *
     * @param data String[]
     * @return StringBuilder
     */
    private StringBuilder build( String[] data )
    {
        StringBuilder builder = new StringBuilder();

        // Build XML format
        builder.append( "<LoanRequest>" );
        builder.append( "<ssn>" ).append( data[ 0 ] ).append( "</ssn>" );
        builder.append( "<creditScore>" ).append( data[ 1 ] ).append( "</creditScore>" );
        builder.append( "<loanAmount>" ).append( data[ 2 ] ).append( "</loanAmount>" );
        builder.append( "<loanDuration>" ).append( convertDuration( Integer.parseInt( data[ 3 ] ) ) ).append( "</loanDuration>" );
        builder.append( "</LoanRequest>" );

        return builder;
    }

    private String convertDuration( int duration )
    {
        DateTime time = new DateTime( 0 ).plusMonths( duration ).minusHours( 1 );
        SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss.S" );

        return dateFormat.format( time.toDate() ) + " CET";
    }
}
