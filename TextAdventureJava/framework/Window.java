import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL;
import org.lwjgl.glfw.GLFWErrorCallback;
// Door het gebruik van import static kunnen we de de static functies van GLFW direct aanroepen zonder eerst de class naam te benoemen
import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.glfw.GLFW.*;

/**
 * @author Tim Bunk
 * Dit is de window class die gebruikt maakt van de GLFW library. De window gebruikt versie 3.3 van OpenGL.
 */
public class Window {

    private static boolean glfw_initialized = false;
    private static int windowCount = 0;

    private int width, height;
    private String title;
    private long window;
    private Vector3f backgroundColor;
    private float[] projection;

    private double lastTime;
    private double deltaTime;

    /**
     * In de constructor word gecheckt of glfw al geinitialiseerd is anders wordt hij geinitialiseerd ook wordt de window hier gemaakt doormiddel van glfw
     * @param width de breedte van de window
     * @param height de hoogte van de window
     * @param title de titel van de window
     */
    Window(int width, int height, String title) {
        // Initializeer alle variablen
        this.width = width;
        this.height = height;
        this.title = title;
        backgroundColor = new Vector3f(0.0f, 0.0f, 0.0f);
        // De reden dat ik de project matrix4f omzet in een array van floats is omdat de shaders die deze matrix gaan gebruiken alleen maar een array van floats accepteren
        Matrix4f projectionMatrix = new Matrix4f().identity();
        projectionMatrix.ortho(0.0f, width, 0.0f, height, -100.0f, 100.0f);
        projection = new float[16];
        projection = projectionMatrix.get(projection);

        if (!Window.glfw_initialized) {
            // Initializeer glfw als dat nog niet gedaan was
            if (glfwInit()) {
                Window.glfw_initialized = true;
                // Maak een error callback voor wanneer glfw een error aan ons wil doorgeven
                glfwSetErrorCallback((error, description) -> {
                    throw new RuntimeException(String.format("GLFW Error: %d, beschrijving: %s.", error, GLFWErrorCallback.getDescription(description)));
                });
            } else {
                throw new RuntimeException("GLFW Error: Er ging iets mis bij het initializeren van glfw.");
            }
        }
        // Gebruik versie 3.3 van openGL
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        // De window kan niet van grootte worden verandert
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE); // the window will be resizable
        // Maak de window
        window = glfwCreateWindow(this.width, this.height, this.title, 0, 0);
        // Creeer een error als de window niet met succes was gemaakt
        if (window == 0) {
            throw new RuntimeException("GLFW Error: Kon de glfw window niet aanmaken.");
        }
        // De windowCount gaat met 1 omhoog omdat deze window met succes is gemaakt
        Window.windowCount++;

        lastTime = 0.0f;
        deltaTime = 0.0f;
    }

    // Getters
    public int getWidth()                { return width; }
    public int getHeight()               { return height; }
    public String getTitle()             { return title; }
    public Vector3f getBackgroundColor() { return backgroundColor; }
    public float[] getProjection()       { return projection; }
    public double getDeltaTime()         { return deltaTime; }
    // Setters
    public void setBackgroundColor(Vector3f backgroundColor)    { this.backgroundColor = backgroundColor; }
    public void setVSync(boolean state)                         { glfwSwapInterval((state ? 1: 0)); }

    public void setInput(Input input) {
        glfwSetKeyCallback(window, input);
        glfwSetCharCallback(window, input);
    }

    /**
     * Voor dat je de window kan gebruiken moet je use() een keer aanroepen om aan te geven dat dit de window is die je nu wilt gebruiken.
     * Als je meerdere windows hebt zorg er dan voor dat je use() een keer aanroept voor de huidige window waar je mee bezig bent.
     */
    public void use() {
        glfwMakeContextCurrent(window);
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();
    }

    /**
     * @return Als de gebruiker op het kruisje rechts boven in de window heeft geklikt dan geeft shouldClose true en anders false
     */
    public boolean shouldClose() {
        return glfwWindowShouldClose(window);
    }

    /**
     * De window wordt leeggemaakt van alles wat er gerendert is op de window en de backgroundcolor wordt toegepast.
     * Voer deze functie uit voordat je begint met renderen
     */
    public void clear() {
        glClearColor(backgroundColor.x, backgroundColor.y, backgroundColor.z, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT);
    }

    public void update() {
        double currentTime = glfwGetTime();
        deltaTime = currentTime - lastTime;
        lastTime = currentTime;
    }

    /**
     * Verwisselt de buffers om alles wat er gerendert is zichtbaar te maken.
     * Voer deze functie uit na dat je gerendert hebt.
     */
    public void swapBuffers() {
        glfwSwapBuffers(window);
    }

    /**
     * Als je de window niet meer gebruikt roep dan deze functie is aan om allocated geheugen weer vrij te maken.
     * Als je alle windows hebt laten destroyen dan wordt glfw gedeinitialiseerd.
     */
    public void destroy() {
        glfwDestroyWindow(window);
        Window.windowCount--;
        // Als er geen windows meer zijn dan deinitialiseer gflw
        if (Window.windowCount == 0) {
            Window.glfw_initialized = false;
            glfwTerminate();
        }
    }

    /**
     * Haalt alle events op die er zijn gebeurt voor glfw.
     * Het is belangrijk deze aan het eind van je frame aan te roepen om alle huidige events op te halen zoals keyboard en window events van glfw.
     */
    public void pollEvents() {
        glfwPollEvents();
    }
}