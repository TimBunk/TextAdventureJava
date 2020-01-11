import java.io.IOException;

public class Core {

    private Window window;
    private Renderer renderer;
    private Input input;

    Core(Window window) throws IOException {
        this.window = window;
        window.use();
        input = Input.init();
        window.setInput(input);
        renderer = new Renderer();
    }

    public void update(Scene scene) {

        window.clear();
        window.update();

        scene.update(window.getDeltaTime());
        scene.draw();
        renderer.render(scene, window.getProjection());
        scene.clear();

        input.update();
        window.swapBuffers();
        window.pollEvents();
    }

    public void destroy() {
        renderer.destroy();
    }

    // Getters
    public Window getWindow() { return window; }

    // Setters
    public void setWindow(Window window) {
        this.window = window;
        window.setInput(input);
    }

}