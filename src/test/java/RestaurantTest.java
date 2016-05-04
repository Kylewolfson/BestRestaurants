import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;

public class RestaurantTest {

  @Before
  public void setUp() {
    DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/restaurants_test", null, null);
  }

  @After
  public void tearDown() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "DELETE FROM restaurants *;";
      con.createQuery(sql).executeUpdate();
      sql = "DELETE FROM cuisines *;";
      con.createQuery(sql).executeUpdate();
    }
  }

  @Test
  public void Restaurant_instantiatesCorrectly_true() {
    Restaurant myRestaurant = new Restaurant("Pizza hut", 1);
    assertEquals(true, myRestaurant instanceof Restaurant);
  }

  @Test
  public void getName_returnsRestaurantName_PizzaHut() {
    Restaurant myRestaurant = new Restaurant("Pizza hut", 1);
    assertEquals("Pizza hut", myRestaurant.getName());
  }

  @Test
  public void save_savesIntoDatabase_true() {
    Restaurant yourRestaurant = new Restaurant ("Pizza shack", 1);
    yourRestaurant.save();
    assertTrue(Restaurant.all().get(0).equals(yourRestaurant));
  }

  @Test
  public void updateDescription_updatesDesccriptiontoDB_smelly(){
    Restaurant yourRestaurant = new Restaurant ("Pizza shack", 1);
    yourRestaurant.save();
    yourRestaurant.updateDescription("smelly");
    assertEquals("smelly", Restaurant.all().get(0).getDescription());
  }

}
