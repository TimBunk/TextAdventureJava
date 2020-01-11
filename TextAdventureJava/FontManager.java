import java.io.IOException;
import java.util.HashMap;

public class FontManager {

    private HashMap<String, Font> fonts;

    FontManager() {
        fonts = new HashMap<String, Font>();
    }

    public void destroy() {
        for (Font f : fonts.values()) {
            f.destroy();
        }
    }

    public Font load(String filePath) throws IOException {
        Font f = fonts.get(filePath);
        if (f == null) {
            f = new Font(filePath);
            fonts.put(filePath, f);
        }
        return f;
    }

    public void remove(String filePath) {
        fonts.remove(filePath).destroy();
    }

}