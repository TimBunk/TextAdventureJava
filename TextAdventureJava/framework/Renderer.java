import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

import static org.lwjgl.opengl.GL33.*;

public class Renderer {

    List<Sprite> sprites;
    Shader spriteShader;
    int VAO_sprite, VBO_sprite, VBOInstance_sprite, EBO_sprite;

    List<Text> text;
    Shader textShader;
    int VAO_text, VBO_text;

    Renderer() throws IOException {
        // Door deze twwee functies aan te roepen is het mogelijk om plaatjes met doorzichtige achtergronden te gebruiken
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable( GL_BLEND );
        // Intialiseer de variablen
        sprites = new ArrayList<Sprite>();
        spriteShader = new Shader("Shaders/Sprite.glsl");

        float[] vertices = {
            // Position     // Texture coords
            1.0f, 1.0f,     1.0f, 1.0f,  // top right
            1.0f, -1.0f,    1.0f, 0.0f, // bottom right
            -1.0f, -1.0f,   0.0f, 0.0f,// bottom left
            -1.0f, 1.0f,    0.0f, 1.0f // top left
        };

        int[] indices = {
                0, 1, 3,
                1, 2, 3,
        };

        // Creeer de vertex array en de buffers
        VAO_sprite = glGenVertexArrays();
        glBindVertexArray(VAO_sprite);
        VBO_sprite = glGenBuffers();
        VBOInstance_sprite = glGenBuffers();
        EBO_sprite = glGenBuffers();
        // In deze buffer wordt ruimte vrij gemaakte voor 16 floats namelijk de vertices en uv's
        glBindBuffer(GL_ARRAY_BUFFER, VBO_sprite);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_DYNAMIC_DRAW);
        // Kopieer indexen in element buffer
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO_sprite);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
        // Set de vertext attrib pointers
        glVertexAttribPointer(0, 2, GL_FLOAT, false, 4 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 4 * Float.BYTES, 2 * Float.BYTES);
        glEnableVertexAttribArray(1);
        // In deze buffer komen de model matrix en de color te staan. Deze komen maar 1 keer per instance voor
        glBindBuffer(GL_ARRAY_BUFFER, VBOInstance_sprite);
        glBufferData(GL_ARRAY_BUFFER, 0, GL_DYNAMIC_DRAW);
        // Set de vertext attrib pointers
        glVertexAttribPointer(2, 4, GL_FLOAT, false, 20 * Float.BYTES, 0);
        glEnableVertexAttribArray(2);
        glVertexAttribPointer(3, 4, GL_FLOAT, false, 20 * Float.BYTES, 4 * Float.BYTES);
        glEnableVertexAttribArray(3);
        glVertexAttribPointer(4, 4, GL_FLOAT, false, 20 * Float.BYTES, 8 * Float.BYTES);
        glEnableVertexAttribArray(4);
        glVertexAttribPointer(5, 4, GL_FLOAT, false, 20 * Float.BYTES, 12 * Float.BYTES);
        glEnableVertexAttribArray(5);
        glVertexAttribPointer(6, 4, GL_FLOAT, false, 20 * Float.BYTES, 16 * Float.BYTES);
        glEnableVertexAttribArray(6);
        // Met de divisor geef ik aan dat de color en de model een keer per instance moeten worden gebruikt
        glVertexAttribDivisor(2, 1);
        glVertexAttribDivisor(3, 1);
        glVertexAttribDivisor(4, 1);
        glVertexAttribDivisor(5, 1);
        glVertexAttribDivisor(6, 1);

        // Initialiseer de variablen voor de text rendering
        text = new ArrayList<Text>();
        textShader = new Shader("Shaders/Text.glsl");
        VAO_text = glGenVertexArrays();
        glBindVertexArray(VAO_text);
        VBO_text = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, VBO_text);
        glBufferData(GL_ARRAY_BUFFER, 0, VBO_text);
        glVertexAttribPointer(0, 2, GL_FLOAT, false, 4 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 4 * Float.BYTES, 2 * Float.BYTES);
        glEnableVertexAttribArray(1);
    }

    public void add(Sprite sprite)  { sprites.add(sprite); }
    public void add(Text text)      { this.text.add(text); }
    public void clear()             { sprites.clear(); }

    public void render(Window window) {
        window.clear();

        if (sprites.size() > 0) {
            renderSprites(window.getProjection());
        }
        renderText(window.getProjection());

        window.swapBuffers();
    }

    public void destroy() {
        spriteShader.destroy();
        glDeleteBuffers(VBO_sprite);
        glDeleteBuffers(VBOInstance_sprite);
        glDeleteBuffers(EBO_sprite);
        glDeleteVertexArrays(VAO_sprite);

        textShader.destroy();
        glDeleteBuffers(VBO_text);
        glDeleteVertexArrays(VAO_text);
    }

    private void renderSprites(float[] projection) {
        // Sorteer de sprites. De sprites worden door de class SortSprite gesorteerd op layer > texture > id
        Collections.sort(sprites, new SortSprite());
        // Gebruik de shader
        spriteShader.use();
        spriteShader.setUniformMatrix4f("projection", projection);
        // Bind de VAO
        glBindVertexArray(VAO_sprite);
        // pak het eerste textureID waar we mee beginnen en bind de texture
        int textureID = sprites.get(0).getTextureID();
        // Maak een list voor de color/matrix
        List<Float> instances = new ArrayList<Float>();
        // Loop door het lijstje met sprites en render ze
        for (int i=0;i<sprites.size();i++) {
            // Pak de sprite
            Sprite sprite = sprites.get(i);
            // Set de color in de list voor instances
            Vector4f color = sprite.getColor();
            instances.add(color.x); instances.add(color.y); instances.add(color.z); instances.add(color.w);
            // Creeer de model matrix
            Matrix4f model = new Matrix4f().identity();
            Vector2f pos = sprite.getPosition();
            Vector2f pivot = sprite.getPivot();
            int width = sprite.getWidth();
            int height = sprite.getHeight();
            model = model.translate((pos.x - (pivot.x * 0.5f * width)), (pos.y - (pivot.y * 0.5f * height)), 0.0f);
            model = model.rotate((float)Math.toRadians(sprite.getRotation()), 0.0f, 0.0f, 1.0f);
            model = model.scale(width * 0.5f, height * 0.5f, 0.0f);
            // Set de model matrix in de list voor instances
            instances.add(model.m00()); instances.add(model.m01()); instances.add(model.m02()); instances.add(model.m03());
            instances.add(model.m10()); instances.add(model.m11()); instances.add(model.m12()); instances.add(model.m13());
            instances.add(model.m20()); instances.add(model.m21()); instances.add(model.m22()); instances.add(model.m23());
            instances.add(model.m30()); instances.add(model.m31()); instances.add(model.m32()); instances.add(model.m33());
            // Als we bij de laatste sprite zijn of de textureID is verandert dan renderen we alle sprites die we op het moment hebben verwerkt
            if (i+1 == sprites.size() || sprites.get(i+1).getTextureID() != textureID) {
                // Bind de texture
                glBindTexture(GL_TEXTURE_2D, textureID);
                // Als we nog niet bij de laatste sprite zijn dan updaten we de textureID met de nieuwste texture
                if (i+1 != sprites.size()) {
                    textureID = sprites.get(i+1).getTextureID();
                }
                // Set alle instance data in de buffer
                glBindBuffer(GL_ARRAY_BUFFER, VBOInstance_sprite);
                glBufferData(GL_ARRAY_BUFFER, toFloatArray(instances), GL_DYNAMIC_DRAW);
                // Draw. De count wordt berekend door instances.size()/20 want er zijn 20 floats per instance
                glDrawElementsInstanced(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0, instances.size()/20);
                instances.clear();
            }
        }
    }

    private void renderText(float[] projection) {
        textShader.use();
        textShader.setUniformMatrix4f("u_projection", projection);
        glBindVertexArray(VAO_text);
        Matrix4f model = new Matrix4f();
        for (int i=0;i<text.size();i++) {
            Text t = text.get(i);
            Font f = t.getFont();
            // Activeer de texture
            f.getTexture().use();
            // Set de color
            Vector4f color = t.getColor();
            textShader.setUniform4f("u_color", color.x, color.y, color.z, color.w);
            // Set de model matrix
            Vector2f position = t.getPosition();
            float rotation = t.getRotation();
            model = model.identity();
            model = model.translate(position.x, position.y, 0.0f);
            model = model.rotate((float)Math.toRadians(rotation), 0.0f, 0.0f, 1.0f);
            model = model.scale(1.0f, 1.0f, 0.0f);
            float[] modelValues = new float[16];
            modelValues = model.get(modelValues);
            textShader.setUniformMatrix4f("u_model", modelValues);

            String string = t.getString();
            float pos = 0.0f;
            List<Float>  vertices = new ArrayList<Float>();
            for (int j=0;j<string.length();j++) {
                char c = string.charAt(j);
                Glyph g = f.getGlyph(c);
                if (g != null) {
                    Vector4f uv = g.getUv();
                    Vector2f scale = g.getScale();
                    Vector2f offset = g.getOffset();
                    // Positions                                                            // UV's
                    // First triangle
                    vertices.add(pos+0.0f+offset.x); vertices.add(0.0f-offset.y-scale.y);       vertices.add(uv.x); vertices.add(uv.y); // Bottom left
                    vertices.add(pos+scale.x+offset.x); vertices.add(0.0f-offset.y); vertices.add(uv.z); vertices.add(uv.w); // Top right
                    vertices.add(pos+0.0f+offset.x); vertices.add(0.0f-offset.y);    vertices.add(uv.x); vertices.add(uv.w); // Top left
                    // Second triangle
                    vertices.add(pos+0.0f+offset.x); vertices.add(0.0f-offset.y-scale.y);       vertices.add(uv.x); vertices.add(uv.y); // Bottom left
                    vertices.add(pos+scale.x+offset.x); vertices.add(0.0f-offset.y); vertices.add(uv.z); vertices.add(uv.w); // Top right
                    vertices.add(pos+scale.x+offset.x); vertices.add(0.0f-offset.y-scale.y);    vertices.add(uv.z); vertices.add(uv.y); // Bottom right

                    pos += g.getXAdvance();
                }
            }
            // Draw
            glBindBuffer(GL_ARRAY_BUFFER, VBO_text);
            glBufferData(GL_ARRAY_BUFFER, toFloatArray(vertices), GL_DYNAMIC_DRAW);
            glDrawArrays(GL_TRIANGLES, 0, vertices.size());
        }
    }

    private float[] toFloatArray(List<Float> collection) {
        float[] floatArray = new float[collection.size()];
        for (int i=0;i<collection.size();i++) {
            floatArray[i] = collection.get(i);
        }
        return floatArray;
    }
}