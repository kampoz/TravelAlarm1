package com.kampoz.travelalarm.networking;

import android.content.Context;

import com.kampoz.travelalarm.global.Constants;
import com.kampoz.travelalarm.global.GoogleTransportMode;


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

    public GetRouteDetailsRequest(Context context, String fromId, String toId, String transportMode) {
        super(context);
        this.fromId = fromId;
        this.toId = toId;
        this.transportMode = transportMode;
    }

    @Override
    public String getEndpoint() {
        return Constants.RECUEST_URL+"origin=place_id:"+fromId+"&destination=place_id:"+toId+"&mode="+transportMode;

                //https://maps.googleapis.com/maps/api/directions/json?origin=Toronto&destination=Montreal&avoid=highways&mode=bicycling
    }


}
