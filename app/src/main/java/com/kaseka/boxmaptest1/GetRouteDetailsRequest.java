package com.kaseka.boxmaptest1;

import android.content.Context;

/**
 * Created by wasili on 2016-09-26.
 */
public class GetRouteDetailsRequest extends BaseRequest {

    private String fromId;
    private String toId;

    public GetRouteDetailsRequest(Context context, String fromId, String toId) {
        super(context);
        this.fromId = fromId;
        this.toId = toId;
    }

    @Override
    public String getEndpoint() {
        return "https://maps.googleapis.com/maps/api/directions/json?origin=place_id:"+fromId+"&destination=place_id:"+toId;
    }
}
