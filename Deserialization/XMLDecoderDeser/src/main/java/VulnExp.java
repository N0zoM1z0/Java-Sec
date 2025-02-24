import com.sun.beans.decoder.DocumentHandler;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.beans.XMLDecoder;

public class VulnExp {

    public static void XMLDecode_Deserialize(String path) throws Exception {
        File file = new File(path);
        FileInputStream fis = new FileInputStream(file);
        BufferedInputStream bis = new BufferedInputStream(fis);
        XMLDecoder xd = new XMLDecoder(bis);
        xd.readObject();
        /*
getValue:155, Expression (java.beans)
getValueObject:166, ObjectElementHandler (com.sun.beans.decoder)
getValueObject:123, NewElementHandler (com.sun.beans.decoder)
endElement:169, ElementHandler (com.sun.beans.decoder)
endElement:318, DocumentHandler (com.sun.beans.decoder)
endElement:609, AbstractSAXParser (com.sun.org.apache.xerces.internal.parsers)
emptyElement:183, AbstractXMLDocumentParser (com.sun.org.apache.xerces.internal.parsers)
scanStartElement:1344, XMLDocumentFragmentScannerImpl (com.sun.org.apache.xerces.internal.impl)
next:2787, XMLDocumentFragmentScannerImpl$FragmentContentDriver (com.sun.org.apache.xerces.internal.impl)
next:606, XMLDocumentScannerImpl (com.sun.org.apache.xerces.internal.impl)
scanDocument:510, XMLDocumentFragmentScannerImpl (com.sun.org.apache.xerces.internal.impl)
parse:848, XML11Configuration (com.sun.org.apache.xerces.internal.parsers)
parse:777, XML11Configuration (com.sun.org.apache.xerces.internal.parsers)
parse:141, XMLParser (com.sun.org.apache.xerces.internal.parsers)
parse:1213, AbstractSAXParser (com.sun.org.apache.xerces.internal.parsers)
parse:643, SAXParserImpl$JAXPSAXParser (com.sun.org.apache.xerces.internal.jaxp)
parse:327, SAXParserImpl (com.sun.org.apache.xerces.internal.jaxp)
run:375, DocumentHandler$1 (com.sun.beans.decoder)
run:372, DocumentHandler$1 (com.sun.beans.decoder)
doPrivileged:-1, AccessController (java.security)
doIntersectionPrivilege:76, ProtectionDomain$JavaSecurityAccessImpl (java.security)
parse:372, DocumentHandler (com.sun.beans.decoder)
run:201, XMLDecoder$1 (java.beans)
run:199, XMLDecoder$1 (java.beans)
doPrivileged:-1, AccessController (java.security)
parsingComplete:199, XMLDecoder (java.beans)
readObject:250, XMLDecoder (java.beans)
XMLDecode_Deserialize:18, VulnExp
main:27, VulnExp
         */
        xd.close();
    }


    public static void main(String[] args){
        //XMLDecode Deserialize Test
        String path = "poc.xml";
        try {
            XMLDecode_Deserialize(path);

//            File f = new File(path);
//            SAXParserFactory sf = SAXParserFactory.newInstance();
//            SAXParser sp = sf.newSAXParser();
//
//            DefaultHandler dh = new DefaultHandler();
//            DocumentHandler dh = new DocumentHandler();
//            sp.parse(f, dh);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}