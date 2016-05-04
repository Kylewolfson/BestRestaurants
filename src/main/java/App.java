
import java.util.Map;
import java.util.HashMap;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import static spark.Spark.*;
import java.util.ArrayList;
import java.util.List;
import org.sql2o.*;

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

    get("/cuisine/:id", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      int cuisineNumber = Integer.parseInt(request.params(":id"));
      List<Restaurant> restaurants = Cuisine.find(cuisineNumber).getRestaurants();
      model.put("restaurants", restaurants);
      model.put("template","templates/restaurants-by-cuisine.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());
  }
}
