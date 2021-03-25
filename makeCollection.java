package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.w3c.dom.Element;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


public class makeCollection {
    public void mkCollection(File[] fileList) {
       if (fileList.length > 0) {   // empty?

            int id =0;
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = null;
            try {
                docBuilder = docFactory.newDocumentBuilder();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }
            org.w3c.dom.Document new_document = docBuilder.newDocument();    // new doc

            Element docs = new_document.createElement("docs");

            new_document.appendChild(docs);

            for (int i = 0; i < fileList.length; i++) { // parse
                try {
                    File input = new File(String.valueOf(fileList[i]));
                    String file = String.valueOf(input);

                    if (file.contains(".html")) {

                        Document document = Jsoup.parse(input, "UTF-8"); // library

                        Element doc = new_document.createElement("doc");
                        Element title = new_document.createElement("title");
                        Element body = new_document.createElement("body");

                        doc.setAttribute("id", String.valueOf(id));

                        title.appendChild(new_document.createTextNode(document.title()));
                        body.appendChild(new_document.createTextNode(document.text()));

                        doc.appendChild(title);
                        doc.appendChild(body);
                        docs.appendChild(doc);

                        id++;
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
            try {  // xml
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

                DOMSource source = new DOMSource(new_document);
                StreamResult result = new StreamResult(new FileOutputStream(new File("/Users/ksi05/Desktop/openSW01/src/main/simpleIR/collection.xml")));
                transformer.transform(source, result);
            } catch (TransformerException | FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

}