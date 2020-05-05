package es.deusto.mcu.classchat2020;

public class ChatMessage {
    private String id;
    private String messageText;
    private String senderName;
    private String senderAvatarURL;

    public ChatMessage(){}

    public ChatMessage(String messageText, String senderName, String senderAvatarURL) {
        this.messageText = messageText;
        this.senderName = senderName;
        this.senderAvatarURL = senderAvatarURL;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderAvatarURL() {
        return senderAvatarURL;
    }

    public void setSenderAvatarURL(String senderAvatarURL) {
        this.senderAvatarURL = senderAvatarURL;
    }
}
