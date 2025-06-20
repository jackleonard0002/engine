package engine.util;

import java.io.Serializable;
import java.util.Stack;
import java.util.function.Consumer;

public class ConsumerStack<T> implements Serializable {

    private final Stack<Consumer<T>> stackConsumers = new Stack<>();

    public void pushToStack(Consumer<T> stackConsumer) {
        stackConsumers.push(stackConsumer);
    }

    public void peekStack() {
        stackConsumers.peek();
    }

    public void onAccept(T scene) {
        for (Consumer<T> stackConsumer : stackConsumers) {
            stackConsumer.accept(scene);
        }
    }

}
