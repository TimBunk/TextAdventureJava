import org.joml.Vector3f;

import java.io.IOException;

import framework.Core;
import framework.Window;

public class main {

    public static void main(String args[]) throws IOException {

        Window window = new Window(960, 540, "TextAdventureJava");
        window.use();
        window.setVSync(true);
        window.setBackgroundColor(new Vector3f(0.2f, 0.2f, 0.2f));

        Core core = new Core(window);

        Game game = new Game();

        while (!window.shouldClose() && !game.shouldClose()) {
            core.update(game);
        }

        game.destroy();
        window.destroy();
        core.destroy();

    }
}