package com.epam.training.ticketservice.prompt;


import org.jline.utils.AttributedString;
import org.springframework.shell.jline.PromptProvider;
import org.springframework.stereotype.Component;

@Component
public class PromptProviderImpl implements PromptProvider {

    public AttributedString getPrompt() {
        return new AttributedString("Ticket service>");
    }
}
