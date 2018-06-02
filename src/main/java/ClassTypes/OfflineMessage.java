package main.java.ClassTypes;

import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.requests.Route;

import java.util.List;

public class OfflineMessage {

    private User user;
    private List<Role> roles;
    private String messageRaw;
    private TextChannel textChannel;

    public OfflineMessage(User userin, List<Role> rolesin, String messageRawin, TextChannel textChannelin){
        user = userin;
        roles = rolesin;
        messageRaw = messageRawin;
        textChannel = textChannelin;
    }

    public User getUser() {
        return user;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public String getMessageRaw() {
        return messageRaw;
    }

    public TextChannel getTextChannel() {
        return textChannel;
    }
}
