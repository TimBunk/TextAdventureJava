package framework;

import java.util.Comparator;

/**
 * @author Tim Bunk
 * In deze SortSprite class staat een compare functie voor het sorteren van sprites.
 * Sprites worden gesorteerd op layers, dan textures, dan id's
 */
public class SortSprite implements Comparator<Sprite> {

    // https://www.geeksforgeeks.org/collections-sort-java-examples/
    public int compare(Sprite sprite1, Sprite sprite2) {
        // Layer > texture > id
        int layerDiff = sprite1.getLayer() - sprite2.getLayer();
        if (layerDiff != 0) {
            return layerDiff;
        }
        int textureIDDiff = sprite1.getTextureID() - sprite2.getTextureID();
        if (textureIDDiff != 0) {
            return textureIDDiff;
        }
        return sprite1.getID() - sprite2.getID();
    }
}