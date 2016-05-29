package com.alex.schwartzman.fivehundredpx.model;

import com.alex.schwartzman.fivehundredpx.provider.DefaultContract;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Collection;

@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressWarnings("UnusedDeclaration")
@DatabaseTable(tableName = DefaultContract.PageColumns.TABLE)
public class PageWithPhotos {
    @DatabaseField(id = true)
    @JsonProperty("current_page")
    private int id;

    @JsonProperty("total_pages")
    @DatabaseField(columnName = DefaultContract.PageColumns.TOTAL_PAGES)
    public String total_pages;

    @JsonProperty("photos")
    @ForeignCollectionField(eager = false)
    private Collection<ImageInfo> photos;

    public Collection<ImageInfo> getPhotos() {
        return photos;
    }

    public void setPhotos(Collection<ImageInfo> items) {
        this.photos = items;
    }
}
