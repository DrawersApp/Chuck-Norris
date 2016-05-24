package com.chuck.models.listener;

import org.drawers.bot.lib.DrawersBotString;
import org.drawers.bot.lib.Operation;
import org.drawers.bot.lib.Response;

/**
 * Created by nishant.pathak on 24/05/16.
 */
public interface UserOperation extends Operation {
    Response operateUser(String userId, DrawersBotString body);
}
