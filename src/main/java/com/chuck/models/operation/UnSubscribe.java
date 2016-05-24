package com.chuck.models.operation;

import com.chuck.App;
import com.chuck.models.listener.UserOperation;
import com.chuck.models.response.UnSubscribeResponse;
import org.drawers.bot.lib.DrawersBotString;
import org.drawers.bot.lib.Response;
import org.junit.runner.notification.RunListener;

/**
 * Created by nishant.pathak on 23/05/16.
 */
@RunListener.ThreadSafe
public class UnSubscribe implements UserOperation {
    @Override
    public Response operateInternal(DrawersBotString drawersBotString) {
        // Unsubscribe from the list
        return new UnSubscribeResponse();
    }

    @Override
    public boolean validateAndParse(DrawersBotString drawersBotString) {
        return true;
    }

    @Override
    public Response operateUser(String userId, DrawersBotString body) {
        App.users.remove(userId);
        return operate(body);
    }
}
