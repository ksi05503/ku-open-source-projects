package main;
import java.io.File;

public class kuir {
    public static void main(String[] args) {


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
        }else if(arg.equals("-p")){
            if (args[1] != null) {
                String collection_dir = args[1];
                File collection = new File(collection_dir);
                System.out.println(collection_dir);
                makePost mp = new makePost();
                mp.mkPost(collection);
            }
        }
    }
}
