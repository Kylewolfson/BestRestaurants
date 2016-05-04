import java.util.List;
import org.sql2o.*;
import java.util.Date;


public class Review {
  private int id;
  private double rating;
  private String review_text;
  private Date review_date;
  private int restaurant_id;


  public Review(double rating, String review_text, Date review_date, int restaurant_id) {
    this.rating = rating;
    this.review_text = review_text;
    this.review_date = review_date;
    this.restaurant_id = restaurant_id;
  }

  public int getId(){
    return id;
  }
  public double getRating(){
    return rating;
  }
  public String getReviewText(){
    return review_text;
  }
  public Date getDate(){
    return review_date;
  }
  public int getRestaurantId(){
    return restaurant_id;
  }
  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO reviews(rating, review_text, review_date, restaurant_id) VALUES (:rating, :review_text, :review_date, :restaurant_id)";
      this.id = (int) con.createQuery(sql, true)
      .addParameter("review_text", this.review_text)
      .addParameter("rating", this.rating)
      .addParameter("review_date", this.review_date)
      .addParameter("restaurant_id", this.restaurant_id)
      .executeUpdate()
      .getKey();
    }
  }

  public static List<Review> all() {
    String sql = "SELECT * FROM reviews";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Review.class);
    }
  }


}
