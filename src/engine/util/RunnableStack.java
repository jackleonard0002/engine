package engine.util;

import java.util.Stack;

public class RunnableStack {
    private final Stack<Runnable> stackConsumers = new Stack<>();

    public void pushToStack(Runnable stackConsumer) {
        stackConsumers.push(stackConsumer);
    }

    public void peekStack() {
        stackConsumers.peek();
    }

    public void onRun() {
        for (Runnable stackConsumer : stackConsumers) {
            stackConsumer.run();
        }
    }
}
