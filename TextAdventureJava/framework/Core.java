import org.joml.Vector3f;

import java.io.IOException;

public class Core {

    private Window window;
    private Renderer renderer;

    Core(Window window) throws IOException {
        this.window = window;
        window.use();
        renderer = new Renderer();
    }

    public void update(Scene scene) {

        window.clear();
        window.update();

        scene.update(window.getDeltaTime());
        renderer.render(scene, window.getProjection());
        scene.clear();

        window.swapBuffers();
        window.pollEvents();
    }

    public void destroy() {
        renderer.destroy();
    }

    // Getters
    public Window getWindow() { return window; }

    // Setters
    public void setWindow(Window window) { this.window = window; }

}