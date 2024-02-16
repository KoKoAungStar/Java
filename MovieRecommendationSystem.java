import java.util.*;

public class MovieRecommendationSystem {
    private Map<String, Map<Movie, Double>> ratings;

    public MovieRecommendationSystem() {
        ratings = new HashMap<>();
    }

    public void addRatings(String user, Map<Movie, Double> movieRatings) {
        ratings.put(user, movieRatings);
    }

    public List<Movie> getRecommendations(String user) {
        Map<Movie, Double> userRatings = ratings.get(user);
        if (userRatings == null) {
            System.out.println("User '" + user + "' not found.");
            return Collections.emptyList();
        }

        Map<Movie, Double> scores = new HashMap<>();
        Map<Movie, Integer> freq = new HashMap<>();

        for (Map.Entry<String, Map<Movie, Double>> entry : ratings.entrySet()) {
            String otherUser = entry.getKey();
            if (!otherUser.equals(user)) {
                Map<Movie, Double> otherRatings = entry.getValue();
                for (Map.Entry<Movie, Double> movieRating : otherRatings.entrySet()) {
                    Movie movie = movieRating.getKey();
                    double rating = movieRating.getValue();

                    if (!userRatings.containsKey(movie)) {
                        scores.putIfAbsent(movie, 0.0);
                        scores.put(movie, scores.get(movie) + rating);
                        freq.putIfAbsent(movie, 0);
                        freq.put(movie, freq.get(movie) + 1);
                    }
                }
            }
        }

        for (Movie movie : scores.keySet()) {
            scores.put(movie, scores.get(movie) / freq.get(movie));
        }

        List<Map.Entry<Movie, Double>> sortedMovies = new ArrayList<>(scores.entrySet());
        sortedMovies.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        List<Movie> recommendations = new ArrayList<>();
        for (Map.Entry<Movie, Double> entry : sortedMovies) {
            recommendations.add(entry.getKey());
        }
        return recommendations;
    }

    public static void main(String[] args) {
        MovieRecommendationSystem recommendationSystem = new MovieRecommendationSystem();

        // Sample ratings
        Map<Movie, Double> user1Ratings = new HashMap<>();
        user1Ratings.put(new Movie("Inception"), 5.0);
        user1Ratings.put(new Movie("The Dark Knight"), 4.0);
        user1Ratings.put(new Movie("Interstellar"), 3.0);
        recommendationSystem.addRatings("User1", user1Ratings);

        Map<Movie, Double> user2Ratings = new HashMap<>();
        user2Ratings.put(new Movie("Inception"), 4.0);
        user2Ratings.put(new Movie("The Dark Knight"), 5.0);
        user2Ratings.put(new Movie("Pulp Fiction"), 4.0);
        recommendationSystem.addRatings("User2", user2Ratings);

        Map<Movie, Double> user3Ratings = new HashMap<>();
        user3Ratings.put(new Movie("The Dark Knight"), 3.0);
        user3Ratings.put(new Movie("Interstellar"), 4.0);
        user3Ratings.put(new Movie("Pulp Fiction"), 5.0);
        recommendationSystem.addRatings("User3", user3Ratings);

        // Get recommendations for User1
        List<Movie> recommendations = recommendationSystem.getRecommendations("User1");
        System.out.println("Recommended movies for User1:");
        for (Movie movie : recommendations) {
            System.out.println(movie.getTitle());
        }
    }
}
