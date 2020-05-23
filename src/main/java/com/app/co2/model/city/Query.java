package com.app.co2.model.city;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Query
{
    @SerializedName(value="private")
    private String isPrivate;

    private String size;

    private String parser;

    private String querySize;

    private Parsed_text parsed_text;

    private String[] layers;

    private String text;

    private Lang lang;

}
