package com.gcodedevelopers.kenyanrides.Notifications;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAfSuD9pA:APA91bELvwKHjNfaO_jwVhJsRbqZYd6zo4x7OergeYe-0QhUkGJ3KsY2_MKgcv0Ot4rcNngzeLJcM8-fLfNSKnOYlHUbC4Pho5itCVersnpPIJtcCTzR8piHMyzITClMbBwz8aeCnOYI"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);

}
