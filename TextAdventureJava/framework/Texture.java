package framework;

import org.lwjgl.stb.STBImage;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL33.*;

/**
 * @author Tim Bunk
 * Dit is de texture class die alle informatie bevat over een openGL texture
 */
public class Texture {

    private int textureID;

    /**
     * Constructor voor texture waar de afbeelding wordt geladen en opgeslagen in het geheugen
     *
     * @param filePath lokaal path naar de afbeelding
     */
    public Texture(String filePath) {
        // Make the filePath absolute
        //filePath = getClass().getResource(filePath).getPath().substring(1);
        // Genereer texture
        textureID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureID);
        // Set de texture wrapping parameters
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        // Set de texture filtering parameters
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        // Laad de afbeelding met behulp van de STB library
        int[] width = new int[1];
        int[] height = new int[1];
        int[] nrChannels = new int[1];
        STBImage.stbi_set_flip_vertically_on_load(true);
        ByteBuffer data = STBImage.stbi_load(filePath, width, height, nrChannels, 0);
        // Check of de data met succes is opgehaald
        if (data == null) {
            throw new RuntimeException(String.format("Error tijdens het laden van de texture: %s, berschrijving: %s", filePath, STBImage.stbi_failure_reason()));
        }
        // Set de texture data
        if (nrChannels[0] == 3) {
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width[0], height[0], 0, GL_RGB, GL_UNSIGNED_BYTE, data);
        } else if (nrChannels[0] == 4) {
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width[0], height[0], 0, GL_RGBA, GL_UNSIGNED_BYTE, data);
        }
        glGenerateMipmap(GL_TEXTURE_2D);
        // Maak de allocated geheugen weer vrij
        STBImage.stbi_image_free(data);
    }

    // Getter
    public int getID() {
        return textureID;
    }

    /**
     * Roep deze functie aan als je deze texture wilt gebruiken. Doe dit voor de draw call
     */
    public void use() {
        glBindTexture(GL_TEXTURE_2D, textureID);
    }

    /**
     * Roep deze functie aan wanneer je de texture niet meer gebruikt en het allocated geheugen weer vrij wilt maken
     */
    public void destroy() {
        glDeleteTextures(textureID);
    }
}