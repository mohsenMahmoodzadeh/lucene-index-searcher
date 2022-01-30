import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.queryparser.classic.ParseException;

import java.io.FileWriter;
import java.io.IOException;

import java.nio.file.Paths;

import java.util.Set;

public class Searcher {

    QueryParser queryParser;

    public Searcher(String indexDirectoryPath, Analyzer analyzer, Set<String> hashSet) throws IOException, ParseException {
        FileWriter writer = new FileWriter("result.txt");
        Directory indexDirectory = FSDirectory.open(Paths.get(indexDirectoryPath));
        IndexReader reader = DirectoryReader.open(indexDirectory);

        IndexSearcher searcher = new IndexSearcher(reader);
        queryParser = new QueryParser("content",analyzer);

        for(String queryString : hashSet){
            Query q=queryParser.parse(queryString);
            TopDocs td = searcher.search(q,Integer.MAX_VALUE);
            ScoreDoc[] sd = td.scoreDocs;
            writer.append(queryString + ": ");
            for (int j = 0 ; j< sd.length ; j++){
                Document document = reader.document(sd[j].doc);
                String fieldContent = document.get("content");
                int count = countOccurrences(fieldContent,queryString);
                writer.append("doc" + sd[j].doc + "[" + count + "]");
                if(j < sd.length-1)  {
                    writer.append(", ");
                }
            }
            writer.append("\n");
        }
        writer.close();
    }

    static int countOccurrences(String str, String word) {
//        // split the string by spaces in a
        String a[] = str.split(" ");
//
//        // search for pattern in a
        int count = 0;
        for (int i = 0; i < a.length; i++)
        {
            // if match found increase count
            if (word.equals(a[i]))
                count++;
        }
        return count;
    }
}
