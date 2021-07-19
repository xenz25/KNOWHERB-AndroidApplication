package com.example.kowherbz.holder;

import java.io.Serializable;

/**
 * class Title Holder - for Title and Definition
 */
public class TitleHolder implements Serializable {
    private final String titleOfSickness;
    private final String definitionOfSickness;

    public TitleHolder(String titleOfSickness, String definitionOfSickness) {
        this.titleOfSickness = titleOfSickness;
        this.definitionOfSickness = definitionOfSickness;
    }

    public String getTitleOfSickness() {
        return titleOfSickness;
    }

    public String getDefinitionOfSickness() {
        return definitionOfSickness;
    }
}
