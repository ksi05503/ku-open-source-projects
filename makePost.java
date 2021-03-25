package main;

import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class makePost {

    public void mkPost(File Index) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = docFactory.newDocumentBuilder();
            org.w3c.dom.Document d = builder.parse(Index);
            NodeList nodeList = d.getElementsByTagName("doc");

            HashMap<String, List<HashMap<Integer, Double>>> hashMap = new HashMap<>();
            String[] textArr = new String[nodeList.getLength()];

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node n = nodeList.item(i);
                Node body = n.getChildNodes().item(1);
                textArr[i] = body.getTextContent();
            }

            for (int i = 0; i < textArr.length; i++) {
                String[] test = textArr[i].split("#"); // 단어별 정제

                for (String item : test) {
                    String[] splitedText = item.split(":"); // 이름 및 반복횟수 정제
//                    System.out.println(Arrays.toString(splitedText));
                    String keywordName = splitedText[0];
                    int keywordCount = Integer.parseInt(splitedText[1]);
                    HashMap<Integer, Double> itemMap = new HashMap<>();
                    int docKeywordFrequency = 0;

                    for (String docText : textArr) {
                        String[] test2 = docText.split("#"); // 단어별 정제
                        for (String item2 : test2) {
                            String[] splitedText2 = item2.split(":"); // 이름 및 반복횟수 정제
                            String keywordName2 = splitedText2[0];
                            if (keywordName2.contains(keywordName)) {
                                docKeywordFrequency++;
                                break;
                            }
                        }
                    }
                    double termFrequency = keywordCount * Math.log((double) nodeList.getLength() / (double) docKeywordFrequency);
                    itemMap.put(i, termFrequency);

                    if (hashMap.get(keywordName) != null) {
                        List<HashMap<Integer, Double>> prevData = hashMap.get(keywordName);
                        prevData.add(itemMap);
                        hashMap.put(keywordName, prevData);
                    } else {
                        List<HashMap<Integer, Double>> newData = new ArrayList<>();
                        newData.add(itemMap);
                        hashMap.put(keywordName, newData);
                    }
                }
            }

            // 새 파일 선언
            FileOutputStream fileStream = new FileOutputStream("main/simpleIR/Index.post");
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileStream, StandardCharsets.UTF_8);
            BufferedWriter bw = new BufferedWriter(outputStreamWriter);

            // 파일에 버퍼로 추가 및 출력
            bw.append(hashMap.toString());
            bw.flush();

            bw.close();
            outputStreamWriter.close();
            fileStream.close();
        } catch (Exception ignored) {

        }

    }
}