package royalties;

/**
 * Basic implementation of a tuple which is so useful in Scala
 */
public class ValuePair<T, U> {
    private T value1;
    private U value2;

    public ValuePair(T v1, U v2) {
        value1 = v1;
        value2 = v2;
    }

    public T getValue1() {
        return value1;
    }

    public U getValue2() {
        return value2;
    }
}
