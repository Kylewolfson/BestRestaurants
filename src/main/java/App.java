
import java.util.Map;
import java.util.HashMap;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import static spark.Spark.*;
import java.util.ArrayList;
import java.util.List;
import org.sql2o.*;
import java.util.Date;

public class App {
  public static void main(String[] args) {
    staticFileLocation("/public");
    String layout = "templates/layout.vtl";

    get("/", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      List<Cuisine> cuisines = Cuisine.all();
      model.put("cuisines", cuisines);
      List<Restaurant> restaurants = Restaurant.all();
      model.put("restaurants", restaurants);
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/saveNewRestaurant", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      String restaurantName = request.queryParams("name");
      int cuisineId = Integer.parseInt(request.queryParams("cuisineId"));
      Restaurant newRestaurant = new Restaurant(restaurantName, cuisineId);
      newRestaurant.save();
      response.redirect("/");
      return null;
    });

    post("/saveDescription", (request, response) -> {
    Map<String, Object> model = new HashMap<String, Object>();
    int restaurant_id = Integer.parseInt(request.queryParams("restaurant_id"));
    String description = request.queryParams("description");
    Restaurant restaurant = Restaurant.find(restaurant_id);
    Integer cuisine_id = restaurant.getCuisineId();
    Integer id = restaurant.getId();
    restaurant.updateDescription(description);
    response.redirect("/cuisine/" + cuisine_id.toString() + "/restaurants/" + id.toString());
    return null;
  });

    get("/cuisine", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      int cuisineNumber = Integer.parseInt(request.queryParams("chooseCuisine"));
      String cuisine = Cuisine.find(cuisineNumber).getName();
      List<Restaurant> restaurants = Cuisine.find(cuisineNumber).getRestaurants();
      model.put("restaurants", restaurants);
      model.put("cuisine", cuisine);
      model.put("template","templates/restaurants-by-cuisine.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/cuisine/:cuisine_id/restaurants/:id", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      int id = Integer.parseInt(request.params(":id"));
      Restaurant restaurant = Restaurant.find(id);
      model.put("restaurant", restaurant);
      model.put("reviews", restaurant.getReviews());
      model.put("template","templates/display-restaurant.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/saveReview", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Integer restaurant_id = Integer.parseInt(request.queryParams("restaurant_id"));
      String review_text = request.queryParams("reviewText");
      double rating = Double.parseDouble(request.queryParams("rating"));
      Date currentDate = new Date();
      Review newReview = new Review(rating, review_text, currentDate, restaurant_id);
      newReview.save();
      Restaurant restaurant = Restaurant.find(restaurant_id);
      Integer cuisine_id = restaurant.getCuisineId();
      response.redirect("/cuisine/" + cuisine_id.toString() + "/restaurants/" + restaurant_id.toString());
      return null;
    });
  }
}
