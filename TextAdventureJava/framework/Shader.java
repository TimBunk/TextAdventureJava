package framework;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL33.*;

/**
 * @author Tim Bunk
 * Dit is de shader class die gebruikt wordt om shader programs te maken in openGL. Je kan een path opgeven naar een shader bestand en de shader programma wordt gemaakt
 */
public class Shader {

    private int shaderProgram;
    private String vertexShaderSource, fragmentShaderSource;

    /**
     * In deze constructor moet je een path opgeven naar een shader bestand waar zowel de vertex en fragment shader inbeschreven staan.
     * Om aan te geven welke gedeelte voor de vertex shader is en welke gedeelte voor de fragment shader moet je '!VERTEX' of '!FRAGMENT' boven je code zetten op een aparte regel.
     * Als je klaar bent met de dat gedeelte zet je een '!END' neer op een aparte regel zie ook de spriteShader.glsl als voorbeeld
     *
     * @param shaderFilePath path naar de shader bestand
     * @throws IOException als het opgegeven bestand niet gevonden wordt onstaat er een IOException error
     */
    public Shader(String shaderFilePath) throws IOException {
        // Make the shaderFilePath absolute
        shaderFilePath = getClass().getResource(shaderFilePath).getPath();
        // Get the shader soruce
        readShaderSource(shaderFilePath);
        // Create the shaders
        createShaders();
    }

    /**
     * In deze constructor moet je een path opgeven naar de vertex shader en de fragment shader
     *
     * @param vertexShaderFilePath   path naar de vertex shader bestand
     * @param fragmentShaderFilePath path naar de fragment shader bestand
     * @throws IOException als het opgegeven bestand niet gevonden wordt onstaat er een IOException error
     */
    Shader(String vertexShaderFilePath, String fragmentShaderFilePath) throws IOException {
        // Make the shaderFilePaths absolute
        vertexShaderFilePath = getClass().getResource(vertexShaderFilePath).getPath();
        fragmentShaderFilePath = getClass().getResource(fragmentShaderFilePath).getPath();
        // Get the shaders sources
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader(vertexShaderFilePath));
        vertexShaderSource = getShaderSource(sb, br);
        br = new BufferedReader(new FileReader(fragmentShaderFilePath));
        fragmentShaderSource = getShaderSource(sb, br);
        // Create the shaders
        createShaders();
    }

    /**
     * Voor dat je 'draw' kan gebruiken in openGL moet je eerst deze functie aanroepen om aan te geven dat je deze shader wilt gebruiken.
     * Ook als je een uniform wilt zetten voer dan eerst deze functie uit
     */
    public void use() {
        glUseProgram(shaderProgram);
    }

    /**
     * Maakt de allocated geheugen voor de shader programma weer vrij
     */
    public void destroy() {
        glDeleteShader(shaderProgram);
    }

    // Uniforms
    public void setUniform2f(String uniformName, float f0, float f1) {
        glUniform2f(getUniformLocation(uniformName), f0, f1);
    }

    public void setUniform3f(String uniformName, float f0, float f1, float f2) {
        glUniform3f(getUniformLocation(uniformName), f0, f1, f2);
    }

    public void setUniform4f(String uniformName, float f0, float f1, float f2, float f3) {
        glUniform4f(getUniformLocation(uniformName), f0, f1, f2, f3);
    }

    public void setUniformMatrix4f(String uniformName, float[] value) {
        glUniformMatrix4fv(getUniformLocation(uniformName), false, value);
    }

    public void setUniformMatrix4f(String uniformName, FloatBuffer value) {
        glUniformMatrix4fv(getUniformLocation(uniformName), false, value);
    }

    private int getUniformLocation(String uniformName) {
        return glGetUniformLocation(shaderProgram, uniformName);
    }

    private void createShaders() {
        // Creeer de shaders
        int vertexShader, fragmentShader;
        vertexShader = glCreateShader(GL_VERTEX_SHADER);
        fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);

        int[] compiled = new int[1];
        // Compileer vertex shader
        glShaderSource(vertexShader, vertexShaderSource);
        glCompileShader(vertexShader);
        glGetShaderiv(vertexShader, GL_COMPILE_STATUS, compiled);
        if (compiled[0] == 0) {
            throw new RuntimeException(String.format("Kon de vertex shader niet compileren, beschrijving: %s.", glGetShaderInfoLog(vertexShader)));
        }
        // Compileer fragment shader
        glShaderSource(fragmentShader, fragmentShaderSource);
        glCompileShader(fragmentShader);
        glGetShaderiv(fragmentShader, GL_COMPILE_STATUS, compiled);
        if (compiled[0] == 0) {
            throw new RuntimeException(String.format("Kon de fragment shader niet compileren, beschrijving: %s.", glGetShaderInfoLog(vertexShader)));
        }
        // Creeer een shader program
        shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertexShader);
        glAttachShader(shaderProgram, fragmentShader);
        glLinkProgram(shaderProgram);
        glGetProgramiv(shaderProgram, GL_COMPILE_STATUS, compiled);
        if (compiled[0] == 0) {
            throw new RuntimeException(String.format("Kon de shader program niet compileren, beschrijving: %s.", glGetProgramInfoLog(shaderProgram)));
        }
        // Verwijder de shaders
        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);
    }

    private void readShaderSource(String filePath) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String currentLine = "";
        while ((currentLine = br.readLine()) != null) {
            if (currentLine.equals("!VERTEX")) {
                vertexShaderSource = getShaderSource(sb, br);
            } else if (currentLine.equals("!FRAGMENT")) {
                fragmentShaderSource = getShaderSource(sb, br);
            }
        }
    }

    private String getShaderSource(StringBuilder sb, BufferedReader br) throws IOException {
        sb.setLength(0);
        String currentLine = "";
        while ((currentLine = br.readLine()) != null && currentLine.equals("!END") == false) {
            sb.append(currentLine).append("\n");
        }
        String vertexSource = sb.toString();
        sb.setLength(0);
        return vertexSource;
    }
}