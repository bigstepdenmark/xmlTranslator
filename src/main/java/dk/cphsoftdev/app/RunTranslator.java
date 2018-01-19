package dk.cphsoftdev.app;

import dk.cphsoftdev.app.controller.ReceiveController;
import dk.cphsoftdev.app.controller.SenderController;
import dk.cphsoftdev.app.controller.Translator;

public class RunTranslator
{
    public static void main( String[] args )
    {
//        ReceiveController receive = new ReceiveController( "group3.bank.normalizator" );
//        SenderController sender = new SenderController( "cphbusiness.bankXML", "", "group3.bank.normalizator" );
//
//        sender.sendMessage( new Translator( "1209855372,500,12000.0,48" ).toXML() );
//        receive.printMessages();
//
//        sender.close();
//        receive.close();

        // ReceiveController receive = new ReceiveController( "group3.reciplist.xmltranslator" );
        // receive.printMessages();
        // receive.close();

        // Start Translator (On production use this)
        // -----------------------------------------------------------------------------------------------
        ReceiveController receiver = new ReceiveController( "group3.reciplist.xmltranslator" );

        if( receiver.isReady() )
        {
            SenderController sender = new SenderController( "cphbusiness.bankXML", "", "group3.bank.normalizator" );
            sender.sendMessage( new Translator( receiver.getMessage() ).toXML() );
            sender.close();
        }

        receiver.close();
    }
}
