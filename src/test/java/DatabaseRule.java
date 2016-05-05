import org.junit.rules.ExternalResource;
import org.sql2o.*;

public class DatabaseRule extends ExternalResource {

@Override
  protected void before() {
    DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/restaurants_test", null, null);
  }

@Override
  protected void after() {
    try(Connection con = DB.sql2o.open()) {
      String deleteRestuarantsksQuery = "DELETE FROM restaurants *;";
      String deleteReviewsQuery = "DELETE FROM reviews *;";
      String deleteCuisinesQuery = "DELETE FROM cuisines *;";
      con.createQuery(deleteRestuarantsksQuery).executeUpdate();
      con.createQuery(deleteReviewsQuery).executeUpdate();
      con.createQuery(deleteCuisinesQuery).executeUpdate();
    }
  }
}
