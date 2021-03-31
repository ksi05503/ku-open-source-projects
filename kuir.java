package main;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class kuir {
    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException, ClassNotFoundException {


        String arg = args[0];
        if(arg.equals("-c")) {

            if(args[1] != null) {

                String dir = args[1];

                File path = new File(dir);

                File[] fileList = path.listFiles();

                makeCollection mc = new makeCollection();
                mc.mkCollection(fileList);

            }
        }else if(arg.equals("-k")){
            if (args[1] != null) {
                String collection_dir = args[1];
                File collection = new File(collection_dir);
                makeKeyword mk = new makeKeyword();
                mk.mkKeyword(collection);
            }
        }else if(arg.equals("-i")){
            if (args[1] != null) {
                String index_dir = args[1];
                indexer indexer = new indexer();
                indexer.saveHashmap(index_dir);
                indexer.read();

            }
        }
    }
}
