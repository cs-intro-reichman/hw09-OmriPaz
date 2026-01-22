import java.util.HashMap;
import java.util.Random;

public class LanguageModel {

    // The map of this model.
    // Maps windows to lists of charachter data objects.
    HashMap<String, List> CharDataMap;
    
    // The window length used in this model.
    int windowLength;
    
    // The random number generator used by this model. 
	private Random randomGenerator;

    /** Constructs a language model with the given window length and a given
     *  seed value. Generating texts from this model multiple times with the 
     *  same seed value will produce the same random texts. Good for debugging. */
    public LanguageModel(int windowLength, int seed) {
        this.windowLength = windowLength;
        randomGenerator = new Random(seed);
        CharDataMap = new HashMap<String, List>();
    }

    /** Constructs a language model with the given window length.
     * Generating texts from this model multiple times will produce
     * different random texts. Good for production. */
    public LanguageModel(int windowLength) {
        this.windowLength = windowLength;
        randomGenerator = new Random();
        CharDataMap = new HashMap<String, List>();
    }

    /** Builds a language model from the text in the given file (the corpus). */
	public void train(String fileName) {
		String window = "";
        In in = new In(fileName);
        for (int i = 0; i < this.windowLength; i++) {
            window += in.readChar();
        }
        while (!in.isEmpty()) {
            char c = in.readChar();
            List probs = CharDataMap.get(window);
            if (probs == null) {
                probs = new List();
                CharDataMap.put(window, probs);
            }
            probs.update(c);
            window = window.substring(1) + c;
        }
        for (List probs : CharDataMap.values()) {
            calculateProbabilities(probs);
        }
	}

    // Computes and sets the probabilities (p and cp fields) of all the
	// characters in the given list. */
	void calculateProbabilities(List probs) {				
		int totalCount = 0;
        for (int i = 0; i < probs.getSize(); i++) {
            CharData cd = probs.get(i);
            totalCount += cd.count;
        }
        for (int i = 0; i < probs.getSize(); i++) {
            CharData cd = probs.get(i);
            cd.p = (double) cd.count / totalCount;
            cd.cp = cd.p;
            for (int j = 0; j < i; j++) {
                cd.cp += probs.get(j).p;
            }
        }
	}

    // Returns a random character from the given probabilities list.
	char getRandomChar(List probs) {
		double randomDouble = randomGenerator.nextDouble();
        for (int i = 0; i < probs.getSize(); i++) {
            CharData cd = probs.get(i);
            if (cd.cp >= randomDouble) {
                return cd.chr;
            }
        }
        return probs.getFirst().chr;  // Should not reach here
	}

	public String generate(String initialText, int textLength) {
        String text = initialText;
        int count = 0;
        while (count < textLength) {
            String window = text.substring(text.length() - windowLength, text.length());
            if (!CharDataMap.containsKey(window)) {
                return text;
            }
            List probs = CharDataMap.get(window);
            char randomChar = getRandomChar(probs);
            text += randomChar;
            count++;
        }
        return text;
	}

    /** Returns a string representing the map of this language model. */
	public String toString() {
		StringBuilder str = new StringBuilder();
		for (String key : CharDataMap.keySet()) {
			List keyProbs = CharDataMap.get(key);
			str.append(key + " : " + keyProbs + "\n");
		}
		return str.toString();
	}

    public static void main(String[] args) {
		// Your code goes here
        int windowLength = Integer.parseInt(args[0]);
        String initialText = args[1];
        int generateTextLength = Integer.parseInt(args[2]);
        Boolean randomGeneration = args[3].equals("random");
        String filename = args[4];

        LanguageModel lm;
        if (randomGeneration) {
            lm = new LanguageModel(windowLength);
        } else {
            lm = new LanguageModel(windowLength, 20);
        }
        lm.train(filename);
        System.out.println(lm.generate(initialText, generateTextLength));
    }
}
