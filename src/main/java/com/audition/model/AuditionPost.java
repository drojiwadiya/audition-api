package com.audition.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class AuditionPost {

    private int userId;
    private int id;
    private String title;
    private String body;

}
