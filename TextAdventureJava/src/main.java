import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import java.io.IOException;

public class main {

    public static void main(String args[]) throws IOException {

        //Game game = new Game();
        //game.play();

        Window window = new Window(960, 540, "TextAdventureJava");
        window.use();
        window.setVSync(true);
        window.setBackgroundColor(new Vector3f(0.0f, 0.0f, 1.0f));

        TextureManager textureManager = new TextureManager();
        FontManager fontManager = new FontManager();
        Font f = fontManager.load("Fonts/OCR_A_Extended");

        Text text = new Text(f, "Hello world\ntest how the line will fit into the test how the line will fit into the test how the line will fit into the screen.");
        text.setPosition(new Vector2f(0.0f, 540.0f));
        text.setSize(22.5f);
        text.setMaxWidth(480.0f);

        Text text2 = new Text(f, "Sample");
        text2.setPosition(new Vector2f(0.0f, 450.0f));

        Sprite sprite = new Sprite(250, 250, textureManager.load("Images/awesomeface.png"));
        sprite.setColor(new Vector4f(1.0f, 0.0f, 1.0f, 1.0f));
        sprite.setPosition(new Vector2f(240, 270));
        sprite.setRotation(-45.0f);

        Sprite sprite2 = new Sprite(250, 250, textureManager.load("Images/awesomeface.png"));
        sprite2.setPosition(new Vector2f(480, 270));

        Renderer renderer = new Renderer();
        renderer.add(sprite);
        renderer.add(sprite2);
        renderer.add(text);
        renderer.add(text2);

        while (!window.shouldClose()) {

            // Rendering...
            renderer.render(window);
            // Poll events
            Window.pollEvents();
        }

        textureManager.destroy();
        renderer.destroy();
        window.destroy();

    }

}