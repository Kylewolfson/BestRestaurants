import java.util.List;
import org.sql2o.*;


public class Restaurant {
  private String name;
  private int id;
  private int cuisine_id;
  private String description;

  public Restaurant (String name, int cuisine_id) {
    this.name = name;
    this.cuisine_id = cuisine_id;

  }

  public String getName(){
    return name;
  }

  public String getCuisineName(){
    return Cuisine.find(this.cuisine_id).getName();
  }

  @Override
  public boolean equals(Object otherRestaurant) {
    if (!(otherRestaurant instanceof Restaurant)) {
      return false;
    } else {
      Restaurant newRestaurant = (Restaurant) otherRestaurant;
      return this.getId() == newRestaurant.getId();
    }
  }

  public int getId(){
    return id;
  }

  public String getDescription(){
    return description;
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO restaurants(name, cuisine_id) VALUES (:name, :cuisine_id)";
      this.id = (int) con.createQuery(sql, true)
      .addParameter("name", this.name)
      .addParameter("cuisine_id", this.cuisine_id)
      .executeUpdate()
      .getKey();
    }
  }

  public void updateDescription(String description){
    this.description = description;
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE restaurants SET description = :description WHERE id = :id;";
      con.createQuery(sql)
      .addParameter("id", this.id)
      .addParameter("description", description)
      .executeUpdate();
    }
  }

  public static List<Restaurant> all() {
    String sql = "SELECT * FROM restaurants";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Restaurant.class);
    }
  }
}
