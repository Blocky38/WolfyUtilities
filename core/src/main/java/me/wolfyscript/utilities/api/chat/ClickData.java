package me.wolfyscript.utilities.api.chat;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public class ClickData {

    private final String message;
    private final ClickAction clickAction;
    private final List<ChatEvent> clickEvents;
    private final boolean discard;

    public ClickData(String message, @Nullable ClickAction clickAction, boolean discard, ChatEvent... clickEvents) {
        this.clickAction = clickAction;
        this.message = message;
        this.clickEvents = Arrays.asList(clickEvents);
        this.discard = discard;
    }

    public ClickData(String message, @Nullable ClickAction clickAction, ChatEvent... clickEvents) {
        this(message, clickAction, false, clickEvents);
    }

    public ClickData(String message, @Nullable ClickAction clickAction) {
        this(message, clickAction, new ChatEvent[]{});
    }

    public ClickAction getClickAction() {
        return clickAction;
    }

    public String getMessage() {
        return message;
    }

    public List<ChatEvent> getChatEvents() {
        return clickEvents;
    }

    public boolean isDiscard() {
        return discard;
    }
}
