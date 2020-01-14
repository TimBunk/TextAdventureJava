package framework;

import java.io.IOException;
import java.util.HashMap;

/**
 * De fontManager zorgt ervoor dat elke font maar 1 keer kan worden ingeladen.
 */
public class FontManager {

    private HashMap<String, Font> fonts;

    /**
     * Initialiseert de fontManager
     */
    public FontManager() {
        fonts = new HashMap<String, Font>();
    }

    /**
     * Verwijdert alle fonts
     */
    public void destroy() {
        for (Font f : fonts.values()) {
            f.destroy();
        }
        fonts.clear();
    }

    /**
     * Laad een bestande font of maakte een nieuwe aan als hij er geen kan vinden
     *
     * @param filePath path naar de locatie van de font bestand en plaatje. Het kan er als volgt uitzien: "Fonts/OCR_A_Extended"
     * @return Geeft een font terug
     * @throws IOException
     */
    public Font load(String filePath) throws IOException {
        Font f = fonts.get(filePath);
        if (f == null) {
            f = new Font(filePath);
            fonts.put(filePath, f);
        }
        return f;
    }

    /**
     * Verwijdert een enekele font uit de fontmanager
     *
     * @param filePath path naar de locatie van de font bestand en plaatje. Het kan er als volgt uitzien: "Fonts/OCR_A_Extended"
     */
    public void remove(String filePath) {
        fonts.remove(filePath).destroy();
    }

}