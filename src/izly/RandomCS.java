package izly;

import java.util.Random;

// Uniquement si nous n'utilisons pas mockito inline pour mocker la classe Random
public class RandomCS {

    private Random random;

    public RandomCS() {
        random = new Random();
    }

    public int nextInt(int borne) {
        return random.nextInt(borne);
    }

}
