package services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import models.Liked;
import play.Logger;

/**
 * @author Jean-Baptiste lemée
 */
public class SearchService {

   static  IndexWriter indexWriter;

   static {
      try {
         buildIndexes();
      } catch (IOException e) {
         Logger.error(e, e.getMessage());
         System.exit(0);
      }
   }

   public static void buildIndexes() throws IOException {
      List<Liked> likedList = Liked.findAll();
      indexWriter = new IndexWriter(new RAMDirectory(), new StandardAnalyzer(Version.LUCENE_30),
              IndexWriter.MaxFieldLength.UNLIMITED);
      buildIndexes(likedList);
   }

   public static void buildIndexes(List<Liked> likedList) throws IOException {
      for (Liked liked : likedList) {
         addToIndex(liked, indexWriter);
      }
      indexWriter.optimize();
   }

   public static void addToIndex(Liked liked) throws IOException {
      addToIndex(liked, indexWriter);
   }

   private static void addToIndex(Liked liked, IndexWriter indexWriter) throws IOException {
      Document doc = new Document();
      doc.add(new Field("id", String.valueOf(liked.id),
              Field.Store.YES,
              Field.Index.NOT_ANALYZED));
      doc.add(new Field("name", liked.name,
              Field.Store.NO,
              Field.Index.ANALYZED));
      doc.add(new Field("contents", liked.description,
              Field.Store.NO,
              Field.Index.ANALYZED));
      indexWriter.addDocument(doc);
   }

   public static List<Liked> search(String queryText) throws IOException, ParseException {
      IndexSearcher searcher = new IndexSearcher(indexWriter.getReader().reopen());
      BooleanQuery query = new BooleanQuery();
      query.add(new FuzzyQuery(new Term("contents", queryText)),
              BooleanClause.Occur.SHOULD);
      query.add(new FuzzyQuery(new Term("name", queryText)),
              BooleanClause.Occur.SHOULD);
      query.add(new QueryParser(
              Version.LUCENE_30,
              "contents",
              new StandardAnalyzer(Version.LUCENE_30)).parse(queryText), BooleanClause.Occur.SHOULD);
      query.add(new QueryParser(
              Version.LUCENE_30,
              "name",
              new StandardAnalyzer(Version.LUCENE_30)).parse(queryText), BooleanClause.Occur.SHOULD);

      TopDocs hits = searcher.search(query, 10);
      List<Document> docs = new ArrayList<Document>(hits.totalHits);
      for (ScoreDoc sdoc : hits.scoreDocs) {
         docs.add(searcher.doc(sdoc.doc));
      }

      return fromDocToLiked(docs);
   }

   private static List<Liked> fromDocToLiked(List<Document> documents) {
      List<Liked> result = new ArrayList<Liked>();
      for (Document doc : documents) {
         result.add(Liked.<Liked>findById(Long.valueOf(doc.getField("id").stringValue())));
      }
      return result;
   }

}
