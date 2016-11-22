package com.kaseka.boxmaptest1;

import android.content.Context;


public class GetRouteDetailsRequest extends BaseRequest {


    private String fromId;
    private String toId;
    private String transportMode;

    public GetRouteDetailsRequest(Context context, String fromId, String toId, GoogleTransportMode transportMode) {
        super(context);
        this.fromId = fromId;
        this.toId = toId;
        this.transportMode = transportMode.toString();
    }

    @Override
    public String getEndpoint() {
        return "https://maps.googleapis.com/maps/api/directions/json?origin=place_id:"+fromId+"&destination=place_id:"+toId+"&mode="+transportMode;

                //https://maps.googleapis.com/maps/api/directions/json?origin=Toronto&destination=Montreal&avoid=highways&mode=bicycling
    }


}
