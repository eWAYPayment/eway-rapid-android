package rapidandroidsdk.eway.com.rapidandroidsdk.bus;

import com.squareup.otto.Bus;

/**
 * Created by alexanderparra on 31/10/15.
 */
public final class BusProvider {
    private static final Bus BUS = new Bus();

    public static Bus getInstance(){return BUS;}

    private BusProvider(){}
}
