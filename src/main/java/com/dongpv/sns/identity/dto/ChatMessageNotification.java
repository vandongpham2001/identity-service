package com.dongpv.sns.identity.dto;

import java.io.Serializable;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatMessageNotification implements Serializable {
    Long channelId;
    Long senderId;
}
