 

import java.util.ArrayList;
import java.util.List;

public class Scene {

    private List<Sprite> sprites;
    private List<Text> text;

    protected TextureManager textureManager;
    protected FontManager fontManager;

    Scene() {
        sprites = new ArrayList<Sprite>();
        text = new ArrayList<Text>();

        textureManager = new TextureManager();
        fontManager = new FontManager();
    }

    public void update(double deltaTime) {

    }

    public void draw() {}

    protected void draw(Sprite sprite) {
        this.sprites.add(sprite);
    }

    protected void draw(Text text) {
        this.text.add(text);
    }

    public void clear() {
        sprites.clear();
        text.clear();
    }

    public void destroy() {
        textureManager.destroy();
        fontManager.destroy();
    }

    // Getters
    public List<Sprite> getSprites() { return sprites; }
    public List<Text> getText() { return text; }

}