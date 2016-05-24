package com.chuck.models.operation;

import com.chuck.App;
import com.chuck.User;
import com.chuck.models.listener.UserOperation;
import com.chuck.models.response.SubscribeResponse;
import org.drawers.bot.lib.DrawersBotString;
import org.drawers.bot.lib.Response;
import org.junit.runner.notification.RunListener;

/**
 * Created by nishant.pathak on 23/05/16.
 */
@RunListener.ThreadSafe
public class Subscribe implements UserOperation {
    @Override
    public Response operateInternal(DrawersBotString drawersBotString) {
        // Subscribe to joke listeners
        return new SubscribeResponse();
    }

    @Override
    public boolean validateAndParse(DrawersBotString drawersBotString) {
        return true;
    }

    @Override
    public Response operateUser(String userId, DrawersBotString body) {
        App.users.put(userId, new User(userId));
        return operate(body);
    }
}
