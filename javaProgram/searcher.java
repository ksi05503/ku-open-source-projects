package main;

import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.*;

public class searcher {
    public ArrayList<Double> CalcSim(String query, String post_dir) throws IOException, ClassNotFoundException {
        KeywordExtractor ke = new KeywordExtractor();
        KeywordList kl = ke.extractKeyword(query, true);

        FileInputStream fileInputStream = new FileInputStream(post_dir);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

        Object object = objectInputStream.readObject();
        objectInputStream.close();

        HashMap<String, ArrayList<String>> hashMap = (HashMap) object;

        HashMap<String, String> map_word = new HashMap<>();
        for (Keyword kw : kl) {
            String[] split_value = hashMap.get(kw.getString()).toString().replace("[", "").replace("]", "").split(" ");

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < split_value.length; i += 2) {
                int index = Integer.parseInt(split_value[i]);
                double weight = Double.parseDouble(split_value[i + 1]);
                for (int j = 0; j < 5; j++) {
                    if (index == j)
                        sb.append(j + ":" + weight + ";");
                    else sb.append(j + ":0;");
                }
//                System.out.println(sb);
            }
            map_word.put(kw.getString(), kw.getCnt() + "/" + sb);
        }

        HashMap<Integer, String> result = new HashMap<>();
        for (int id = 0; id < 5; id++) {
            double inner_product = 0.0;
            double size_tf = 0.0;
            double size_weight = 0.0;
            double cos = 0.0;
            for (String key : map_word.keySet()) {
                String[] split_tf = map_word.get(key).split("/");
                double tf = Double.parseDouble(split_tf[0]);
                String[] split_value = split_tf[1].split(";");
                double weight = Double.parseDouble(split_value[id].split(":")[1]);

                inner_product += tf * weight;
                size_tf += Math.pow(tf, 2);
                size_weight += Math.pow(weight, 2);
            }
            size_tf = Math.sqrt(size_tf);
            size_weight = Math.sqrt(size_weight);
            cos = inner_product / (size_tf * size_weight);
//            result.put(id, String.format("%.2f", inner_product));
            if(Double.isNaN(cos)){
                cos = 0;
            }
            result.put(id, String.format("%.2f", cos));
        }

        ArrayList<Double> sim = new ArrayList<>();
        for (int id : result.keySet()) {
            System.out.println("문서"+id+":"+result.get(id));
            sim.add(Double.parseDouble(result.get(id)));
        }
        return sim;
    }

    public void printTitle(ArrayList<Double> sim, String dir_collection) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = docFactory.newDocumentBuilder();
        org.w3c.dom.Document document = builder.parse(dir_collection);

        ArrayList<Double> copy_sim = new ArrayList<>(sim);
        Collections.copy(copy_sim, sim);

        for (int i = 0; i < copy_sim.size(); i++) {
            for (int j = 0; j < copy_sim.size() - i - 1; j++) {
                if (copy_sim.get(j + 1) > copy_sim.get(j)) {
                    Collections.swap(copy_sim, j, j + 1);
                }
            }
        }

        NodeList nodeList = document.getElementsByTagName("doc");
        ArrayList<Integer> list_index = new ArrayList<>();
        int c = 0;

        for (int i = 0; i < 3; i++) {
            if (copy_sim.get(i) == 0){
                continue;
            }
            int index = sim.indexOf(copy_sim.get(i));
            if (list_index.contains(index) && c == 0) {
                for (int j = 0; j < 5; j++) {
                    if (sim.get(j).equals(sim.get(index))) {
                        c++;
                        if (c == 2) {
                            index = j;
                            break;
                        }
                    }
                }
            } else if (list_index.contains(index) && c == 2) {
                c = 0;
                for (int j = 0; j < 5; j++) {
                    if (sim.get(j).equals(sim.get(index))) {
                        c++;
                        if (c == 3) {
                            index = j;
                        }
                    }
                }
            }
            System.out.println(nodeList.item(index).getFirstChild().getTextContent());
            list_index.add(index);
        }

    }
}
