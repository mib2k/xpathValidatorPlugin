package Utils;

public class SharedData {
    static String url = "";

    public static String getUrl() {
        return url;
    }

    public static void setUrl(String url) {
        SharedData.url = url;
    }
}
