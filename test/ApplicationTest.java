import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.test.*;
import play.mvc.Http.*;

public class ApplicationTest extends FunctionalTest {

   private static final Logger log = LoggerFactory.getLogger(ApplicationTest.class);

   @Test
   public void testThatIndexPageWorks() {
      Response response = GET("/");
      assertIsOk(response);
      assertContentType("text/html", response);
      assertCharset("utf-8", response);
   }

}