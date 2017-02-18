package Tools;

import org.xutils.http.annotation.HttpResponse;

/**
 * Created by Administrator on 2017/2/15.
 */

@HttpResponse(parser = JsonResponseParser.class)
public class CommonResponse {
    private String test;

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    @Override
    public String toString() {
        return test;
    }
}
