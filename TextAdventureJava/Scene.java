import java.util.ArrayList;
import java.util.List;

/**
 * De scene is een base class
 * Het bevat een list met sprites en een list met text.
 * Ook beschikt de scene over zijn eigen textureManager en fontManager
 */
public class Scene {

    private List<Sprite> sprites;
    private List<Text> text;

    protected TextureManager textureManager;
    protected FontManager fontManager;

    /**
     * Constructor voor de scene
     */
    Scene() {
        sprites = new ArrayList<Sprite>();
        text = new ArrayList<Text>();

        textureManager = new TextureManager();
        fontManager = new FontManager();
    }

    /**
     * Als je de scene laat updaten door de core roep hij automatisch deze functie aan
     * @param deltaTime de tijd in seconden tussen elke frame
     */
    public void update(double deltaTime) {

    }

    /**
     * Als je de scene laat updaten door de core roept hij automatisch deze draw functie aan
     */
    public void draw() {
    }

    /**
     * Voegt een sprite toe aan de list met sprites die vervolgens gerendert gaat worden
     * @param sprite de sprite die je wil renderen
     */
    protected void draw(Sprite sprite) {
        this.sprites.add(sprite);
    }

    /**
     * Voegt een text toe aan de list met text die vervolgens gerendert gaat worden
     * @param text de text die je wil renderen
     */
    protected void draw(Text text) {
        this.text.add(text);
    }

    /**
     * Maakt de lijstjes met sprites en text leeg
     */
    public void clear() {
        sprites.clear();
        text.clear();
    }

    /**
     * Destroyed de textureManager en de fontManager
     * Roep deze functie aan als je de scene niet meer gaat gebruiken
     */
    public void destroy() {
        textureManager.destroy();
        fontManager.destroy();
    }

    // Getters
    public List<Sprite> getSprites() {
        return sprites;
    }

    public List<Text> getText() {
        return text;
    }

}