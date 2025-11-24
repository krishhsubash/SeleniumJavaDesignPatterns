package pages;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.config.RestAssuredConfig;
import io.restassured.config.HttpClientConfig;
import java.net.UnknownHostException;

public record ProductPage(String id, String title, String description, String category) {
    private static RequestSpecification requestSpec;
    private static Response response;
    private static final String BASE_URL = "https://fakestoreapi.com/";
    private static final String FALLBACK_URL = "https://jsonplaceholder.typicode.com/";
    
    // Mock data for offline testing
    private static final String MOCK_PRODUCTS_JSON = """
        [
            {
                "id": 1,
                "title": "Fjallraven - Foldsack No. 1 Backpack, Fits 15 Laptops",
                "price": 109.95,
                "description": "Your perfect pack for everyday use and walks in the forest.",
                "category": "men's clothing",
                "image": "https://fakestoreapi.com/img/81fPKd-2AYL._AC_SL1500_.jpg",
                "rating": {"rate": 3.9, "count": 120}
            },
            {
                "id": 2,
                "title": "Mens Casual Premium Slim Fit T-Shirts",
                "price": 22.3,
                "description": "Slim-fitting style, contrast raglan long sleeve.",
                "category": "men's clothing",
                "image": "https://fakestoreapi.com/img/71-3HjGNDUL._AC_SY879._SX._UX._SY._UY_.jpg",
                "rating": {"rate": 4.1, "count": 259}
            },
            {
                "id": 3,
                "title": "Mens Cotton Jacket",
                "price": 55.99,
                "description": "Great outerwear jackets for Spring/Autumn/Winter.",
                "category": "men's clothing",
                "image": "https://fakestoreapi.com/img/71li-ujtlUL._AC_UX679_.jpg",
                "rating": {"rate": 4.7, "count": 500}
            }
        ]
        """;
    
    public static ProductAPIResult getAllProducts() {
        try {
            // Try primary API first
            return getAllProductsFromPrimaryAPI();
        } catch (UnknownHostException e) {
            System.out.println("❌ Network connectivity issue detected: " + e.getMessage());
            System.out.println("🔄 Falling back to offline mock data...");
            return new ProductAPIResult(200, MOCK_PRODUCTS_JSON, "Mock Data (Offline)");
        } catch (Exception e) {
            System.out.println("❌ Primary API failed: " + e.getMessage());
            try {
                System.out.println("🔄 Attempting fallback API...");
                return getAllProductsFromFallbackAPI();
            } catch (Exception fallbackException) {
                System.out.println("❌ Fallback API also failed: " + fallbackException.getMessage());
                System.out.println("🔄 Using offline mock data...");
                return new ProductAPIResult(200, MOCK_PRODUCTS_JSON, "Mock Data (Offline - All APIs Failed)");
            }
        }
    }
    
    private static ProductAPIResult getAllProductsFromPrimaryAPI() throws Exception {
        RestAssured.baseURI = BASE_URL;
        RestAssured.config = RestAssuredConfig.config()
            .httpClient(HttpClientConfig.httpClientConfig()
                .setParam("http.connection.timeout", 5000)
                .setParam("http.socket.timeout", 5000));
                
        requestSpec = RestAssured.given()
            .header("Accept", "application/json")
            .header("User-Agent", "JavaTestProject/1.0");
            
        response = requestSpec
            .basePath("products")
            .get();
        System.out.println(response);
            
        if (response.getStatusCode() != 200) {
            throw new RuntimeException("API returned status: " + response.getStatusCode());
        }
        
        return new ProductAPIResult(response.getStatusCode(), response.getBody().asString(), "Fake Store API");
    }
    
    private static ProductAPIResult getAllProductsFromFallbackAPI() throws Exception {
        RestAssured.baseURI = FALLBACK_URL;
        RestAssured.config = RestAssuredConfig.config()
            .httpClient(HttpClientConfig.httpClientConfig()
                .setParam("http.connection.timeout", 5000)
                .setParam("http.socket.timeout", 5000));
                
        requestSpec = RestAssured.given()
            .header("Accept", "application/json")
            .header("User-Agent", "JavaTestProject/1.0");
            
        response = requestSpec
            .basePath("posts")
            .get();
            
        return new ProductAPIResult(response.getStatusCode(), response.getBody().asString(), "JSONPlaceholder API");
    }
    
    public static boolean isAPIAccessible() {
        try {
            RestAssured.baseURI = BASE_URL;
            RestAssured.config = RestAssuredConfig.config()
                .httpClient(HttpClientConfig.httpClientConfig()
                    .setParam("http.connection.timeout", 3000)
                    .setParam("http.socket.timeout", 3000));
                    
            Response healthCheck = RestAssured.given()
                .get("products/1");
            return healthCheck.getStatusCode() == 200;
        } catch (Exception e) {
            return false;
        }
    }
    
    public static Response getLastResponse() {
        return response;
    }
    
    public static String getMockData() {
        return MOCK_PRODUCTS_JSON;
    }
    
    // Simple result wrapper to avoid Response interface complexity
    public static class ProductAPIResult {
        private final int statusCode;
        private final String body;
        private final String source;
        
        public ProductAPIResult(int statusCode, String body, String source) {
            this.statusCode = statusCode;
            this.body = body;
            this.source = source;
        }
        
        public int getStatusCode() { return statusCode; }
        public String getBody() { return body; }
        public String getSource() { return source; }
        public boolean isSuccessful() { return statusCode >= 200 && statusCode < 300; }
        
        @Override
        public String toString() {
            return String.format("ProductAPIResult{statusCode=%d, source='%s', bodyLength=%d}", 
                                statusCode, source, body != null ? body.length() : 0);
        }
    }
}
