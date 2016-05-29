package com.alex.schwartzman.fivehundredpx.model;

import com.alex.schwartzman.fivehundredpx.provider.DefaultContract;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressWarnings("UnusedDeclaration")
@DatabaseTable(tableName = DefaultContract.AuthorColumns.TABLE)
public class Author {
    @DatabaseField(id = true, columnName = DefaultContract.AuthorColumns._ID)
    @JsonProperty("id")
    private int id;

    @JsonProperty("firstname")
    @DatabaseField(columnName = DefaultContract.AuthorColumns.FIRST_NAME)
    public String firstname;

    @JsonProperty("lastname")
    @DatabaseField(columnName = DefaultContract.AuthorColumns.LAST_NAME)
    public String lastname;

    public String getFullName() {
        return (firstname != null ? firstname : "") + " " + (lastname != null ? lastname : "");
    }
}
