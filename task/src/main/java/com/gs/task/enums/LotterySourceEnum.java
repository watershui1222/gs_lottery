package com.gs.task.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum LotterySourceEnum {

    DUOCAI("duocai", "多采网"),
    ;
    private String code;
    private String name;


    }
