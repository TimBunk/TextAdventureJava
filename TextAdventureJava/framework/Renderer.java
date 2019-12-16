import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;
import static org.lwjgl.opengl.GL33.*;

public class Renderer {

    List<Sprite> sprites;
    Shader spriteShader;
    int VAO, VBO, EBO;

    Renderer() throws IOException {
        // Door deze twwee functies aan te roepen is het mogelijk om plaatjes met doorzichtige achtergronden te gebruiken
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable( GL_BLEND );

        sprites = new ArrayList<Sprite>();
        spriteShader = new Shader("Shaders/Sprite.glsl");

        float[] vertices =  {
                // Positions        // texture coords
                250.0f,  250.0f, 0.0f,  1.0f, 1.0f,  // top right
                250.0f, -250.0f, 0.0f,  1.0f, 0.0f,  // bottom right
                -250.0f, -250.0f, 0.0f, 0.0f, 0.0f,  // bottom left
                -250.0f,  250.0f, 0.0f, 0.0f, 1.0f // top left
        };
        int[] indices = {
                0, 1, 3,
                1, 2, 3
        };
        // Creeer de vertex array en de buffers
        VAO = glGenVertexArrays();
        glBindVertexArray(VAO);
        VBO = glGenBuffers();
        EBO = glGenBuffers();
        // Kopieer vertices in vertex buffer
        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        glBufferData(GL_ARRAY_BUFFER, new float[16], GL_DYNAMIC_DRAW);
        // Kopieer indexen in element buffer
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
        // Set de vertext attrib pointers
        glVertexAttribPointer(0, 2, GL_FLOAT, false, 4 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 4 * Float.BYTES, 2 * Float.BYTES);
        glEnableVertexAttribArray(1);
    }

    public void addSprite(Sprite sprite) { sprites.add(sprite); }
    public void clear() { sprites.clear(); }

    public void render(Window window) {
        window.clear();

        // Pak de projection matrix
        float[] matrixValues = new float[16];
        matrixValues = window.getProjection().get(matrixValues);

        // Gebruik de shader
        spriteShader.use();
        spriteShader.setUniformMatrix4f("projection", matrixValues);

        // Bind de VAO
        glBindVertexArray(VAO);
        // Loop door het lijstje met sprites en render ze
        float[] modelValues = new float[16];
        for (int i=0;i<sprites.size();i++) {
            Sprite sprite = sprites.get(i);
            Texture t = sprite.getTexture();
            if (t != null) {
                t.use();
            } else {
                glBindTexture(GL_TEXTURE_2D, 0);
            }
            Matrix4f model = new Matrix4f().identity();
            model = model.rotate((float)Math.toRadians(sprite.getRotation()), 0.0f, 0.0f, 1.0f);
            modelValues = model.get(modelValues);
            spriteShader.setUniformMatrix4f("model", modelValues);
            float[] vertices = sprite.getVertices();
            glBindBuffer(GL_ARRAY_BUFFER, VBO);
            glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);
            glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
        }

        window.swapBuffers();
    }

    public void destroy() {
        spriteShader.destroy();
    }
}