import java.util.HashMap;

/**
 * @author Tim Bunk
 * Dit is de texture manager class deze class zorgt ervoor dat er geen dubbele textures worden gemaakt
 */
public class TextureManager {
    private HashMap<String, Texture> textures;

    /**
     * De constructor die alles initialiseert
     */
    TextureManager() {
        textures = new HashMap<String, Texture>();
    }

    /**
     * Roep deze functie aan als je klaar bent met het gebruiken van de texutureManager om alle texture te verwijderen uit het geheugen
     */
    public void destroy() {
        for (Texture t : textures.values()) {
            t.destroy();
        }
    }

    /**
     * Deze functie checkt of het bestand al eerder was geladen en als dat het geval is dan krijg je een bestande texture terug en anders wordt er een nieuwe texture gemaakt en opgeslagen
     * @param filePath path naar de afbeelding
     * @return Je krijgt een texture van deze functie
     */
    public Texture load(String filePath) {
        Texture t = textures.get(filePath);
        if (t == null) {
            t = new Texture(filePath);
            textures.put(filePath, t);
        }
        return t;
    }

    /**
     * Verwijder een specifieke texture uit het geheugen
     * @param filePath path naar de afbeelding die je uit het geheugen wil verwijderen
     */
    public void remove(String filePath) {
        textures.remove(filePath).destroy();
    }

}