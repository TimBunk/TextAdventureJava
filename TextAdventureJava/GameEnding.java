import framework.*;
import org.joml.Vector2f;
import static org.lwjgl.glfw.GLFW.*;

import java.io.IOException;

/**
 * Deze scene is een visueel plaatje van of je de game hebt gewonnen of verloren.
 * @author Tim Bunk
 */
public class GameEnding extends Scene {

    private boolean replay;
    private Text textFinalDialogue;
    private Text textCredits;
    private Text textArrested;
    private Text textMurderer;
    Sprite spriteArrested;
    Sprite spriteMurderer;

    GameEnding(Person arrested, Person murderer) throws IOException {
        replay = false;
        spriteArrested = arrested.getSprite();

        Font font = fontManager.load("Resources/Fonts/OCR_A_Extended");
        textFinalDialogue = new Text(font, "");
        textFinalDialogue.setSize(14);
        textFinalDialogue.setPosition(new Vector2f(266, 475));
        textFinalDialogue.setMaxWidth(428);
        textCredits = new Text(font,
                Localization.getString(Localization.Text.DEVELOPED_BY)
        );
        textCredits.setSize(14);
        textCredits.setPosition(new Vector2f(266, 130));
        if (arrested.getName().equals(murderer.getName())) {
            spriteMurderer = new Sprite(spriteArrested);
            textFinalDialogue.setString(
                Localization.getString(Localization.Text.YOU_WON) + arrested.getName() + "\n" +
                Localization.getString(Localization.Text.REPLAY)
            );
        }
        else {
            spriteMurderer = murderer.getSprite();
            textFinalDialogue.setString(
                Localization.getString(Localization.Text.YOU_LOST) + arrested.getName() + "\n" +
                Localization.getString(Localization.Text.ACTUAL_MURDERER) + murderer.getName() + "\n" +
                Localization.getString(Localization.Text.REPLAY)
            );
        }
        textArrested = new Text(font, Localization.getString(Localization.Text.ARRESTED));
        textArrested.setSize(14);
        textArrested.setPosition(new Vector2f(266, 348));
        textMurderer = new Text(font, Localization.getString(Localization.Text.MURDERER));
        textMurderer.setSize(14);
        textMurderer.setPosition(new Vector2f(566, 348));

        spriteArrested.setPosition(new Vector2f(330, 270));
        spriteMurderer.setPosition(new Vector2f(630, 270));
    }

    @Override
    public void update(double deltaTime) {
        if (Input.key(GLFW_KEY_ENTER, Key.KeyState.PRESSED)) {
            replay = true;
        }
    }

    @Override
    public void draw() {
        draw(textFinalDialogue);
        draw(textCredits);
        draw(spriteArrested);
        draw(spriteMurderer);
        draw(textArrested);
        draw(textMurderer);
    }

    public boolean shouldClose() {
        return replay;
    }
}