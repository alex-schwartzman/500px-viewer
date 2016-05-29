package com.alex.schwartzman.fivehundredpx.network.robospice;

import com.alex.schwartzman.fivehundredpx.model.PageWithPhotos;
import com.alex.schwartzman.fivehundredpx.network.retrofit.FiveHundredPxApi;

public class GetImagesListRequest extends BaseRequest<PageWithPhotos, FiveHundredPxApi> {

    public static final String DEFAULT_FEATURE = "popular";
    public static final String MY_CONSUMER_KEY = "wB4ozJxTijCwNuggJvPGtBGCRqaZVcF6jsrzUadF";
    public static final int DEFAULT_IMAGE_SIZE = 20; //the requirements say we should use size 3, but it really sucks - it loses 40% of the image because of cropping.
    private final int mPage;

//    https://github.com/500px/api-documentation/blob/master/basics/formats_and_terms.md
//    These are the standard cropped sizes:
//
//    ID	Dimensions
//    1	70px x 70px
//    2	140px x 140px
//    3	280px x 280px
//    100	100px x 100px
//    200	200px x 200px
//    440	440px x 440px
//    600	600px x 600px
//    These are the standard uncropped sizes:
//
//    ID	Dimensions
//    4	900px on the longest edge
//    5	1170px on the longest edge
//    6	1080px high
//    20	300px high
//    21	600px high
//    30	256px on the longest edge
//    31	450px high
//    1080	1080px on the longest edge
//    1600	1600px on the longest edge
//    2048	2048px on the longest edge


    public GetImagesListRequest() {
        super(PageWithPhotos.class, FiveHundredPxApi.class);
        mPage = 1;
    }

    public GetImagesListRequest(int page) {
        super(PageWithPhotos.class, FiveHundredPxApi.class);
        mPage = page;
    }

    @Override
    public PageWithPhotos loadDataFromNetwork() {
        return getService().getImagesList(DEFAULT_FEATURE, MY_CONSUMER_KEY, DEFAULT_IMAGE_SIZE, mPage);
    }
}
