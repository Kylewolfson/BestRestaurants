import java.util.List;
import org.sql2o.*;


public class Cuisine {
  private String name;
  private int id;

  public Cuisine (String name) {
    this.name = name;
  }

  public String getName(){
    return name;
  }

  @Override
  public boolean equals(Object otherCuisine) {
    if (!(otherCuisine instanceof Cuisine)) {
      return false;
    } else {
      Cuisine newCuisine = (Cuisine) otherCuisine;
      return this.getId() == newCuisine.getId();
    }
  }

  public int getId(){
    return id;
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO cuisines(name) VALUES (:name)";
      this.id = (int) con.createQuery(sql, true)
      .addParameter("name", this.name)
      .executeUpdate()
      .getKey();
    }
  }

  public static Cuisine find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM cuisines WHERE id = :id";
      Cuisine cuisine = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Cuisine.class);
      return cuisine;
    }
  }

  public static List<Cuisine> all() {
    String sql = "SELECT * FROM cuisines";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Cuisine.class);
    }
  }

  public List<Restaurant> getRestaurants() {
    String sql = "SELECT * FROM restaurants WHERE cuisine_id = :this_cuisine_id";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql)
      .addParameter("this_cuisine_id", this.id)
      .executeAndFetch(Restaurant.class);
    }
  }
}
