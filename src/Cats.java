package src;

public class Cats {
    String id;
    String url;
    String apiKey = Secrets.API_KEY;
    String image;

    public String getId () {
        return id;
    }

    public String getUrl () {
        return url;
    }

    public String getImage () {
        return image;
    }

    public void setImage (String image) {
        this.image = image;
    }
}
