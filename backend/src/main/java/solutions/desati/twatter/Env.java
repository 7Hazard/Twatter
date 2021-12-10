package solutions.desati.twatter;

public class Env {
    public static final boolean isContainer;
    public static final String postsServiceHost;
    public static final String githubClientSecret;
    public static final String chartsServiceHost;
    public static final String imgurClientId;

    static {
        isContainer = System.getenv("IS_CONTAINER") != null;
        githubClientSecret = System.getenv("TWATTER_GITHUB_CLIENT_SECRET");
        imgurClientId = System.getenv("TWATTER_IMGUR_CLIENT_ID");
        postsServiceHost = isContainer ? "posts-service" : "localhost:34566";
        chartsServiceHost = isContainer ? "charts-service" : "localhost:34567";
    }
}
