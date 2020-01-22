package framework;

import java.io.IOException;

/**
 * @Author Tim Bunk
 * De core class is zoals de naam zegt is het de core van de framework. Je maakt deze aan en geeft een window mee die je wilt gebruiken. De core class die zorgt ervoor dat alles wordt upgedate elke frame
 */
public class Core {

    private Window window;
    private Renderer renderer;
    private Input input;

    /**
     * Initialiseert de input en renderer en zet de window aan door de use() functie
     *
     * @param window de window die op het moment gebruikt gaat worden
     * @throws IOException
     */
    public Core(Window window) throws IOException {
        this.window = window;
        window.use();
        input = Input.init();
        window.setInput(input);
        renderer = new Renderer();
    }

    /**
     * De update functie roept de functie clear, update, swapBuffers en pollEvents aan van window.
     * Ook worden de functies update, draw en clear aangeroepn voor scene en als laatst word de renderer ook nog aangeroepen om te renderen
     *
     * @param scene de scene die je wil updaten
     */
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

    /**
     * Roept de destroy functie aan voor de renderer
     */
    public void destroy() {
        renderer.destroy();
    }

    // Getters

    /**
     * Geeft de window terug
     * @return de huidige window die gebruikt wordt
     */
    public Window getWindow() {
        return window;
    }

    // Setters
    /**
     * zet de window en bindt de input class aan deze window
     * @param window de huidige window die je wilt gebruiken voor het renderen en voor de input
     */
    public void setWindow(Window window) {
        this.window = window;
        window.setInput(input);
    }

}
