package main;



import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

import java.io.FileOutputStream;
import java.io.IOException;

public class makeKeyword {
    public void mkKeyword(File collection) {

        try{
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = docFactory.newDocumentBuilder();
            org.w3c.dom.Document d = builder.parse(collection);
            NodeList nodeList = d.getElementsByTagName("doc");
            for( int i = 0; i<nodeList.getLength(); i++) {
                Node n = nodeList.item(i);

                KeywordExtractor ke = new KeywordExtractor();
                KeywordList k1 = ke.extractKeyword(n.getLastChild().getTextContent(), true);

                StringBuilder sb = new StringBuilder();
                for(int j=0;j<k1.size(); j++){
                    Keyword kwrd = k1.get(j);
                    sb.append(kwrd.getString() + ":" + kwrd.getCnt() + "#");
                }
                n.getLastChild().setTextContent(sb.toString());
            }
            try {
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

                DOMSource source = new DOMSource (d) ;
                StreamResult result = new StreamResult(new FileOutputStream(new File("/Users/ksi05/SimpleIR/index.xml")));
                transformer.transform(source, result);
            } catch (TransformerConfigurationException e) {
                e.printStackTrace();
            } catch (TransformerException e) {
                e.printStackTrace();
            }

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
