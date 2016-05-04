import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.Date;

public class ReviewTest {

  @Before
  public void setUp() {
    DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/restaurants_test", null, null);
  }

  @After
  public void tearDown() {
    try(Connection con = DB.sql2o.open()) {
      String deletePatientsQuery = "DELETE FROM reviews *;";
      String deleteDoctorsQuery = "DELETE FROM restaurants *;";
      con.createQuery(deletePatientsQuery).executeUpdate();
      con.createQuery(deleteDoctorsQuery).executeUpdate();
    }
  }

  @Test
  public void review_instantiatesCorectly_true() {
    Date reviewDate = new Date(81, 11, 18);
    Review newReview = new Review(5.0, "Patient McPatientface", reviewDate, 33);
    assertEquals(true, newReview instanceof Review);
  }

  @Test
  public void getRating_returnsArating_4(){
    Date reviewDate = new Date(81, 11, 18);
    Review testReview = new Review(4, "Patient McPatientface", reviewDate, 33);
    assertEquals(4, testReview.getRating(), 0);
  }

  @Test
  public void averageRating_returns_3point5(){
    Date reviewDate = new Date(81, 11, 18);
    Restaurant yourRestaurant = new Restaurant ("Pizza shack", 1);
    yourRestaurant.save();
    Review testReview = new Review(4, "Good", reviewDate, yourRestaurant.getId());
    Review secondReview = new Review(3, "Not as good", reviewDate, yourRestaurant.getId());
    testReview.save();
    secondReview.save();
    assertEquals(3.5, Restaurant.find(yourRestaurant.getId()).averageRating(), 0);

  }
}
