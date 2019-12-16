import org.joml.Vector2f;
import org.joml.Vector3f;

import java.io.IOException;

public class main {

    public static void main(String args[]) throws IOException {

        Window window = new Window(960, 540, "TextAdventureJava");
        window.use();
        window.setVSync(true);
        window.setBackgroundColor(new Vector3f(0.0f, 0.0f, 1.0f));

        TextureManager textureManager = new TextureManager();

        Sprite sprite = new Sprite(250, 250, textureManager.load("Images/awesomeface.png"));
        sprite.setPosition(new Vector2f(480, 270));
        Sprite sprite2 = new Sprite(250, 250, textureManager.load("Images/awesomeface.png"));
        sprite2.setPosition(new Vector2f(0, 270));

        Renderer renderer = new Renderer();
        renderer.addSprite(sprite);
        renderer.addSprite(sprite2);

        while (!window.shouldClose()) {

            // Rendering...
            renderer.render(window);

            Window.pollEvents();
        }

        textureManager.destroy();
        renderer.destroy();
        window.destroy();

    }

}