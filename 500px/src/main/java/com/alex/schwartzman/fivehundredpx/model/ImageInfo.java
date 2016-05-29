package com.alex.schwartzman.fivehundredpx.model;

import com.alex.schwartzman.fivehundredpx.provider.DefaultContract;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressWarnings("UnusedDeclaration")
@DatabaseTable(tableName = DefaultContract.ImageColumns.TABLE)
public class ImageInfo {
    @DatabaseField(id = true, columnName = DefaultContract.ImageColumns._ID)
    @JsonProperty("id")
    private int id;

    @JsonProperty("name")
    @DatabaseField(columnName = DefaultContract.ImageColumns.NAME)
    public String name;

    @JsonProperty("camera")
    @DatabaseField(columnName = DefaultContract.ImageColumns.CAMERA)
    public String camera;

    @JsonProperty("image_url")
    @DatabaseField(columnName = DefaultContract.ImageColumns.URI)
    public String image_url;

    @JsonProperty("width")
    @DatabaseField(columnName = DefaultContract.ImageColumns.WIDTH)
    public int width;

    @JsonProperty("height")
    @DatabaseField(columnName = DefaultContract.ImageColumns.HEIGHT)
    public int height;

    @JsonProperty("rating")
    @DatabaseField(columnName = DefaultContract.ImageColumns.RATING, index = true)
    public float rating;

    @DatabaseField(foreign = true)
    private PageWithPhotos page;

    @JsonProperty("user")
    private Author author;

    @DatabaseField(columnName = DefaultContract.ImageColumns.AUTHOR_NAME, useGetSet = true)
    public String author_name;

    public String getAuthor_name() {
        if (author == null) {
            return "";
        }
        author_name = author.getFullName();
        return author_name;
    }

    public void setAuthor_name(String fullname) {
        author_name = fullname;
    }
}
