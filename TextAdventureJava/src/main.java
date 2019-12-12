import org.joml.Vector3f;

public class main {

    public static void main(String args[]) {

        System.out.println("test");

        Window window = new Window(960, 540, "TextAdventureJava");

        window.use();
        window.setVSync(true);
        window.setBackgroundColor(new Vector3f(0.0f, 0.0f, 1.0f));

        while (!window.shouldClose()) {
            window.clear();

            // Rendering...

            window.swapBuffers();
            Window.pollEvents();
        }

        window.destroy();

    }

}