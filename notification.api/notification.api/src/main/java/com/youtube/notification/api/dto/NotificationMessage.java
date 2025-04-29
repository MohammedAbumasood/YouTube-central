package com.youtube.notification.api.dto;

import lombok.Data;

@Data
public class NotificationMessage {

    String purpose; //user-registration, video-upload-notification, new-video-available
    String email; //user we need to send notification
    String name; //name of the user
}
