package com.lookingdev.api.Domain.Models;

import com.lookingdev.api.Domain.Enums.QueueAction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageStatus {
    private QueueAction action;
    private String status;

}
