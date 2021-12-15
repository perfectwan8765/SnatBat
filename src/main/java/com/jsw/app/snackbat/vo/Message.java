package com.jsw.app.snackbat.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Message {

    private String from;
    private String text;
    private String time;
    private String type;

}
