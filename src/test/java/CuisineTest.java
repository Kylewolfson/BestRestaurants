import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.List;

public class CuisineTest {

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
  public void Cuisine_instantiatesCorrectly_true() {
    Cuisine myCuisine = new Cuisine("Italian");
    assertEquals(true, myCuisine instanceof Cuisine);
  }

  @Test
  public void getName_returnsCuisineName_PizzaHut() {
    Cuisine myCuisine = new Cuisine("Italian");
    assertEquals("Italian", myCuisine.getName());
  }

  @Test
  public void save_savesIntoDatabase_true() {
    Cuisine yourCuisine = new Cuisine ("Italian");
    yourCuisine.save();
    assertTrue(Cuisine.all().get(0).equals(yourCuisine));
  }


  @Test
  public void getRestaurant_returnsListOfRestaurantsByCuisine_true() {
    Cuisine yourCuisine = new Cuisine ("Italian");
    yourCuisine.save();
    Cuisine myCuisine = new Cuisine ("Mongolian");
    myCuisine.save();
    Restaurant firstRestaurant = new Restaurant("Pizza hut", yourCuisine.getId());
    Restaurant secondRestaurant = new Restaurant("Pasta shack", yourCuisine.getId());
    Restaurant thirdRestaurant = new Restaurant("Mongolian grill", myCuisine.getId());
    firstRestaurant.save();
    secondRestaurant.save();
    thirdRestaurant.save();
    Restaurant[] restaurants = new Restaurant[] {firstRestaurant, secondRestaurant};
    assertTrue(yourCuisine.getRestaurants().containsAll(Arrays.asList(restaurants)));
  }

  @Test
  public void getRestaurant_returnsNonMatchingRestaurants_false() {
    Cuisine yourCuisine = new Cuisine ("Italian");
    yourCuisine.save();
    Cuisine myCuisine = new Cuisine ("Mongolian");
    myCuisine.save();
    Restaurant firstRestaurant = new Restaurant("Pizza hut", yourCuisine.getId());
    Restaurant secondRestaurant = new Restaurant("Pasta shack", yourCuisine.getId());
    Restaurant thirdRestaurant = new Restaurant("Mongolian grill", myCuisine.getId());
    firstRestaurant.save();
    secondRestaurant.save();
    thirdRestaurant.save();
    Restaurant[] restaurants = new Restaurant[] {thirdRestaurant};
    assertFalse(yourCuisine.getRestaurants().containsAll(Arrays.asList(restaurants)));
  }
  // @Test
  // public void

}
