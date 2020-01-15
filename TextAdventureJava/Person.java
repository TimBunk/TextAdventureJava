import framework.Sprite;

import java.util.ArrayList;

/**
 * @author Ruben Eekhof rubeneekhof@gmail.com
 */
public class Person extends Inspectable {

    private Sprite sprite;
    private ArrayList<String> answers;

    public Person(String name, String description, Sprite sprite) {
        super(name, description);
        this.sprite = sprite;
        answers = new ArrayList<>();
    }

    // Getters
    public Sprite getSprite() {
        return sprite;
    }

    /**
     * Voegt een antwoord toe aan het antwoorden lijstje
     * @param answer het antwoord dat je wil toevoegen
     */
    public void addAnswer(String answer) {
        answers.add(answer);
    }

    /**
     * Krijg het antwoord doormiddel van de index
     * @param index de index
     * @return als de index out of bounds is dan wordt er null gereturned en anders het antwoord
     */
    public String getAnswer(int index) {
        if (index < 0 || index >= answers.size()) { return null; }
        return answers.get(index);
    }
}
