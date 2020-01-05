import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import java.io.IOException;

public class main {

    public static void main(String args[]) throws IOException {

        Window window = new Window(960, 540, "TextAdventureJava");
        window.use();
        window.setVSync(true);
        window.setBackgroundColor(new Vector3f(0.0f, 0.0f, 1.0f));

        Core core = new Core(window);

        Game game = new Game();
        //game.play();

        while (!window.shouldClose()) {
            core.update(game);
        }

        game.destroy();
        window.destroy();
        core.destroy();

    }

}