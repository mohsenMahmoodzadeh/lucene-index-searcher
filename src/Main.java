import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.custom.CustomAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.queryparser.classic.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Main {

    static String indexDir = null;
    static String dataDir = null;
    static Set<String> hashSet = new HashSet<String>();

    public static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    public static ArrayList<Document> getDocuments(String content, Analyzer analyzer){
        Scanner scan = new Scanner(content);
        ArrayList<Document> docs = new ArrayList<>();
        int i=0;
        String str = "";
        while (scan.hasNextLine()){
            String readLine = scan.nextLine();
            if(readLine.contains(".I "+(i+207))){
                if(i!=0) {
                    String stringAnalyzed = getAnalyzedString(analyzer,str);
                    Document doc = new Document();
                    doc.add(new TextField("content",stringAnalyzed,Field.Store.YES));
                    docs.add(doc);
                }
                str = "";
                i++;
            }else if(!readLine.contains("I "+(i+207)) && !readLine.contains(".W")){
                str+=" "+readLine;
            }
        }
        //last paragraph remain so it must be documented
        Document doc = new Document();
        String stringAnalyzed = getAnalyzedString(analyzer,str);
        doc.add(new TextField("content",stringAnalyzed, Field.Store.YES));
        docs.add(doc);
        return docs;
    }

    public static void analyze(ArrayList<Document> documents, Analyzer analyzer){
        try{
            Directory dir = FSDirectory.open(Paths.get(indexDir));

            IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
            iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
            IndexWriter writer = new IndexWriter(dir, iwc);
            for(int i=0 ; i<documents.size() ; i++){
                writer.addDocument(documents.get(i));
                if(i==0) iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
            }
            writer.close();
        }catch(IOException e){
            System.out.println(" caught a " + e.getClass() + "\n with message: " + e.getMessage());
        }
    }

    public static String getAnalyzedString(Analyzer analyzer,String content){
        TokenStream tokenStream = analyzer.tokenStream("content",content);
        OffsetAttribute offsetAttribute = tokenStream.addAttribute(OffsetAttribute.class);
        CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
        String stringAnalyzed = "";
        try{
            tokenStream.reset();
            while (tokenStream.incrementToken()) {
                int startOffset = offsetAttribute.startOffset();
                int endOffset = offsetAttribute.endOffset();
                String term = charTermAttribute.toString();
                hashSet.add(term);
                stringAnalyzed+=term+" ";
            }
            tokenStream.close();
        }catch (IOException e){
            System.out.println(" caught a " + e.getClass() + "\n with message: " + e.getMessage());
        }
        return stringAnalyzed;
    }

    public static void main(String[] args) {
        String content = null;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Please enter index directory path:: ");
            indexDir = reader.readLine();
            System.out.print("Please enter data directory path for indexing:: ");
            dataDir = reader.readLine();
            content = readFile(dataDir, StandardCharsets.UTF_8);

            Analyzer analyzer = CustomAnalyzer.builder()
                    .withTokenizer("standard")
                    .addTokenFilter("lowercase")
                    .addTokenFilter("stop")
                    .addTokenFilter("porterstem")
                    .build();

            ArrayList<Document> docs = getDocuments(content,analyzer);

            Analyzer analyzer2 = new WhitespaceAnalyzer();
            analyze(docs,analyzer2);
            Searcher s = new Searcher(indexDir,analyzer2,hashSet);

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}
